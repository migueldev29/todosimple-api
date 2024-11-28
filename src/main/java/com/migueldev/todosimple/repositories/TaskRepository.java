package com.migueldev.todosimple.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.migueldev.todosimple.models.Task;
import com.migueldev.todosimple.models.projection.TaskProjection;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    List<TaskProjection> findByUser_Id(Long id);

    @Query(value = "SELECT * FROM task", nativeQuery = true)
	List<TaskProjection> findAllTasks();

    //Abaixo é JPQL(utiliza os models criados)
    //@Query(value = "SELECT t FROM Task t WHERE t.user.id = :id")
    //List<Task> findByUser_Id(@Param("id") Long id);

    //QUERY NATIVA SQL
    //@Query(value = "SELECT * FROM task t WHERE t.user_id = :id", nativeQuery = true)
    //List<Task> findByUser_Id(@Param("id") Long id);

}
