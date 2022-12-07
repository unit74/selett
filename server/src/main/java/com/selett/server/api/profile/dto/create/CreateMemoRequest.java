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
public class CreateMemoRequest {
    @Size(max = 5000, message = "내용은 5000자까지 가능합니다.")
    private String description;
    @NotNull(message = "유저 아이디가 null 이어서는 안됩니다.")
    @JsonProperty("user_id")
    private Integer userId;
}
