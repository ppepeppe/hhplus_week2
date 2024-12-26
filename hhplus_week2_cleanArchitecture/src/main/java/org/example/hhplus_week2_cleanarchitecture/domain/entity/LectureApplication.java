package org.example.hhplus_week2_cleanarchitecture.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "LectureApplication", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"userId", "lectureId"}) // 동일 유저가 동일 특강 중복 신청 방지
})
public class LectureApplication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long applicationId; // 신청 ID

    @Column(nullable = false)
    private Long userId; // 신청자 ID

    @Column(nullable = false)
    private Long lectureId; // 신청한 특강 ID

    @Column(nullable = false)
    private LocalDateTime appliedAt = LocalDateTime.now(); // 신청 시간

    @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            LectureApplication that = (LectureApplication) o;
            return Objects.equals(lectureId, that.lectureId) &&
                   Objects.equals(userId, that.userId) &&
                   Objects.equals(appliedAt, that.appliedAt);
        }

        @Override
        public int hashCode() {
            return Objects.hash(lectureId, userId, appliedAt);
        }
}
