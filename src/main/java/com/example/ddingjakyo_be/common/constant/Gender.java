package com.example.ddingjakyo_be.common.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Gender {
  MALE(0), FEMALE(1);

  private final int identifier;

  public static Gender getGender(int userInput){
    for(Gender gender : Gender.values()){
      if(gender.identifier == userInput){
        return gender;
      }
    }
    return null;
  }
}
