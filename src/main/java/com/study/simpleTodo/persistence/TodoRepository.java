package com.study.simpleTodo.persistence;

import com.study.simpleTodo.model.TodoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TodoRepository extends JpaRepository<TodoEntity, String> {

    // ?1는 메서드의 매개변수의 순서 위치
//    @Query("SELECT * FROM TodoEntity T WHERE T.USERID = ?1")
    List<TodoEntity> findByUserId(String userId);
}
