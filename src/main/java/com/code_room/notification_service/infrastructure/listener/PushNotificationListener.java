package com.code_room.notification_service.infrastructure.listener;


import com.code_room.notification_service.domain.exception.NotificationsException;
import com.code_room.notification_service.domain.ports.PushNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class PushNotificationListener {

  @Autowired
  private PushNotificationService pushNotificationService;

  @KafkaListener(
          topics = "${kafka.topic.name}",
          groupId = "${kafka.group.id}"
  )
  public void listen(String message) throws Exception {
    System.out.println("Mensaje recibido de Kafka: " + message);

    pushNotificationService.sendToEmail(message);
  }
}
