package com.example.taskmanager.controller;

import com.example.taskmanager.dto.ApiResponse;
import com.example.taskmanager.dto.UserDTO;
import com.example.taskmanager.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UsersService usersService;

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse<List<UserDTO>>> getAllUsers() {
        return ResponseEntity.ok(usersService.getAllUsers());
    }

    @GetMapping("/profile/me")
    public ResponseEntity<ApiResponse<UserDTO>> getMyProfile(
            Authentication authentication
    ) {
        return ResponseEntity.ok(usersService.getMyProfile(authentication.getName()));

    }

    @PutMapping("/profile/update")
    public ResponseEntity<ApiResponse<UserDTO>> updateMyProfile(
            @RequestBody UserDTO userDTO,
            Authentication authentication
    ) {
        return ResponseEntity.ok(usersService.
                updateUserProfile(authentication.getName(), userDTO));
    }

}