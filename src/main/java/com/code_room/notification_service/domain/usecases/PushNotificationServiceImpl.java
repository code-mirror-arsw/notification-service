package com.code_room.notification_service.domain.usecases;


import com.code_room.notification_service.domain.mapper.NotificationsMapper;
import com.code_room.notification_service.domain.model.FcmMessage;
import com.code_room.notification_service.domain.ports.PushNotificationService;
import com.code_room.notification_service.domain.ports.SendEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class PushNotificationServiceImpl implements PushNotificationService {

    @Autowired
    private SendEmailService sendEmailService;

    @Override
    public void sendToEmail(String message) throws Exception {
        try {
            FcmMessage fcmMessage = NotificationsMapper.mapJsonToFcm(message);
            if (fcmMessage == null || fcmMessage.getSource() == null) {
                throw new RuntimeException("Invalid message or missing notification type");
            }

            String to = fcmMessage.getTo();
            Map<String, String> data = fcmMessage.getData();
            String name = data.getOrDefault("name", "usuario");

            switch (fcmMessage.getSource()) {
                case INTERVIEW_REMINDER_ONE_HOUR:
                    sendEmailService.sendInterviewReminderEmail(to, name, data.get("link"));
                    break;

                case INTERVIEW_ACCEPTED_LINK:
                    sendEmailService.sendInterviewAcceptedEmail(to, name, data.get("link"));
                    break;

                case INTERVIEW_SCHEDULED:
                    sendEmailService.sendInterviewScheduledEmail(to, name, data.get("date"), data.get("link"));
                    break;

                case APPLICATION_REJECTED:
                    sendEmailService.sendApplicationRejectedEmail(to, name);
                    break;


                default:
                    throw new Exception("Unknown notification type: " + fcmMessage.getSource());
            }

        } catch (Exception e) {
            throw new Exception("Error processing notification: " + e.getMessage());
        }
    }


}

