package com.selett.server.api.register.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    @NotNull(message = "가입할 아이디가 null 이어서는 안됩니다.")
    @Size(min = 1, max = 20, message = "가입할 아이디는 20자까지 가능합니다.")
    private String identification;

    @NotNull(message = "가입할 비밀번호가 null 이어서는 안됩니다.")
    @Size(min = 1, max = 20, message = "가입할 비밀번호는ㅎ 20자까지 가능합니다.")
    private String password;

    @NotNull(message = "가입할 이름이 null 이어서는 안됩니다.")
    @Size(min = 1, max = 20, message = "가입할 이름은 20자까지 가능합니다.")
    private String name;

    @NotNull(message = "가입할 이메일이 null 이어서는 안됩니다.")
    @Email(message = "가입할 이메일의 형식이 맞지 않습니다.")
    @Size(min = 1, max = 50, message = "가입할 이메일은 50자까지 가능합니다.")
    private String email;
}
