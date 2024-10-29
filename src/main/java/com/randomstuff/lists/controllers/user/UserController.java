package com.randomstuff.lists.controllers.user;

import com.randomstuff.lists.dtos.UserDto;
import com.randomstuff.lists.exceptions.ResourceNotFoundException;
import com.randomstuff.lists.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @GetMapping
    ResponseEntity<List<UserDto>> findAll(){
        List<UserDto> users = userService.findAll();
        return new ResponseEntity(users,HttpStatus.OK);
    }

    @GetMapping("/{id}")
    ResponseEntity<UserDto> findById(@PathVariable(required = true, name = "id") Long id) throws ResourceNotFoundException {
        UserDto userDto = userService.findById(id);
        return new ResponseEntity(userDto,HttpStatus.OK);
    }
}
