package com.study.simpleTodo.service;

import ch.qos.logback.classic.Logger;
import com.study.simpleTodo.model.TodoEntity;
import com.study.simpleTodo.persistence.TodoRepository;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class TodoService {

    @Autowired
    private TodoRepository todoRepository;

    public String testService() {
        // TodoEntity 생성
        TodoEntity entity = TodoEntity.builder().title("my first todo item").build();
        // TodoEntity 저장
        todoRepository.save(entity);
        // TodoEntity 검색
        TodoEntity saveEntity = todoRepository.findById(entity.getId()).get();
        return saveEntity.getTitle();
    }

    public List<TodoEntity> create(final TodoEntity todoEntity){
        // 검증
        // 넘어온 엔티티가 유효한지 검증
        validate(todoEntity);

        todoRepository.save(todoEntity);

        log.info("Entity Id : {} is saved", todoEntity.getId());

        return todoRepository.findByUserId(todoEntity.getUserId());
    }

    // 리펙토링한 메서드
    private void validate(final TodoEntity todoEntity){
        if(todoEntity == null){
            log.warn("Entity is null");
            throw new RuntimeException("Entity is null");
        }

        if(todoEntity.getUserId() == null){
            log.warn("Unknown user");
            throw new RuntimeException("Unknown user");
        }
    }
}