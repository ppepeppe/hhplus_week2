package org.example.hhplus_week2_cleanarchitecture.interfaces.api.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApplyLectureRequest {
    private long lectureId;
    private long userId;
}
