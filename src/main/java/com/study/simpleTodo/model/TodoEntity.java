package com.study.simpleTodo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

@Builder
@NoArgsConstructor // 매개변수가 없는 생성자 구현
@AllArgsConstructor // 모든 멤버변수를 매개변수로 받는 생성자 구현
@Data // Getter/Setter 메서드 구현
@Entity
@Table(name = "Todo")
public class TodoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String userId;
    private String title;
    private boolean done;


}
