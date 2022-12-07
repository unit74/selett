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

public class CreateLicenseRequest {
    @NotNull(message = "제목이 null 이어서는 안됩니다.")
    @Size(min = 1, max = 200, message = "제목은 200자까지 가능합니다.")
    private String title;
    @NotNull(message = "날짜가 null 이어서는 안됩니다.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
    @Size(max = 5000, message = "설명은 5000자까지 가능합니다.")
    private String description;
    @NotNull(message = "유저 아이디가 null 이어서는 안됩니다.")
    @JsonProperty("user_id")
    private Integer userId;
}
