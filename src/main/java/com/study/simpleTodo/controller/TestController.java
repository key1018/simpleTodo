package com.study.simpleTodo.controller;

import com.study.simpleTodo.dto.ResponseDTO;
import com.study.simpleTodo.dto.TestRequestBodyDTO;
import com.study.simpleTodo.service.TestService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController // http 관련 코드 및 요청/응답 매핑을 스프링에서 알아서 해줌
@RequestMapping("test")
public class TestController {

    @Autowired
    TestService testService;

    @GetMapping
    public String testController() {
        return "hello world";
    }

    @GetMapping("/testGetMapping")
    public String testGetMapping() {
        return "안녕";
    }

    @GetMapping("/{id}")
    public String testControllerWithPathVariables(@PathVariable(required = false) int id) {
        // (required = false) : 매개변수가 반드시 필요한 것은 아니라는 의미
        // 즉, id값이 안들어가도 된다
        return "안녕 ID : " + id;
    }

    @GetMapping("/testRequestParam")
    public String testControllerRequestParam(@RequestParam(required = false) int id) {
        return "안녕 ID : " + id;
    }

    @GetMapping("/testRequestBody")
    public String testControllerRequestBody(@RequestBody TestRequestBodyDTO testRequestBodyDTO) {
        return "안녕 ID : " + testRequestBodyDTO.getId() + ", Message : " + testRequestBodyDTO.getMessage();
    }

    @GetMapping("/testResponseBody")
    public ResponseDTO<String> testControllerResponseBody() {
        List<String> list = new ArrayList<>();
        list.add("hello world");
        ResponseDTO<String> responseDTO = ResponseDTO.<String>builder().data(list).build();
        return responseDTO;
    }

    @GetMapping("/testResponserEntity")
    public ResponseEntity<?> testControllerResponseEntity() {
        String str = testService.testService();
        List<String> list = new ArrayList<>();
        list.add(str);
        ResponseDTO<String> responseDTO = ResponseDTO.<String>builder().data(list).build();
        // http status를 400으로 설정
        return ResponseEntity.badRequest().body(responseDTO);
        // http status 200으로 설정
//        return ResponseEntity.ok(responseDTO);
    }
}
