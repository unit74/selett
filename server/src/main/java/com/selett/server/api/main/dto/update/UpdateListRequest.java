package com.selett.server.api.main.dto.update;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateListRequest {
    @NotNull(message = "자기소개서 번호가 null 이어서는 안됩니다.")
    @JsonProperty("list_id")
    private Integer listId;

    @Size(max = 200, message = "제목은 200자까지 가능합니다.")
    private String title;
}