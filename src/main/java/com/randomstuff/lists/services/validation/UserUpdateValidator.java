package com.randomstuff.lists.services.validation;


import com.randomstuff.lists.dtos.UserUpdateDto;
import com.randomstuff.lists.entities.User;
import com.randomstuff.lists.exceptions.FieldMessage;
import com.randomstuff.lists.repositories.UserRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@RequiredArgsConstructor
public class UserUpdateValidator implements ConstraintValidator<UserUpdateValid, UserUpdateDto> {

    final private UserRepository userRepository;

    @Override
    public void initialize(UserUpdateValid ann) {
    }

    @Override
    public boolean isValid(UserUpdateDto dto, ConstraintValidatorContext context) {

        List<FieldMessage> list = new ArrayList<>();

        User user = userRepository.findByEmail(dto.email());

        if(user != null && !Objects.equals(user.getId(), dto.id())){
            list.add(new FieldMessage("email","e-mail já existe para outro usuário."));
        }

        for (FieldMessage e : list) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(e.message()).addPropertyNode(e.fieldName())
                    .addConstraintViolation();
        }
        return list.isEmpty();
    }
}