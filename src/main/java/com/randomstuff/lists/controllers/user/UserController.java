package com.randomstuff.lists.controllers.user;

import com.randomstuff.lists.dtos.UserDto;
import com.randomstuff.lists.entities.Role;
import com.randomstuff.lists.entities.User;
import com.randomstuff.lists.repositories.RoleRepository;
import com.randomstuff.lists.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @PostMapping("/operator")
    ResponseEntity<User> createOperatorUser(@RequestBody UserDto userDto){
        User user = new User(userDto);

        Role role = roleRepository.findById(2L).get();
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        user.setRoles(roles);
        return ResponseEntity.ok(userRepository.save(user));
    }

    @GetMapping
    ResponseEntity<List<UserDto>> findAll(){
        List<UserDto> users = userRepository.findAll().stream().map(UserDto::new).toList();
        return new ResponseEntity(users,HttpStatus.OK);
    }
}
