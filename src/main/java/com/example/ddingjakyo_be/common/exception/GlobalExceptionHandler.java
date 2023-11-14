package com.example.ddingjakyo_be.common.exception;

import com.example.ddingjakyo_be.common.constant.ResponseStatus;
import com.example.ddingjakyo_be.common.message.ResponseMessage;
import javax.security.sasl.AuthenticationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ResponseMessage> IllegalArgumentException(){
    return new ResponseEntity<>(ResponseMessage.of(ResponseStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(AuthenticationException.class)
  public ResponseEntity<ResponseMessage> AuthenticationException(){
    return new ResponseEntity<>(ResponseMessage.of(ResponseStatus.FORBIDDEN), HttpStatus.FORBIDDEN);
  }
}
