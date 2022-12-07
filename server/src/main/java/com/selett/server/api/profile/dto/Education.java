package com.selett.server.api.profile.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class Education {
    private Integer id;
    private String name;
    private String major;
    private String degree;
    @JsonProperty("admission_date")
    private LocalDate admissionDate;
    @JsonProperty("graduation_date")
    private LocalDate graduationDate;
    @JsonProperty("major_grade")
    private Float majorGrade;
    @JsonProperty("major_course")
    private Integer majorCourse;
    private Float Grade;
    @JsonProperty("max_course")
    private Float maxGrade;
    private Integer course;
}
