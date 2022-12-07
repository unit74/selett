package com.selett.server.jpa.mapper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "educations")
public class EducationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @Size(min = 1, max = 50)
    private String name;

    @NotNull
    @Size(min = 1, max = 50)
    private String major;

    @NotNull
    @Size(min = 1, max = 20)
    private String degree;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate admissionDate;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate graduationDate;

    private Float majorGrade;

    private Integer majorCourse;

    private Float Grade;

    private Float maxGrade;

    private Integer course;

    @NotNull
    private Integer userId;
}
