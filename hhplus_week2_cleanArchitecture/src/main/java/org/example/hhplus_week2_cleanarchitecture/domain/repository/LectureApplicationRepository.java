package org.example.hhplus_week2_cleanarchitecture.domain.repository;

import org.example.hhplus_week2_cleanarchitecture.domain.entity.LectureApplication;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LectureApplicationRepository {
    List<Long> findLectureIdsByUserId(long userId);
    boolean existsByLectureIdAndUserId(long lectureId, long userId);
    LectureApplication save(LectureApplication lectureApplication);
}
