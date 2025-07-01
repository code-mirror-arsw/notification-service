package com.code_room.notification_service.domain.usecases;

import com.code_room.notification_service.domain.ports.SendEmailService;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Service
public class SendEmailServiceImpl implements SendEmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;


    @Override
    public void sendInterviewAcceptedEmail(String to, String name, String offerTitle) {
        sendHtmlEmail(to, "🤝 ¡Has sido aceptado para la entrevista!", generateInterviewAcceptedBody(name, offerTitle));
    }

    @Override
    public void sendInterviewReminderEmail(String to, String name, String link) {
        sendHtmlEmail(to, "⏰ Recordatorio: Entrevista en 1 hora", generateInterviewReminderBody(name, link));
    }

    @Override
    public void sendInterviewScheduledEmail(String to, String name, String date, String link) {
        LocalDateTime dateTime = LocalDateTime.parse(date);

        String formattedDate = dateTime.format(DateTimeFormatter.ofPattern("EEEE dd 'de' MMMM 'de' yyyy 'a las' hh:mm a", new Locale("es", "ES")));

        String body = generateInterviewScheduledBody(name, formattedDate, link);
        sendHtmlEmail(to, "📅 Entrevista programada – ¡Prepárate!", body);
    }


    @Override
    public void sendApplicationRejectedEmail(String to, String name) {
        String body = generateApplicationRejectedBody(name);
        sendHtmlEmail(to, "❌ Postulación rechazada – Code Room", body);
    }

    private void sendHtmlEmail(String to, String subject, String htmlBody) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setFrom(from);
            helper.setText(htmlBody, true);

            mailSender.send(message);

            System.out.println("Email sent to: " + to);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error sending email to: " + to, e);
        }
    }


    private String generateInterviewAcceptedBody(String name, String offerTitle) {
        return htmlTemplate("¡Hola, " + name + "! 🎉",
                "¡Felicidades! Has sido aceptado para la entrevista de <strong>" + offerTitle + "</strong> 🚀",
                "Recibirás más detalles pronto. ¡Prepárate y mucho éxito! 💪",
                null);
    }

    private String generateInterviewReminderBody(String name, String link) {
        return htmlTemplate("¡Hola, " + name + "! ⏰",
                "Tu entrevista comenzará en aproximadamente 1 hora.",
                "Puedes ingresar a través del siguiente enlace:",
                link);
    }

    private String generateInterviewScheduledBody(String name, String date, String link) {
        return htmlTemplate(
                "Hola " + name + " 👋",
                "Tu entrevista ha sido programada para el <strong>" + date + "</strong>.",
                "Por favor, únete al siguiente enlace a la hora indicada:",
                link
        );
    }

    private String generateApplicationRejectedBody(String name) {
        return htmlTemplate(
                "Hola " + name + " 😔",
                "Lamentablemente, tu postulación no fue aceptada esta vez.",
                "Te animamos a seguir participando en futuras oportunidades en Code Room. ¡Mucho ánimo! 💪",
                null
        );
    }


    private String htmlTemplate(String greeting, String mainMsg, String extraMsg, String codeOrLink) {
        return "<!DOCTYPE html><html><head><style>" +
                "body { font-family: Arial, sans-serif; margin: 20px; color: #333; }" +
                ".container { background-color: #f4f9ff; padding: 30px; border-radius: 10px; border: 1px solid #ccc; }" +
                "h2 { color: #1f3b75; }" +
                "p { font-size: 16px; line-height: 1.6; }" +
                ".box { margin-top: 20px; padding: 15px; background: #dceeff; border-radius: 8px; font-size: 18px; font-weight: bold; text-align: center; color: #0a3d62; }" +
                ".footer { margin-top: 30px; font-size: 12px; color: #888; }" +
                "</style></head><body><div class='container'>" +
                "<h2>" + greeting + "</h2>" +
                "<p>" + mainMsg + "</p>" +
                (extraMsg != null ? "<p>" + extraMsg + "</p>" : "") +
                (codeOrLink != null ? "<div class='box'>" + codeOrLink + "</div>" : "") +
                "<p><strong>– El equipo de Code Room 🧠</strong></p>" +
                "<div class='footer'>Este es un mensaje automático. Por favor, no respondas a este correo.</div>" +
                "</div></body></html>";
    }
}
