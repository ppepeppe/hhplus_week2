package org.example.hhplus_week2_cleanarchitecture.test;

import org.example.hhplus_week2_cleanarchitecture.domain.entity.Lecture;
import org.example.hhplus_week2_cleanarchitecture.domain.repository.LectureApplicationRepository;
import org.example.hhplus_week2_cleanarchitecture.domain.repository.LectureRepository;
import org.example.hhplus_week2_cleanarchitecture.application.serviceImpl.LectureServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class LectureConcurrencyTest {

    @Autowired
    private LectureRepository lectureRepository;

    @Autowired
    private LectureApplicationRepository lectureApplicationRepository;

    @Autowired
    private LectureServiceImpl lectureService;
    @Test
    @DisplayName("동시에 40명이 신청 시 30명만 성공 (비관적 락)")
    void shouldAllowOnly30ParticipantsWhen40ApplySimultaneouslyWithPessimisticLock() throws InterruptedException {
        // given
        LocalDate dateTime = LocalDate.of(2025, 1, 1);

        Lecture lecture = new Lecture();
        lecture.setTitle("math");
        lecture.setLecturer("kim");
        lecture.setCurrentCount(0);
        lecture.setMaxCapacity(30);
        lecture.setDate(dateTime);
        lectureRepository.save(lecture);

        int numberOfThreads = 40;
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch latch = new CountDownLatch(numberOfThreads);

        AtomicInteger successfulRegistrations = new AtomicInteger(0);
        AtomicInteger failedRegistrations = new AtomicInteger(0);

        for (int i = 1; i <= numberOfThreads; i++) {
            final long userId = i;
            executorService.submit(() -> {
                try {
                    lectureService.applyLecture(lecture.getLectureId(), userId);
                    successfulRegistrations.incrementAndGet();
                    System.out.println("User " + userId + " successfully registered.");
                } catch (Exception e) {
                    failedRegistrations.incrementAndGet();
                    System.out.println("User " + userId + " failed to register: " + e.getMessage());
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executorService.shutdown();

        // then
        Lecture updatedLecture = lectureRepository.findById(lecture.getLectureId()).orElseThrow();
        assertEquals(30, updatedLecture.getCurrentCount()); // 성공적으로 신청된 참가자 수
        assertEquals(30, successfulRegistrations.get()); // 성공한 신청
        assertEquals(10, failedRegistrations.get()); // 실패한 신청
    }

}
