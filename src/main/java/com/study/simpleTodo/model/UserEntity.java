package com.study.simpleTodo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data  // Getter/Setter 메서드 구현
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = "username")})
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id; // 유저에게 고유하게 부여되는 id
    @Column(nullable = false)
    private String username; // 아이디로 사용할 유저네임. 이메일 또는 문자열
    private String password; // 패스워드 ( OAuth를 통해 SSO 로그인을 하므로 NULL 값 가능)
    private String role; // 사용자의 역할 ex) 어드민, 일반사용자
    private String authProvider; // 이후 OAuth에서 사용할 유저 정보 제공자 : github
}
