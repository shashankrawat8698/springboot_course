package com.example.taskmanager.controller;

import com.example.taskmanager.dto.ApiResponse;
import com.example.taskmanager.dto.RegLoginRequest;
import com.example.taskmanager.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<?>> register(
            @Valid @RequestBody RegLoginRequest regRequest
    ) {
        return ResponseEntity.ok(authService.register(regRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<?>> login(
            @Valid @RequestBody RegLoginRequest regRequest,
            HttpServletRequest request
    ) {
        return ResponseEntity.ok(authService.login(regRequest, request));
    }

}
