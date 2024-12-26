package org.example.hhplus_week2_cleanarchitecture.application.serviceImpl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.hhplus_week2_cleanarchitecture.application.service.LectureService;
import org.example.hhplus_week2_cleanarchitecture.domain.entity.Lecture;
import org.example.hhplus_week2_cleanarchitecture.domain.entity.LectureApplication;
import org.example.hhplus_week2_cleanarchitecture.domain.repository.LectureApplicationRepository;
import org.example.hhplus_week2_cleanarchitecture.domain.repository.LectureRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LectureServiceImpl implements LectureService {
    private static final LocalDateTime DATE_TIME = LocalDateTime.of(2024, 12, 31, 0, 0, 0);

    private final LectureRepository lectureRepository;
    private final LectureApplicationRepository lectureApplicationRepository;

    @Override
    public List<Lecture> getAvailableLectures(LocalDate date) {

        return lectureRepository.findAllByDate(date)
                    .stream()
                    .filter(lecture -> lecture.getCurrentCount() < 30)
                    .collect(Collectors.toList());
    }

}
