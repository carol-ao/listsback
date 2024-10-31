package com.randomstuff.lists.dtos;

import java.util.Set;

public record UserInsertOrUpdateDto(Long id, String name, String email, String password, Set<RoleDto> roleDtoSet) {
}
