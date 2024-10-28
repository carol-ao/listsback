package com.randomstuff.lists.dtos;


import com.randomstuff.lists.entities.User;
import lombok.*;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class UserDto {

    private String name;
    private String email;
    private String password;

    private Set<RoleDto> roles = new HashSet<>();

    public UserDto(User user){
        name = user.getName();
        email = user.getEmail();
        password = user.getPassword();
        roles = user.getRoles().stream().map(RoleDto::new).collect(Collectors.toSet());
    }
}
