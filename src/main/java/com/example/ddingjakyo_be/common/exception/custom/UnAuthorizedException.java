package com.example.ddingjakyo_be.common.exception.custom;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class UnAuthorizedException extends RuntimeException{

  public UnAuthorizedException(String message){
    super(message);
  }
}
