package com.example.ddingjakyo_be.config.exception;

import com.example.ddingjakyo_be.common.constant.ResponseStatus;
import com.example.ddingjakyo_be.common.message.ResponseMessage;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException.Unauthorized;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ResponseMessage> IllegalArgumentException() {
    return new ResponseEntity<>(ResponseMessage.of(ResponseStatus.BAD_REQUEST),
        HttpStatus.BAD_REQUEST);
  }

//  @ExceptionHandler(NotFoundException.class)
//  public ResponseEntity<ResponseMessage> BadCredentialsException() {
//    return new ResponseEntity<>(ResponseMessage.of(ResponseStatus.BAD_CREDENTIAL),
//        HttpStatus.NOT_FOUND);
//  }

  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<ResponseMessage> NotFoundException() {
    return new ResponseEntity<>(ResponseMessage.of(ResponseStatus.MEMBER_NOT_FOUND),
        HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(Unauthorized.class)
  public ResponseEntity<ResponseMessage> UnAuthorizedException() {
    return new ResponseEntity<>(ResponseMessage.of(ResponseStatus.UNAUTHORIZED),
        HttpStatus.UNAUTHORIZED);
  }
}
