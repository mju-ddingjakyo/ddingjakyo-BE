package com.example.ddingjakyo_be.common.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class NoAuthException extends RuntimeException{

  public NoAuthException(String message){
    super(message);
  }
}
