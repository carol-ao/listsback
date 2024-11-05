package com.randomstuff.lists.controllers.user;

import com.randomstuff.lists.dtos.UserDto;
import com.randomstuff.lists.dtos.UserInsertOrUpdateDto;
import com.randomstuff.lists.exceptions.EmailAlreadyRegisteredException;
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
    ResponseEntity<UserDto> findById(@PathVariable(required = true, name = "id") Long id) {
        UserDto userDto = userService.findById(id);
        return new ResponseEntity<>(userDto,HttpStatus.OK);
    }

    @PostMapping
    ResponseEntity<UserInsertOrUpdateDto> insert(@Valid @RequestBody UserInsertOrUpdateDto userInsertOrUpdateDto) {
        userInsertOrUpdateDto = userService.insert(userInsertOrUpdateDto);
        return new ResponseEntity<>(userInsertOrUpdateDto, HttpStatus.CREATED);
    }

    @PatchMapping
    ResponseEntity<UserInsertOrUpdateDto> patch(@Valid @RequestBody UserInsertOrUpdateDto userInsertOrUpdateDto)  {
        userInsertOrUpdateDto = userService.patch(userInsertOrUpdateDto);
        return new ResponseEntity<>(userInsertOrUpdateDto, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(@PathVariable(required = true, name = "id") Long id) throws ResourceNotFoundException {
        userService.delete(id);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }
}
