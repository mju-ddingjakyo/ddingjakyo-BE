package com.example.ddingjakyo_be.common.exception.custom;

public class TeamNotFoundException extends RuntimeException{

  public TeamNotFoundException(String message) {
    super(message);
  }
}
