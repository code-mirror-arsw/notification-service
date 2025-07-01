package com.code_room.notification_service.domain.exception;


import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class NotificationsException extends Exception {

  private final String message;
  private final String detail;
  private final int errorCode;

  public NotificationsException(String message, String detail, int errorCode) {
    super(message);
    this.message = message;
    this.detail = detail;
    this.errorCode = errorCode;
  }
}
