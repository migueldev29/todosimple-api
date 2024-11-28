package com.migueldev.todosimple.controllers;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.migueldev.todosimple.models.Task;
import com.migueldev.todosimple.models.projection.TaskProjection;
import com.migueldev.todosimple.services.TaskService;


@RestController
@RequestMapping("/task")
@Validated
public class TaskController {
    
    @Autowired
    private TaskService taskService;

    @GetMapping("/{id}")
    public ResponseEntity<Task> findById(@PathVariable Long id) {
        Task obj = this.taskService.findById(id);
        return ResponseEntity.ok().body(obj);
    }

    @GetMapping("/user")
    public ResponseEntity<List<TaskProjection>> findAllByUser() {
        List<TaskProjection> objs = this.taskService.findAllByUser();
        return ResponseEntity.ok().body(objs);
    }

    @GetMapping("/findall")
    public ResponseEntity<List<TaskProjection>> findAllTasks() {
        List<TaskProjection> objs = this.taskService.findAllTasks();
        return ResponseEntity.ok().body(objs);
    }

    @PostMapping
    public ResponseEntity<Void> create(@Valid @RequestBody Task obj){
        this.taskService.create(obj);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}").buildAndExpand(obj.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> update(@PathVariable Long id, @Valid @RequestBody Task obj){
        obj.setId(id);
        obj = taskService.update(id, obj);
        return ResponseEntity.ok().body(obj);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        this.taskService.delete(id);
        return ResponseEntity.noContent().build();
    }

    
}
