package com.randomstuff.lists.exceptions;

import java.time.Instant;
import java.util.List;

public record ValidationError(
        Instant timestamp,
        Integer status,
        String mainError,
        String message,
        String path,
        List<FieldMessage> errors

){}
