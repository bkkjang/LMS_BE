package com.aivle.bookapp.controller;

import com.aivle.bookapp.dto.*;
import com.aivle.bookapp.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/send-code")
    public Map<String, String> sendCode(@Valid @RequestBody SendCodeRequest req) {
        return Map.of("message", authService.sendCode(req));
    }

    @PostMapping("/check-code")
    public Map<String, String> checkCode(@Valid @RequestBody CheckCodeRequest req) {
        return Map.of("message", authService.checkCode(req));
    }

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthResponse signup(@Valid @RequestBody SignupRequest req) {
        return authService.signup(req);
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest req) {
        return authService.login(req);
    }
}
