package org.example.hhplus_week2_cleanarchitecture.domain.repository;

import jakarta.persistence.LockModeType;
import org.example.hhplus_week2_cleanarchitecture.domain.entity.Lecture;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface LectureRepository {
    Optional<Lecture> findById(Long lectureId);
    List<Lecture> findAllById(List<Long> lectureIds);
    List<Lecture> findAllByDate(LocalDate date);
    Optional<Lecture> findByIdWithLock(@Param("lectureId") Long lectureId);
    Lecture save(Lecture lecture);
}

