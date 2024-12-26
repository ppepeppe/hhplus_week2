package org.example.hhplus_week2_cleanarchitecture.test;

import org.example.hhplus_week2_cleanarchitecture.application.serviceImpl.LectureServiceImpl;
import org.example.hhplus_week2_cleanarchitecture.domain.entity.Lecture;
import org.example.hhplus_week2_cleanarchitecture.domain.entity.LectureApplication;
import org.example.hhplus_week2_cleanarchitecture.domain.repository.LectureApplicationRepository;
import org.example.hhplus_week2_cleanarchitecture.domain.repository.LectureRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LectureServiceTest {
    private static final long USER_ID = 1L;
    private static final long LECTURE_ID = 1L;
    private static final LocalDateTime DATE_TIME = LocalDateTime.of(2024, 12, 31, 0, 0, 0);

    @Mock
    private LectureRepository lectureRepository;
    @Mock
    private LectureApplicationRepository lectureApplicationRepository;
    @InjectMocks
    private LectureServiceImpl lectureServiceImpl;

    @Test
    @DisplayName("날짜 별로 특강 목록을 조회한다.")
    void shouldRetrieveLecturesByDateTime() {
        // given
        LocalDate dateTime = LocalDate.of(2025, 1, 1);

        List<Lecture> lectures = List.of(
                new Lecture(1L, "math", "kim", LocalDate.of(2025, 1, 1), 0, 30),
                new Lecture(1L, "eng", "park", LocalDate.of(2025, 1, 1), 0, 30)
        );

        when(lectureRepository.findAllByDate(dateTime)).thenReturn(lectures);

        // when
        lectureServiceImpl.getAvailableLectures(dateTime);
        // then
        assertEquals(2, lectures.size()); // 리스트 크기 확인
        assertEquals("math", lectures.get(0).getTitle()); // 첫 번째 강의 제목 확인
        assertEquals("eng", lectures.get(1).getTitle()); // 두 번째 강의 제목 확인
    }
    @Test
    @DisplayName("현재 수강인원이 30명 미만인 신청 가능한 특강만 조회된다.")
    void shouldRetrieveOnlyAvailableLectures() {
        // given
        LocalDate dateTime = LocalDate.of(2025, 1, 1);

        List<Lecture> lectures = List.of(
                new Lecture(1L, "math", "kim", LocalDate.of(2025, 1, 1), 0, 30),
                new Lecture(1L, "eng", "park", LocalDate.of(2025, 1, 1), 30, 30)
        );

        // Mock 설정
        when(lectureRepository.findAllByDate(dateTime)).thenReturn(lectures);

        // when
        List<Lecture> availableLectures = lectureServiceImpl.getAvailableLectures(dateTime);

        // then
        assertEquals(1, availableLectures.size()); // 신청 가능한 강의는 1개
        assertEquals("math", availableLectures.get(0).getTitle());
    }

    @Test
    @DisplayName("우저가 특강Id로 해당 특강을 신청한다")
    void shouldCreateLectureSuccessfully() {
       // given
       LectureApplication lectureApplication = new LectureApplication(1L, USER_ID, LECTURE_ID, DATE_TIME);
       Lecture lecture = new Lecture(LECTURE_ID, "math", "kim", LocalDate.of(2025, 1, 1), 0, 30);
       when(lectureApplicationRepository.save(lectureApplication)).thenReturn(lectureApplication);
       when(lectureRepository.findByIdWithLock(LECTURE_ID)).thenReturn(Optional.of(lecture));
       // when
       LectureApplication result = lectureServiceImpl.applyLecture(LECTURE_ID, USER_ID);

       assertEquals(LECTURE_ID, result.getLectureId());
       assertEquals(USER_ID, result.getUserId());
    }

    @Test
    @DisplayName("특강ID로 해당 특강을 신청 시 currentCount 값을 +1 한다")
    void shouldIncrementCurrentParticipantsWhenLectureIsRegistered() {
       // given
       Lecture lecture = new Lecture(LECTURE_ID, "math", "kim", LocalDate.of(2025, 1, 1), 0, 30);
       LectureApplication lectureApplication = new LectureApplication(1L, USER_ID, LECTURE_ID, DATE_TIME);

       // Mock 설정
       when(lectureRepository.findByIdWithLock(LECTURE_ID)).thenReturn(Optional.of(lecture));
       when(lectureApplicationRepository.save(any(LectureApplication.class))).thenReturn(lectureApplication);
       when(lectureRepository.save(any(Lecture.class))).thenReturn(lecture);

       // when
       LectureApplication result = lectureServiceImpl.applyLecture(LECTURE_ID, USER_ID);

       // then
       assertEquals(1, lecture.getCurrentCount()); // currentCount가 1 증가했는지 확인
       assertEquals(LECTURE_ID, result.getLectureId()); // 신청 기록이 저장되었는지 확인
       verify(lectureRepository).save(lecture); // Lecture 저장 로직 호출 확인
    }

    @Test
    @DisplayName("참가자가 30명을 초과하면 예외를 던진다")
    void shouldThrowExceptionWhenLectureIsFull() {
      // given
       Lecture lecture = new Lecture(LECTURE_ID, "math", "kim", LocalDate.of(2025, 1, 1), 30, 30);

       when(lectureRepository.findByIdWithLock(LECTURE_ID)).thenReturn(Optional.of(lecture));

      // when & then
      IllegalStateException exception = assertThrows(IllegalStateException.class, () ->
              lectureServiceImpl.applyLecture(LECTURE_ID, USER_ID));

      assertEquals("30명 정원이 꽉 찼습니다.", exception.getMessage());
    }

    @Test
    @DisplayName("특강 신청 완료 목록을 초회한다.")
    void shouldRetrieveLectureListByUserId() {
        // given
        List<Long> lectureIds = List.of(1L, 2L);

        List<Lecture> lectures = List.of(
                        new Lecture(1L, "math", "kim",  LocalDate.of(2025, 1, 1), 10, 30),
                        new Lecture(2L, "eng", "park",  LocalDate.of(2025, 1, 1), 20, 30)
        );


        when(lectureApplicationRepository.findLectureIdsByUserId(USER_ID)).thenReturn(lectureIds);
        when(lectureRepository.findAllById(lectureIds)).thenReturn(lectures);

        // when
        List<Lecture> completedLectures = lectureServiceImpl.getAppliedLectures(USER_ID);

        assertEquals(2, completedLectures.size());
        assertEquals("math", completedLectures.get(0).getTitle());
        assertEquals("eng", completedLectures.get(1).getTitle());

    }
}
