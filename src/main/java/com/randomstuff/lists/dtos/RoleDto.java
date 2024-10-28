package com.randomstuff.lists.dtos;

import com.randomstuff.lists.entities.Role;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class RoleDto {

    private Long id;
    private String authority;

    public RoleDto(Role role){
        id = role.getId();
        authority = role.getAuthority();
    }
}
