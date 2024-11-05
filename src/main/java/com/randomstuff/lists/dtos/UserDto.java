package com.randomstuff.lists.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.Set;


public record UserDto(
        Long id,
        String name,
        String email,
        Set<RoleDto> roleDtoSet) {}
