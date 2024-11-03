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

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
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

    public  UserDto findById(Long id) throws ResourceNotFoundException {
        User user =  userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("id not found"));
        return new UserDto(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRoles().stream().map( role -> new RoleDto(role.getId(), role.getAuthority())).collect(Collectors.toSet()));
    }

    @Transactional
    public UserInsertOrUpdateDto insert(UserInsertOrUpdateDto userInsertOrUpdateDto) throws ResourceNotFoundException, EmailAlreadyRegisteredException {

        User user = userRepository.findByEmail(userInsertOrUpdateDto.email());
        if(user != null){
            throw new EmailAlreadyRegisteredException("Esse e-mail já está cadastrado para outro usuário.");
        }
        user = User.builder().
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
    public UserInsertOrUpdateDto patch(UserInsertOrUpdateDto userInsertOrUpdateDto) throws ResourceNotFoundException, EmailAlreadyRegisteredException {

        User user = userRepository.findByEmail(userInsertOrUpdateDto.email());
        if(user != null && !Objects.equals(user.getId(), userInsertOrUpdateDto.id())){
            throw new EmailAlreadyRegisteredException("Esse e-mail já está cadastrado para outro usuário.");
        }

        user = User.builder().
                id(userInsertOrUpdateDto.id()).
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
    public void delete(Long id) throws ResourceNotFoundException {
        User user = userRepository.findById(id).orElseThrow( () -> new ResourceNotFoundException("User of id "+" was not found."));
        userRepository.delete(user);
    }
}
