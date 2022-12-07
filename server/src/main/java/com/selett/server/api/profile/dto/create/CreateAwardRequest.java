package com.selett.server.api.profile.dto.create;

import com.fasterxml.jackson.annotation.JsonProperty;
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

public class CreateAwardRequest {
    @NotNull(message = "제목이 null 이어서는 안됩니다.")
    @Size(min = 1, max = 200,message = "제목은 200자까지 가능합니다.")
    private String title;
    @NotNull(message = "날짜가 null 이어서는 안됩니다.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
    @NotNull(message = "시상 기관이 null 이어서는 안됩니다.")
    @Size(min = 1, max = 50, message = "시상 기관은 50자까지 가능합니다.")
    private String organization;
    @NotNull(message = "등급이 null 이어서는 안됩니다.")
    @Size(min = 1, max = 20, message = "등급은 20자까지 가능합니다.")
    private String grade;
    @Size(max = 5000, message = "내용은 200자까지 가능합니다.")
    private String description;
    @NotNull(message = "유저의 번호가 null 이어서는 안됩니다.")
    @JsonProperty("user_id")
    private Integer userId;
}
