package com.chipping.gateway.controller;

import com.chipping.gateway.dto.LoginRequest;
import com.chipping.gateway.dto.LoginResponse;
import com.chipping.gateway.security.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {

        if ("adminChipping".equals(request.getUsername()) && "1313".equals(request.getPassword())) {
            String token = JwtUtil.generateToken(request.getUsername(), "ADMIN");
            return new LoginResponse(token);
        }

        if ("clienteChipping".equals(request.getUsername()) && "1234".equals(request.getPassword())) {
            String token = JwtUtil.generateToken(request.getUsername(), "CLIENTE");
            return new LoginResponse(token);
        }

        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciales inválidas en Chipping");
    }
}