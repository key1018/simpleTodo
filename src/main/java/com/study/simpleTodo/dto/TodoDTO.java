package com.study.simpleTodo.dto;

import com.study.simpleTodo.model.TodoEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TodoDTO {

    private String id;
    private String title;
    private boolean done;

    public void todoDTO(final TodoEntity entity){
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.done = entity.isDone();
    }
}
