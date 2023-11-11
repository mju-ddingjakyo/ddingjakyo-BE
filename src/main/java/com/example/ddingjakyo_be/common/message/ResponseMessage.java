package com.example.ddingjakyo_be.common.message;

import com.example.ddingjakyo_be.common.constant.StatusEnum;
import lombok.Builder;
import lombok.Data;

@Data
public class ResponseMessage {
  private StatusEnum status;
  private String message;
  private Object data;

  @Builder
  private ResponseMessage(StatusEnum status, String message, Object data) {
    this.status = status;
    this.message = message;
    this.data = data;
  }

  public static ResponseMessage of(StatusEnum status, String message, Object data) {
    return ResponseMessage.builder()
        .status(status)
        .message(message)
        .data(data)
        .build();
  }
}
