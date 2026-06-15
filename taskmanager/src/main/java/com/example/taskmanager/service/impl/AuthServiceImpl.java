package com.example.taskmanager.service.impl;

import com.example.taskmanager.dto.ApiResponse;
import com.example.taskmanager.dto.RegLoginRequest;
import com.example.taskmanager.dto.UserDTO;
import com.example.taskmanager.enums.Role;
import com.example.taskmanager.exceptions.BadRequestException;
import com.example.taskmanager.model.User;
import com.example.taskmanager.repository.UserRepository;
import com.example.taskmanager.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {


    private final UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;


    @Override
    public ApiResponse<?> register(RegLoginRequest regRequest) {

        if (userRepository.findByEmail(regRequest.getEmail()).isPresent()) {

            throw new BadRequestException("Email Already Exist");
        }

        User user = new User();
        user.setEmail(regRequest.getEmail());
        user.setPassword(passwordEncoder.encode(regRequest.getPassword()));

        if (regRequest.getRole() == null) {
            user.setRole(Role.USER);
        } else if (regRequest.getRole().equals(Role.ADMIN)) {
            user.setRole(Role.ADMIN);
        } else {
            user.setRole(Role.USER);
        }

        User savedUser = userRepository.save(user);

        UserDTO savedUserDto = new UserDTO();
        savedUserDto.setEmail(savedUser.getEmail());
        savedUserDto.setRole(savedUser.getRole());

        return new ApiResponse<>(201, "User saved successfully", savedUserDto);
    }


    @Override
    public ApiResponse<?> login(RegLoginRequest loginRequest, HttpServletRequest request) {

        // it calls the cunstom-user-detail-sservice and
        // custom-user-details class we created under the hood to validate the user
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        //save the info to security context
        SecurityContextHolder.getContext().setAuthentication(authentication);

        //create a cookie session JSESSIONID for the users.
        //it is going to auto pass the session down when you can access any endpoint via the Set-Cookie
        HttpSession session = request.getSession(true);
        session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());

        //to return the response to the controller
        return new ApiResponse<>(200, "login successfully", null);
    }
}