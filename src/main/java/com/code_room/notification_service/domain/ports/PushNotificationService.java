package com.code_room.notification_service.domain.ports;


import com.code_room.notification_service.domain.exception.NotificationsException;

public interface PushNotificationService {

  void sendToEmail(String message) throws Exception;
}
