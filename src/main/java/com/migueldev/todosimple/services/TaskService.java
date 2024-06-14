package com.migueldev.todosimple.services;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;
import javax.xml.bind.DataBindingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.migueldev.todosimple.models.Task;
import com.migueldev.todosimple.models.User;
import com.migueldev.todosimple.repositories.TaskRepository;
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
        Optional<Task> task = this.taskRepository.findById(id);
        
        return task.orElseThrow(() -> new ObjectNotFoundException(
            "Tarefa não encontrada! Id: " + ", Tipo: " + Task.class.getName()));
    }

    public List<Task> findAllByUserId(Long userId){
        List<Task> tasks = this.taskRepository.findByUser_Id(userId);
        return tasks;
    }

    @Transactional
    public Task create(Task obj) {
        User user = this.userService.findById(obj.getUser().getId());
        obj.setId(null);
        obj.setUser(user); //Define usuário relacionado a Task
        obj = this.taskRepository.save(obj);
        return obj;
    }

    @Transactional
    public Task update(Long id, Task obj) {
        Task newObj = findById(obj.getId());
        if(obj.getUser().getId() != newObj.getUser().getId()){
            throw new InvalidInputException("Não é permitido alterar o usuário de uma tarefa!");
        }else{
            newObj.setDescription(obj.getDescription());
            return this.taskRepository.save(newObj);
        }
    }

    public void delete(Long id) {
        findById(id);

        try {
            this.taskRepository.deleteById(id);
        } catch (Exception e) {
            throw new DataBindingViolationException("Não é possível excluir pois há entidades relacionadas!");
        }
    }
}
