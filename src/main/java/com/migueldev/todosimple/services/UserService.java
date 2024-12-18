package com.migueldev.todosimple.services;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.migueldev.todosimple.models.User;
import com.migueldev.todosimple.models.dto.UserCreateDTO;
import com.migueldev.todosimple.models.dto.UserUpdateDTO;
import com.migueldev.todosimple.models.enums.ProfileEnum;
import com.migueldev.todosimple.repositories.UserRepository;
import com.migueldev.todosimple.security.UserSpringSecurity;
import com.migueldev.todosimple.services.exception.AuthorizationException;
import com.migueldev.todosimple.services.exception.DataBindingViolationException;
import com.migueldev.todosimple.services.exception.ObjectNotFoundException;

@Service
public class UserService {
    
    //Atributos da classe
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserRepository userRepository;

    public User findById(Long id) {
        UserSpringSecurity userSpringSecurity = authenticated();
        
        if (!Objects.nonNull(userSpringSecurity) 
                || !userSpringSecurity.hasRole(ProfileEnum.ADMIN) && !id.equals(userSpringSecurity.getId())){
            throw new AuthorizationException("Acesso negado!");
        }

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
        obj.setPassword(this.bCryptPasswordEncoder.encode(obj.getPassword()));
        obj.setProfiles(Stream.of(ProfileEnum.USER.getCode()).collect(Collectors.toSet())); //Este metodo setProfiles foi criado pelo Lombok na classe User após ter definido ela como: private Set<Integer> profiles = new HashSet<>();
        obj = this.userRepository.save(obj);
        return obj;
    }

    @Transactional
    public User update(User obj) {
        User newObj = findById(obj.getId());
        newObj.setPassword(obj.getPassword());
        newObj.setPassword(this.bCryptPasswordEncoder.encode(obj.getPassword()));
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

    public static UserSpringSecurity authenticated() {
        try {
            return (UserSpringSecurity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        } catch (Exception e) {
            return null;
        }
    }

    public User fromDTO(@Valid UserCreateDTO obj){
        User user = new User();
        user.setUsername(obj.getUsername());
        user.setPassword(obj.getPassword());
        return user;
    }

    public User fromDTO(@Valid UserUpdateDTO obj){
        User user = new User();
        user.setId(obj.getId());
        user.setPassword(obj.getPassword());
        return user;
    }

}
