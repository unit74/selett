package com.selett.server.api.profile.dto.update;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class UpdateAwardRequest {
    @NotNull(message = "갱신할 번호가 null 이어서는 안됩니다.")
    private Integer id;
    @Size(max = 200, message = "제목은 200자까지 가능합니다.")
    private String title;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
    @Size(max = 50, message = "시상 기관은 50자까지 가능합니다.")
    private String organization;
    @Size(max = 20, message = "등급은 20자까지 가능합니다.")
    private String grade;
    @Size(max = 5000, message = "내용은 5000자까지 가능합니다.")
    private String description;
}
