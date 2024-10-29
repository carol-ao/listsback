package com.randomstuff.lists.services;

import com.randomstuff.lists.dtos.RoleDto;
import com.randomstuff.lists.dtos.UserDto;
import com.randomstuff.lists.entities.User;
import com.randomstuff.lists.exceptions.ResourceNotFoundException;
import com.randomstuff.lists.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public List<UserDto> findAll(){
        return userRepository.findAll().stream().map( user ->
                new UserDto(user.getName(),
                user.getEmail(),
                user.getRoles().stream().map( role ->
                        new RoleDto(role.getId(),
                        role.getAuthority())).collect(Collectors.toSet()))).toList();
    }

    public  UserDto findById(Long id) throws ResourceNotFoundException {
        User user =  userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("id not found"));
        return new UserDto(user.getName(),
                user.getEmail(),
                user.getRoles().stream().map( role -> new RoleDto(role.getId(), role.getAuthority())).collect(Collectors.toSet()));
    }
}
