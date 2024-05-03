package com.migueldev.todosimple.services;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.migueldev.todosimple.models.User;
import com.migueldev.todosimple.repositories.TaskRepository;
import com.migueldev.todosimple.repositories.UserRepository;

@Service
public class UserService {
    
    //Atributos da classe
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private TaskRepository taskRepository;

    public User findById(Long id) {
        Optional<User> user = this.userRepository.findById(id);

        //Caso encontre o ID então retorna o objeto de usuário, caso contrário retorna a RuntimeException com mensagem.
        return user.orElseThrow(() -> new RuntimeException(
            "Usuário não encontrado! " + id + ", Tipo: " + User.class.getName()
        ));
    }

    //Utilizar o Transactional para ter controle melhor da integridade em updates ou insert, irá salvar tudo ou nada, nunca pela metade.
    @Transactional
    public User create(User obj) {
        obj.setId(null); //Define nulo, Proteção para não permitir receber um id no create para que usuários mal intencionados não possam atualizar um registro na função de create ao passar um id.
        obj = this.userRepository.save(obj);
        this.taskRepository.saveAll(obj.getTasks());
        return obj;
    }

    @Transactional
    public User update(User obj) {
        User newObj = findById(obj.getId());
        newObj.setPassword(obj.getPassword());
        return this.userRepository.save(newObj);
    }

    public void delete(Long id) {
        findById(id);
        try {
            this.userRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Não é possível excluir pois há entidades relacionadas!");
        }
    }

}
