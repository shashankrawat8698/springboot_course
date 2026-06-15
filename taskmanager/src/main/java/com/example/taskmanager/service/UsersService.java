package com.example.taskmanager.service;

import com.example.taskmanager.dto.ApiResponse;
import com.example.taskmanager.dto.UserDTO;

import java.util.List;

public interface UsersService {
    ApiResponse<List<UserDTO>> getAllUsers();
    ApiResponse<UserDTO> getMyProfile(String email);

    ApiResponse<UserDTO> updateUserProfile(String email, UserDTO userDTO);
}
