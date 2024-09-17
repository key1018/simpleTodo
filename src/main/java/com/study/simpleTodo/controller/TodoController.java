package com.study.simpleTodo.controller;

import com.study.simpleTodo.dto.ResponseDTO;
import com.study.simpleTodo.dto.TodoDTO;
import com.study.simpleTodo.model.TodoEntity;
import com.study.simpleTodo.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/todo")
public class TodoController {

    @Autowired
    private TodoService todoService;

//    @GetMapping
//    public String todoController(){
//        return "todo";
//    }

    @GetMapping("/todoGetMapping")
    public String todoGetMapping(){
        return "todoGetMapping";
    }

    @GetMapping("/{id}")
    public String todoControllerWithPathVariables(@PathVariable(required = false) int id){
        return "todo ID : " + id;
    }

    @GetMapping("/todoRequestParam")
    public String todoRequestParam(@RequestParam(required = false) int id){
        return "testRequestParam ID : " + id;
    }

    @GetMapping("/todoResponseBody")
    public ResponseDTO todoResponseBody(){
        List<String> list = new ArrayList<>();
        list.add("Hi!");
        ResponseDTO<String> responseDTO = ResponseDTO.<String>builder().data(list).build();
        return responseDTO;
    }

    @GetMapping("/todoResponseEntity")
    public ResponseEntity<?> todoResponseEntity() {
        List<String> list = new ArrayList<>();
        list.add("Hi! I'm todoResponseEntity");
        ResponseDTO<String> responseDTO = ResponseDTO.<String>builder().data(list).build();
//        return  ResponseEntity.badRequest().body(responseDTO);
        return  ResponseEntity.ok(responseDTO);
    }

    // create
    @PostMapping
    public ResponseEntity<?> createTodo(@RequestBody TodoDTO dto){
        try {
            String temporaryUserId = "temporaryUserId";

            // 1. 유저에게 받은 TodoDTO를 TodoEntity로 변환하여 Service에게 넘겨주기
            TodoEntity entity = TodoDTO.todoEntity(dto);

            // 2. id를 null로 초기화. 생성 당시에는 id가 없어야함
            entity.setId(null);

            // 3. 임시 유저 아이디 설정
            entity.setUserId(temporaryUserId);

            // 4. Service를 이용해 TodoEntity 생성
            List<TodoEntity> entities = todoService.create(entity);

            // 5. 자바 스트림을 이용해 리턴된 엔티티 리스트를 TodoDTO 리스트로 변환
            List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());

            // 6. 변환된 TodoDTO 리스트를 이용해 ResponseDTO를 초기화
            ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();

            // 7. ResponseDTO를 리턴
            return ResponseEntity.ok(response);
        }catch (Exception e){
            // 8. 예외가 발생하면 dto대신 error 메세지 리턴
            String error = e.getMessage();
            ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().error(error).build();
            return ResponseEntity.badRequest().body(response);
        }
    }

    // retrieve
    @GetMapping
    public ResponseEntity<?> retrieveTodoList(){
        String temporaryUserId = "temporaryUserId";

        // Service의 retrieve 메서드를 이용해 Todo 리스트 가져오기
        List<TodoEntity> retrieve = todoService.retrieve(temporaryUserId);

        // 자바 스트림을 이용해 리턴된 엔티티 리스트를 TodoDTO 리스트로 변환
        List<TodoDTO> dtos = retrieve.stream().map(TodoDTO::new).toList();

        // 변환된 TodoDTO 리스트를 이용해 ResponseDTO를 초기화
        ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();

        // ResponseDTO를 리턴
        return ResponseEntity.ok(response);
    }

}
