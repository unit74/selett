package com.selett.server.api.profile.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class Award {
    private Integer id;
    private String title;
    private LocalDate date;
    private String organization;
    private String grade;
    private String description;
}
