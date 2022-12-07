package com.selett.server.api.register.controller;

import com.selett.server.api.register.dto.ChangePasswordRequest;
import com.selett.server.api.register.dto.RegisterRequest;
import com.selett.server.api.register.service.RegisterService;
import com.selett.server.utils.RequestTokenValidation;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@AllArgsConstructor
@RestController
@RequestMapping("/register")
public class RegisterApi {
    private final RegisterService registerService;
    private final RequestTokenValidation requestTokenValidation;

    @PostMapping("")
    @Operation(summary = "회원가입", description = "회원가입합니다.")
    public ResponseEntity<?> register(@RequestBody @Valid RegisterRequest registerRequest) {
        try {
            registerService.register(registerRequest.getIdentification(),
                    registerRequest.getPassword(), registerRequest.getName(),
                    registerRequest.getEmail());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PutMapping("/password")
    @Operation(summary = "비밀번호 변경", description = "비밀번호를 변경합니다.")
    public ResponseEntity<?> changePassword(@RequestBody @Valid ChangePasswordRequest changePasswordRequest) {
        try {
            requestTokenValidation.verify(changePasswordRequest.getUserId());

            registerService.changePassword(
                    changePasswordRequest.getUserId(),
                    changePasswordRequest.getCurrentPassword(),
                    changePasswordRequest.getNewPassword()
            );
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
