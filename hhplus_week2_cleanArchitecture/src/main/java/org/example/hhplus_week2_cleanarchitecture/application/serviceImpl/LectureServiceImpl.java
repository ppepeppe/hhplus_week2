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
    @Override
    @Transactional
    public LectureApplication applyLecture(long lectureId, long userId) {
        boolean alreadyRegistered = lectureApplicationRepository.existsByLectureIdAndUserId(lectureId, userId);
        if (alreadyRegistered) {
            throw new IllegalStateException("사용자는 이미 해당 특강을 신청했습니다.");
        }
        Lecture lecture = lectureRepository.findByIdWithLock(lectureId)
                .orElseThrow(() -> new IllegalStateException("강의가 없습니다."));

        // 정원이 초과되었는지 확인
        if (lecture.getCurrentCount() >= lecture.getMaxCapacity()) {
            throw new IllegalStateException("30명 정원이 꽉 찼습니다.");
        }

        // 참가자 수 증가
        lecture.setCurrentCount(lecture.getCurrentCount() + 1);

        // 강의 정보 저장
        lectureRepository.save(lecture);

        // 신청 기록 저장
        LectureApplication lectureApplication = new LectureApplication();
        lectureApplication.setLectureId(lectureId);
        lectureApplication.setUserId(userId);
        lectureApplication.setAppliedAt(DATE_TIME);

        return lectureApplicationRepository.save(lectureApplication);
    }
    @Override
    public List<Lecture> getAppliedLectures(long userId) {
        List<Long> lectureIds = lectureApplicationRepository.findLectureIdsByUserId(userId);

        return lectureRepository.findAllById(lectureIds);
    }
}
