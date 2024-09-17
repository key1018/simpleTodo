package com.study.simpleTodo.service;

import ch.qos.logback.classic.Logger;
import com.study.simpleTodo.model.TodoEntity;
import com.study.simpleTodo.persistence.TodoRepository;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    public List<TodoEntity> retrieve(final String userId){
        return todoRepository.findByUserId(userId);
    }

    public List<TodoEntity> update(final TodoEntity entity){
        validate(entity);

        final Optional<TodoEntity> original = todoRepository.findById(entity.getId());

        original.ifPresent(todo -> {
            // 반환된 TodoEntity가 존재하면 값을 새 entity의 값으로 덮어 씌운다
            todo.setTitle(entity.getTitle());
            todo.setDone(entity.isDone());

            // 데이터베이스에 새 값을 저장
            todoRepository.save(todo);
        });

        // retrieve에서 만든 메서드를 이용해 유저의 모든 todo리스트 리턴
        return retrieve(entity.getUserId());
    }

    public List<TodoEntity> delete(final TodoEntity entity){
        validate(entity);

        try{
            // 엔티티 삭제
            todoRepository.delete(entity);
        } catch(Exception e){
            log.error("error deleting entity", entity.getId(), e);

            throw new RuntimeException("error deleting entity " + entity.getId());
        }
        // 새로운 todoList를 가져와 리턴
        return retrieve(entity.getUserId());
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