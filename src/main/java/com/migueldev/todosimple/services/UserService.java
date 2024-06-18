package com.migueldev.todosimple.services;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.migueldev.todosimple.models.User;
import com.migueldev.todosimple.repositories.UserRepository;
import com.migueldev.todosimple.services.exception.DataBindingViolationException;
import com.migueldev.todosimple.services.exception.ObjectNotFoundException;

@Service
public class UserService {
    
    //Atributos da classe
    @Autowired
    private UserRepository userRepository;

    public User findById(Long id) {
        Optional<User> user = this.userRepository.findById(id);

        //Caso encontre o ID então retorna o objeto de usuário, caso contrário retorna a RuntimeException com mensagem.
        return user.orElseThrow(() -> new ObjectNotFoundException(
            "Usuário não encontrado! " + id + ", Tipo: " + User.class.getName()
        ));
    }

    //Utilizar o Transactional para ter controle melhor da integridade em updates ou insert, irá salvar tudo ou nada, nunca pela metade.
    @Transactional
    public User create(User obj) {
        obj.setId(null); //Define nulo, Proteção para não permitir receber um id no create para que usuários mal intencionados não possam atualizar um registro na função de create ao passar um id.
        obj = this.userRepository.save(obj);
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
            throw new DataBindingViolationException("Não é possível excluir pois há entidades relacionadas!");
        }
    }

}
