package com.example.taskmanager.service;

import com.example.taskmanager.dto.ApiResponse;
import com.example.taskmanager.dto.RegLoginRequest;
import jakarta.servlet.http.HttpServletRequest;

public interface AuthService {
    ApiResponse<?> register(RegLoginRequest regRequest);
    ApiResponse<?> login(RegLoginRequest loginRequest, HttpServletRequest request);
}
