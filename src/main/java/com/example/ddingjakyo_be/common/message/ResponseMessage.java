package com.example.ddingjakyo_be.common.message;

import com.example.ddingjakyo_be.common.constant.ResponseStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Data
public class ResponseMessage {

  private ResponseStatus responseStatus;
  private String resultMessage;
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private Object data;

  @Builder
  private ResponseMessage(ResponseStatus responseStatus, String resultMessage, Object data) {
    this.responseStatus = responseStatus;
    this.resultMessage = resultMessage;
    this.data = data;
  }

  public static ResponseMessage of(ResponseStatus responseStatus, Object data) {
    return ResponseMessage.builder()
        .responseStatus(responseStatus)
        .resultMessage(responseStatus.getResultMessage())
        .data(data)
        .build();
  }

  public static ResponseMessage of(ResponseStatus responseStatus) {
    return ResponseMessage.builder()
        .responseStatus(responseStatus)
        .resultMessage(responseStatus.getResultMessage())
        .build();
  }
}
