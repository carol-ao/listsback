package com.randomstuff.lists.services;

import com.randomstuff.lists.dtos.RoleDto;
import com.randomstuff.lists.dtos.UserDto;
import com.randomstuff.lists.dtos.UserInsertOrUpdateDto;
import com.randomstuff.lists.entities.Role;
import com.randomstuff.lists.entities.User;
import com.randomstuff.lists.exceptions.EmailAlreadyRegisteredException;
import com.randomstuff.lists.exceptions.ResourceNotFoundException;
import com.randomstuff.lists.projections.UserDetailsProjection;
import com.randomstuff.lists.repositories.RoleRepository;
import com.randomstuff.lists.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public List<UserDto> findAll(){
        return userRepository.findAll().stream().map( user ->
                new UserDto(
                        user.getId(),
                        user.getName(),
                        user.getEmail(),
                        user.getRoles().stream().map(Role::getAuthority).collect(Collectors.toSet()))
        ).toList();
    }

    public  UserDto findById(Long id) {
        User user =  userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("id not found"));
        return new UserDto(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRoles().stream().map(Role::getAuthority).collect(Collectors.toSet()));
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
            roles.add(roleRepository.findByAuthority(roleDto.authority()).orElseThrow(() -> new ResourceNotFoundException("Role of authority "+roleDto.authority()+" not found.")));
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
            roles.add(roleRepository.findByAuthority(roleDto.authority()).orElseThrow(() -> new ResourceNotFoundException("Role of authority "+roleDto.authority()+" not found.")));
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
        User user = userRepository.findById(id).orElseThrow( () -> new ResourceNotFoundException("User of id "+id+" was not found."));
        userRepository.delete(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<UserDetailsProjection> result = roleRepository.searchUserAndRolesByEmail(username);
        if(result.isEmpty()){
            throw new UsernameNotFoundException("Usuário não encontrado");
        }

        User user = new User();
        user.setPassword(result.getFirst().getPassword());
        user.setEmail(username);
        for(UserDetailsProjection projection : result){
            user.getRoles().add(new Role(projection.getRoleId(), projection.getAuthority()));
        }

        return user;
    }
}
