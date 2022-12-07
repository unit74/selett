package com.selett.server.api.profile.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class License {
    private Integer id;
    private String title;
    private LocalDate date;
    private String description;
}
