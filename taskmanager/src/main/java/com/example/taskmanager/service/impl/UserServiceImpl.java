package com.example.taskmanager.service.impl;

import com.example.taskmanager.dto.ApiResponse;
import com.example.taskmanager.dto.UserDTO;
import com.example.taskmanager.exceptions.NotFoundException;
import com.example.taskmanager.model.User;
import com.example.taskmanager.repository.UserRepository;
import com.example.taskmanager.service.UsersService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UsersService {


    private final UserRepository userRepository;

    @Override
    public ApiResponse<List<UserDTO>> getAllUsers() {

        List<UserDTO> userDTOS = userRepository.findAll().stream()
                .map(this::mapUserToUserDTO)
                .toList();


        return new ApiResponse<>(200, "Users retrieved successfully", userDTOS);
    }

    @Override
    public ApiResponse<UserDTO> getMyProfile(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(()-> new NotFoundException("User not found"));

        return new ApiResponse<>(200, "profile retrieved", mapUserToUserDTO(user));
    }


    @Override
    public ApiResponse<UserDTO> updateUserProfile(String email, UserDTO userDTO) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(()-> new NotFoundException("User not found"));

        if(userDTO.getEmail() != null) user.setEmail(userDTO.getEmail());

        User updatedUser = userRepository.save(user);
        UserDTO updatedUserDto = mapUserToUserDTO(updatedUser);

        return new ApiResponse<>(200, "profile updated successfully", updatedUserDto);


    }

    private UserDTO mapUserToUserDTO(User user){
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setEmail(user.getEmail());
        userDTO.setRole(user.getRole());
        return userDTO;
    }
}
