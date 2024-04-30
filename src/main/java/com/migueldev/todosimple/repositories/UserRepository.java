package com.migueldev.todosimple.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.migueldev.todosimple.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
}
