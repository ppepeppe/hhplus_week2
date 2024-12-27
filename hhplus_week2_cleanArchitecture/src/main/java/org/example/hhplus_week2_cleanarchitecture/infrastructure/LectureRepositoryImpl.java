package org.example.hhplus_week2_cleanarchitecture.infrastructure;

import lombok.RequiredArgsConstructor;
import org.example.hhplus_week2_cleanarchitecture.domain.entity.Lecture;
import org.example.hhplus_week2_cleanarchitecture.domain.repository.LectureRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class LectureRepositoryImpl implements LectureRepository {
    private final LectureJPARepository lectureJPARepository;

    @Override
    public Optional<Lecture> findById(Long lectureId) {
        return lectureJPARepository.findById(lectureId);
    }

    @Override
    public List<Lecture> findAllById(List<Long> lectureIds) {
        return lectureJPARepository.findAllById(lectureIds);
    }

    @Override
    public List<Lecture> findAllByDate(LocalDate date) {
        return lectureJPARepository.findAllByDate(date);
    }

    @Override
    public Optional<Lecture> findByIdWithLock(Long lectureId) {
        return lectureJPARepository.findByIdWithLock(lectureId);
    }

    @Override
    public Lecture save(Lecture lecture) {
        return lectureJPARepository.save(lecture);
    }
}
