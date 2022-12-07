package com.selett.server.api.login.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {
    @NotNull(message = "아이디가 null 이어서는 안됩니다.")
    @Size(min = 1, max = 20, message = "아이디 길이가 1 ~ 20 글자여야 됩니다.")
    private String identification;

    @NotNull(message = "비밀번호가 null 이어서는 안됩니다.")
    @Size(min = 1, max = 20, message = "비밀번호 길이가 1 ~ 20 글자여야 됩니다.")
    private String password;
}
