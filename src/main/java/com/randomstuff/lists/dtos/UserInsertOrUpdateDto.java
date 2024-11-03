package com.randomstuff.lists.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.Set;

public record UserInsertOrUpdateDto(
        Long id,
        @NotBlank(message = "Um usuário precisa de um nome.")
        String name,
        @Email
        @NotBlank(message = "Um usuário precisa de um e-mail.")
        String email,
        @NotBlank(message = "Um usuário precisa de um password.")
        String password,
        @NotEmpty(message = "Pelo menos um role precisa ser dado ao usuário.")
        Set<RoleDto> roleDtoSet) {
}
