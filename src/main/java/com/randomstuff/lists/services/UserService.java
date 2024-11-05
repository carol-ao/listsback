package com.randomstuff.lists.services;

import com.randomstuff.lists.dtos.RoleDto;
import com.randomstuff.lists.dtos.UserDto;
import com.randomstuff.lists.dtos.UserInsertOrUpdateDto;
import com.randomstuff.lists.entities.Role;
import com.randomstuff.lists.entities.User;
import com.randomstuff.lists.exceptions.EmailAlreadyRegisteredException;
import com.randomstuff.lists.exceptions.ResourceNotFoundException;
import com.randomstuff.lists.repositories.RoleRepository;
import com.randomstuff.lists.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public List<UserDto> findAll(){
        return userRepository.findAll().stream().map( user ->
                new UserDto(
                        user.getId(),
                        user.getName(),
                        user.getEmail(),
                        user.getRoles().stream().map( role ->
                        new RoleDto(role.getId(),
                        role.getAuthority())).collect(Collectors.toSet()))).toList();
    }

    public  UserDto findById(Long id) {
        User user =  userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("id not found"));
        return new UserDto(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRoles().stream().map( role -> new RoleDto(role.getId(), role.getAuthority())).collect(Collectors.toSet()));
    }

    @Transactional
    public UserInsertOrUpdateDto insert(UserInsertOrUpdateDto userInsertOrUpdateDto) {

        if(userRepository.findByEmail(userInsertOrUpdateDto.email()).isPresent()){
            throw new EmailAlreadyRegisteredException("Esse e-mail já está cadastrado para outro usuário.");
        }

        User user = User.builder().
                name(userInsertOrUpdateDto.name()).
                email(userInsertOrUpdateDto.email()).
                password(passwordEncoder.encode(userInsertOrUpdateDto.password())).
                build();
        Set<Role> roles = new HashSet<>();
        for(RoleDto roleDto : userInsertOrUpdateDto.roleDtoSet()){
            roles.add(roleRepository.findById(roleDto.id()).orElseThrow(() -> new ResourceNotFoundException("Role id "+roleDto.id()+" not found.")));
        }
        user.setRoles(roles);
        user = userRepository.save(user);
        return new UserInsertOrUpdateDto(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPassword(),
                user.getRoles().stream().map(role -> new RoleDto(role.getId(), role.getAuthority())).collect(Collectors.toSet()));
    }

    @Transactional
    public UserInsertOrUpdateDto patch(UserInsertOrUpdateDto userInsertOrUpdateDto) {

        User user = userRepository.findById(userInsertOrUpdateDto.id())
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado para atualizar."));

        if(userRepository.findByEmail(userInsertOrUpdateDto.email()).isPresent() ){
            User existingUser = userRepository.findByEmail(userInsertOrUpdateDto.email()).get();
            if(!Objects.equals(existingUser.getId(), userInsertOrUpdateDto.id())){
                throw new EmailAlreadyRegisteredException("Esse e-mail já está cadastrado para outro usuário.");
            }
        }

        user.setName(userInsertOrUpdateDto.name());
        user.setEmail(userInsertOrUpdateDto.email());
        user.setPassword(passwordEncoder.encode(userInsertOrUpdateDto.password()));

        Set<Role> roles = new HashSet<>();
        for(RoleDto roleDto : userInsertOrUpdateDto.roleDtoSet()){
            roles.add(roleRepository.findById(roleDto.id()).orElseThrow(() -> new ResourceNotFoundException("Role id "+roleDto.id()+" not found.")));
        }
        user.setRoles(roles);
        user = userRepository.save(user);
        return new UserInsertOrUpdateDto(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPassword(),
                user.getRoles().stream().map(role -> new RoleDto(role.getId(), role.getAuthority())).collect(Collectors.toSet()));
    }

    @Transactional
    public void delete(Long id) {
        User user = userRepository.findById(id).orElseThrow( () -> new ResourceNotFoundException("User of id "+" was not found."));
        userRepository.delete(user);
    }
}
