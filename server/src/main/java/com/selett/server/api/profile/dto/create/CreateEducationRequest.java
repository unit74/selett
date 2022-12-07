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
public class CreateEducationRequest {
    @NotNull(message = "학교명이 null 이어서는 안됩니다.")
    @Size(min = 1, max = 50, message = "학교명은 50자까지 가능합니다.")
    private String name;
    @NotNull(message = "전공은 null 이어서는 안됩니다.")
    @Size(min = 1, max = 50, message = "전공은 50자까지 가능합니다.")
    private String major;
    @NotNull(message = "학위가 null 이어서는 안됩니다.")
    @Size(min = 1, max = 20, message = "학위는 20자까지 가능합니다.")
    private String degree;
    @NotNull(message = "입학일이 null 이어서는 안됩니다.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonProperty("admission_date")
    private LocalDate admissionDate;
    @NotNull(message = "졸업일이 null 이어서는 안됩니다.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonProperty("graduation_date")
    private LocalDate graduationDate;
    @JsonProperty("major_grade")
    private Float majorGrade;
    @JsonProperty("major_course")
    private Integer majorCourse;
    private Float grade;
    @JsonProperty("max_grade")
    private Float maxGrade;
    private Integer course;
    @NotNull(message = "유저 아이디가 null 이어서는 안됩니다.")
    @JsonProperty("user_id")
    private Integer userId;
}
