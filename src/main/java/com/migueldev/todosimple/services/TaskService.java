package com.migueldev.todosimple.services;

import java.util.List;
import java.util.Objects;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.migueldev.todosimple.models.Task;
import com.migueldev.todosimple.models.User;
import com.migueldev.todosimple.models.enums.ProfileEnum;
import com.migueldev.todosimple.repositories.TaskRepository;
import com.migueldev.todosimple.security.UserSpringSecurity;
import com.migueldev.todosimple.services.exception.AuthorizationException;
import com.migueldev.todosimple.services.exception.DataBindingViolationException;
import com.migueldev.todosimple.services.exception.InvalidInputException;
import com.migueldev.todosimple.services.exception.ObjectNotFoundException;

@Service
public class TaskService {
    
    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserService userService;

    public Task findById(Long id) {
        Task task = this.taskRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException(
            "Tarefa não encontrada! Id: " + ", Tipo: " + Task.class.getName()));

        UserSpringSecurity userSpringSecurity = UserService.authenticated();
        if (Objects.isNull(userSpringSecurity)
                || !userSpringSecurity.hasRole(ProfileEnum.ADMIN) && !userHasTask(userSpringSecurity, task))
            throw new AuthorizationException("Acesso negado!");

        return task;
    }

    public List<Task> findAllByUser(){
        UserSpringSecurity userSpringSecurity = UserService.authenticated();
        if (Objects.isNull(userSpringSecurity))
            throw new AuthorizationException("Acesso negado!");

        List<Task> tasks = this.taskRepository.findByUser_Id(userSpringSecurity.getId());
        return tasks;
    }

    public List<Task> findAll(){
        UserSpringSecurity userSpringSecurity = UserService.authenticated();
        if (Objects.isNull(userSpringSecurity)
                || !userSpringSecurity.hasRole(ProfileEnum.ADMIN))
            throw new AuthorizationException("Acesso negado!");

        List<Task> tasks = this.taskRepository.findAll();
        return tasks;
    }

    @Transactional
    public Task create(Task obj) {
        UserSpringSecurity userSpringSecurity = UserService.authenticated();
        if (Objects.isNull(userSpringSecurity))
            throw new AuthorizationException("Acesso negado!");

        User user = this.userService.findById(userSpringSecurity.getId());
        obj.setId(null);
        obj.setUser(user); //Define usuário relacionado a Task
        obj = this.taskRepository.save(obj);
        return obj;
    }

    @Transactional
    public Task update(Long id, Task obj) {
        Task newObj = findById(obj.getId());
        newObj.setDescription(obj.getDescription());
        return this.taskRepository.save(newObj);
    }

    public void delete(Long id) {
        findById(id);

        try {
            this.taskRepository.deleteById(id);
        } catch (Exception e) {
            throw new DataBindingViolationException("Não é possível excluir pois há entidades relacionadas!");
        }
    }

    public Boolean userHasTask(UserSpringSecurity userSpringSecurity, Task task) {
        return task.getUser().getId().equals(userSpringSecurity.getId());
    }
}
