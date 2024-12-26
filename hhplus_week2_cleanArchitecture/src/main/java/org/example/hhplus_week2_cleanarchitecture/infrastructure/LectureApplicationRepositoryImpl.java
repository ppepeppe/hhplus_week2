package org.example.hhplus_week2_cleanarchitecture.infrastructure;

import lombok.RequiredArgsConstructor;
import org.example.hhplus_week2_cleanarchitecture.domain.entity.LectureApplication;
import org.example.hhplus_week2_cleanarchitecture.domain.repository.LectureApplicationRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class LectureApplicationRepositoryImpl implements LectureApplicationRepository {
    private final LectureApplicationJPARepository lectureApplicationJPARepository;
    @Override
    public List<Long> findLectureIdsByUserId(long userId) {

        return lectureApplicationJPARepository.findLectureIdsByUserId(userId);
    }

    @Override
    public boolean existsByLectureIdAndUserId(long lectureId, long userId) {

        return lectureApplicationJPARepository.existsByLectureIdAndUserId(lectureId, userId);
    }

    @Override
    public LectureApplication save(LectureApplication lectureApplication) {
        return lectureApplicationJPARepository.save(lectureApplication);
    }
}
