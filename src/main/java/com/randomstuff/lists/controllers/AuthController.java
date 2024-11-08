package com.randomstuff.lists.controllers;

import com.randomstuff.lists.dtos.AuthRequest;
import com.randomstuff.lists.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;


    @PostMapping("/login")
    public String login(@RequestBody AuthRequest authRequest) throws AuthenticationException {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.username(), authRequest.password())
        );
        return jwtUtil.generateToken(authRequest.username());
    }
}

