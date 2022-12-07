package com.selett.server.api.register.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangePasswordRequest {
    @NotNull(message = "유저 아이디가 null 이어서는 안됩니다.")
    @JsonProperty("user_id")
    private Integer userId;

    @NotNull(message = "현재 비밀번호가 null 이어서는 안됩니다.")
    @Size(min = 1, max = 20, message = "현재 비밀번호는 20자까지 가능합니다.")
    @JsonProperty("current_password")
    private String currentPassword;

    @NotNull(message = "새 비밀번호가 null 이어서는 안됩니다.")
    @Size(min = 1, max = 20, message = "새 비밀번호는 20자까지 가능합니다.")
    @JsonProperty("new_password")
    private String newPassword;
}
