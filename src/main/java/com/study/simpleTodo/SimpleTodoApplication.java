package com.study.simpleTodo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication // 해당 클래스가 스프링 부트를 설정하는 클래스임을 의미
public class SimpleTodoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SimpleTodoApplication.class, args);
	}

}
