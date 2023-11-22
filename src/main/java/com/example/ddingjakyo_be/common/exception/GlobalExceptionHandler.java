package com.example.ddingjakyo_be.common.exception;

import com.example.ddingjakyo_be.common.constant.ResponseStatus;
import com.example.ddingjakyo_be.common.message.ResponseMessage;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ResponseMessage> IllegalArgumentException() {
    return new ResponseEntity<>(ResponseMessage.of(ResponseStatus.BAD_REQUEST),
        HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(NoAuthException.class)
  public ResponseEntity<ResponseMessage> NoAuthException() {
    return new ResponseEntity<>(ResponseMessage.of(ResponseStatus.FORBIDDEN), HttpStatus.FORBIDDEN);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public static ResponseEntity<ResponseMessage> validationException(
      MethodArgumentNotValidException e) {

    BindingResult bindingResult = e.getBindingResult();
    StringBuilder errorMessage = new StringBuilder();

    bindingResult.getFieldErrors().forEach(
        column -> {
          errorMessage.append(column.getField());
          errorMessage.append(": ");
          errorMessage.append(column.getDefaultMessage());
          errorMessage.append("\n");
        }
    );
    return new ResponseEntity<>(ResponseMessage.of(ResponseStatus.BAD_REQUEST, errorMessage.toString()),
        HttpStatus.BAD_REQUEST);
  }
}
