package org.example.hhplus_week2_cleanarchitecture.infrastructure;

import jakarta.persistence.LockModeType;
import org.example.hhplus_week2_cleanarchitecture.domain.entity.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface LectureJPARepository extends JpaRepository<Lecture, Long> {
    List<Lecture> findAllByDate(LocalDate date);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT l FROM Lecture l WHERE l.lectureId = :lectureId")
    Optional<Lecture> findByIdWithLock(@Param("lectureId") Long lectureId);
}
