package org.example.hhplus_week2_cleanarchitecture.interfaces.api.controller;

import lombok.RequiredArgsConstructor;
import org.example.hhplus_week2_cleanarchitecture.application.service.LectureService;
import org.example.hhplus_week2_cleanarchitecture.domain.entity.Lecture;
import org.example.hhplus_week2_cleanarchitecture.interfaces.api.dto.ApplyLectureRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/lecture")
public class LectureController {
    private final LectureService lectureService;

    // 특강 신청 가능 목록 API
    @GetMapping("/available")
    public List<Lecture> getAvailableLectures(@RequestParam LocalDate date) {

        return lectureService.getAvailableLectures(date);
    }

    // 특강 신청 API
    @PostMapping("/apply")
    public ResponseEntity<?> applyLecture(@RequestBody ApplyLectureRequest applyLectureRequest) {
        lectureService.applyLecture(applyLectureRequest.getLectureId(), applyLectureRequest.getUserId());

        return ResponseEntity.ok("수강 신청 성공");
    }

    // 특강 신청 완료 목록 조회 API
    @GetMapping("/{userId}/applied")
    public List<Lecture> getAppliedLectures(@PathVariable Long userId) {

        return lectureService.getAppliedLectures(userId);
    }
}
