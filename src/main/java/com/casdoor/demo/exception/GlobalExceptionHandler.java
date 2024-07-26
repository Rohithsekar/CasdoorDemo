package com.casdoor.demo.exception;

import com.casdoor.demo.constants.Constants;
import com.casdoor.demo.dto.APIResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<APIResponse> handleBadRequestException(BadRequestException e){
        return ResponseEntity.badRequest().body(new APIResponse(Constants.ERROR, e.getMessage(), new ArrayList<>()));
    }


    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<APIResponse> handleBadRequestException(HttpMessageNotReadableException e){
        return ResponseEntity.badRequest().body(new APIResponse(Constants.ERROR, e.getMessage(), new ArrayList<>()));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public APIResponse handleValidationExceptions(MethodArgumentNotValidException ex) {
        APIResponse apiResponseBody = new APIResponse();

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach((error) -> {
            String fieldName = error.getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        // Flatten the errors Map into a single string
        String flattenedErrors = errors.entrySet().stream()
                .map(entry -> entry.getValue())
                .collect(Collectors.joining(", "));

        apiResponseBody.setStatus(Constants.ERROR);
        apiResponseBody.setMessage("Invalid request body. " + flattenedErrors);


        return apiResponseBody;
    }


    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<APIResponse> handleBadRequestException(IllegalArgumentException e){
        return ResponseEntity.badRequest().body(new APIResponse(Constants.ERROR, e.getMessage(), new ArrayList<>()));
    }




}
