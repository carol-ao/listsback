package com.randomstuff.lists.dtos;

import java.util.Set;


public record UserDto(String name, String email, Set<RoleDto> roleDtoSet) {}
