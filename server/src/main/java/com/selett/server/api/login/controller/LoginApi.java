package com.selett.server.api.login.controller;

import com.selett.server.api.login.dto.LoginRequest;
import com.selett.server.api.login.dto.LoginResponse;
import com.selett.server.api.login.service.LoginService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@AllArgsConstructor
@RestController
@RequestMapping("/login")
public class LoginApi {
    private final LoginService loginService;

    @PostMapping("")
    @Operation(summary = "로그인", description = "로그인합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK",
                            content = @Content(schema = @Schema(implementation = LoginResponse.class)))
            }
    )
    @io.swagger.annotations.ApiResponses(
            @io.swagger.annotations.ApiResponse(
                    response = LoginResponse.class, message = "OK", code = 200)
    )
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequest loginRequest) {
        LoginResponse loginResponse;

        try {
            loginResponse = loginService.login(loginRequest.getIdentification(), loginRequest.getPassword());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

        return ResponseEntity.status(HttpStatus.OK).body(loginResponse);
    }
}
