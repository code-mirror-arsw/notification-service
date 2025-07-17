package com.code_room.notification_service.domain.ports;

public interface SendEmailService {

    void sendInterviewAcceptedEmail(String to, String name, String offerTitle);

    void sendInterviewReminderEmail(String to, String name, String link);

    void sendInterviewScheduledEmail(String to, String name, String date, String link);

    void sendEvaluationResultEmail(String to, String name, int score, String feedback);

    void sendApplicationRejectedEmail(String to, String name);
}
