package org.example.hhplus_week2_cleanarchitecture.infrastructure;

import org.example.hhplus_week2_cleanarchitecture.domain.entity.LectureApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LectureApplicationJPARepository extends JpaRepository<LectureApplication, Long> {
    @Query("SELECT la.lectureId FROM LectureApplication la WHERE la.userId = :userId")
    List<Long> findLectureIdsByUserId(@Param("userId") long userId);

    boolean existsByLectureIdAndUserId(long lectureId, long userId);
}
