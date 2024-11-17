package com.randomstuff.lists.dtos;

import java.util.Set;


public record UserDto(
        Long id,
        String name,
        String email,
        Set<String> roles) {}
