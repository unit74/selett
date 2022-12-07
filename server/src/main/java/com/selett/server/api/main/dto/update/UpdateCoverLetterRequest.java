package com.selett.server.api.main.dto.update;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCoverLetterRequest {
    @NotNull(message = "자기소개서 번호가 null 이어서는 안됩니다.")
    private Integer id;

    @Size(max = 200, message = "제목은 200자까지 가능합니다.")
    private String title;

    private String question;

    @JsonProperty("question_lock")
    private Boolean questionLock;

    @Size(max = 5000, message = "내용은 5000자까지 가능합니다.")
    private String description;

    @JsonProperty("description_lock")
    private Boolean descriptionLock;
}
