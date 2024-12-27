package org.example.hhplus_week2_cleanarchitecture.application.service;

import org.example.hhplus_week2_cleanarchitecture.domain.entity.Lecture;
import org.example.hhplus_week2_cleanarchitecture.domain.entity.LectureApplication;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public interface LectureService {
    List<Lecture> getAvailableLectures(LocalDate date);
    LectureApplication applyLecture(long lectureId, long userId);
    List<Lecture> getAppliedLectures(long userId);

}
