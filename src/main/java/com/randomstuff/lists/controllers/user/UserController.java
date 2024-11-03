package com.randomstuff.lists.controllers.user;

import com.randomstuff.lists.dtos.UserDto;
import com.randomstuff.lists.dtos.UserInsertDto;
import com.randomstuff.lists.dtos.UserUpdateDto;
import com.randomstuff.lists.exceptions.ResourceNotFoundException;
import com.randomstuff.lists.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @GetMapping
    ResponseEntity<List<UserDto>> findAll(){
        List<UserDto> users = userService.findAll();
        return new ResponseEntity<>(users,HttpStatus.OK);
    }

    @GetMapping("/{id}")
    ResponseEntity<UserDto> findById(@PathVariable(required = true, name = "id") Long id) throws ResourceNotFoundException {
        UserDto userDto = userService.findById(id);
        return new ResponseEntity<>(userDto,HttpStatus.OK);
    }

    @PostMapping
    ResponseEntity<UserInsertDto> insert(@Valid @RequestBody UserInsertDto userInsertDto) throws ResourceNotFoundException {
        userInsertDto = userService.insert(userInsertDto);
        return new ResponseEntity<>(userInsertDto, HttpStatus.CREATED);
    }

    @PatchMapping
    ResponseEntity<UserUpdateDto> patch(@Valid @RequestBody UserUpdateDto userUpdateDto) throws ResourceNotFoundException {
        userUpdateDto = userService.patch(userUpdateDto);
        return new ResponseEntity<>(userUpdateDto, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(@PathVariable(required = true, name = "id") Long id) throws ResourceNotFoundException {
        userService.delete(id);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }
}
