package com.example.ddingjakyo_be.common.exception.custom;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class NoAuthException extends RuntimeException{

  public NoAuthException(String message){
    super(message);
  }
}
