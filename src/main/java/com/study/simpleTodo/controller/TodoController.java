package com.study.simpleTodo.controller;

import com.study.simpleTodo.dto.ResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/todo")
public class TodoController {
    @GetMapping
    public String todoController(){
        return "todo";
    }

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

}
