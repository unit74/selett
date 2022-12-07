package com.selett.server.api.profile.dto.update;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class UpdateLanguageSkillRequest {
    @NotNull(message = "갱신할 번호가 null 이어서는 안됩니다.")
    private Integer id;
    @Size(max = 200, message = "제목은 200자까지 가능합니다.")
    private String title;
    @Size(max = 20, message = "등급은 20자까지 가능합니다.")
    private String grade;
}
