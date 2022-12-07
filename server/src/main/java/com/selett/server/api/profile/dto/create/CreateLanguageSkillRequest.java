package com.selett.server.api.profile.dto.create;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class CreateLanguageSkillRequest {
    @NotNull(message = "제목이 null 이어서는 안됩니다.")
    @Size(min = 1, max = 200, message = "제목은 200자까지 가능합니다.")
    private String title;
    @NotNull(message = "등급이 null 이어서는 안됩니다.")
    @Size(min = 1, max = 20, message = "등급은 20자까지 가능합니다.")
    private String grade;
    @NotNull(message = "유저 아이디가 null 이어서는 안됩니다.")
    @JsonProperty("user_id")
    private Integer userId;
}
