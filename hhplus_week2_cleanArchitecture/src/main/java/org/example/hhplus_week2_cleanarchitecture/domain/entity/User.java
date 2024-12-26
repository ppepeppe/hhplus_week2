package org.example.hhplus_week2_cleanarchitecture.domain.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "User")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 사용자 ID

    @Column(nullable = false, length = 50)
    private String name; // 사용자 이름

    @Column(nullable = false, unique = true, length = 100)
    private String email; // 사용자 이메일
}
