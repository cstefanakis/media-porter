package org.sda.mediaporter.controllers;

import lombok.AllArgsConstructor;
import org.sda.mediaporter.services.AuthService;
import org.sda.mediaporter.dtos.JwtAuthResponseDto;
import org.sda.mediaporter.dtos.LoginDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private AuthService authService;

    // Build Login REST API
    @PostMapping("/login")
    public ResponseEntity<JwtAuthResponseDto> login(@RequestBody LoginDto loginDto){
        String token = authService.login(loginDto);

        JwtAuthResponseDto jwtAuthResponse = new JwtAuthResponseDto();
        jwtAuthResponse.setAccessToken(token);

        return new ResponseEntity<>(jwtAuthResponse, HttpStatus.OK);
    }

}
