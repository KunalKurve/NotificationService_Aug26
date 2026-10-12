package dev.kunal.notificationservice_aug26.clients;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.kunal.notificationservice_aug26.dtos.EmailDto;
import dev.kunal.notificationservice_aug26.utils.EmailUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;

@Component
public class KafkaConsumerWelcomeEmailClient {

    @Autowired
    private ObjectMapper objectMapper;

    // now we use Spring Kafka dependency
    // groupId= "emailService" for when code will be running on multiple servers on AWS,
    // we only want 1 consumer from the group to send email
    @KafkaListener(topics = "signup", groupId = "emailService")
    public void sendEmail(String message) {

        try {
            EmailDto emailDto = objectMapper.readValue(message, EmailDto.class);

            // Logic for Sending an Email in java
            Properties props = new Properties();
            props.put("mail.smtp.host", "smtp.gmail.com"); //SMTP Host
            props.put("mail.smtp.port", "587"); //TLS Port
            props.put("mail.smtp.auth", "true"); //enable authentication
            props.put("mail.smtp.starttls.enable", "true"); //enable STARTTLS

            //create Authenticator object to pass in Session.getInstance argument
            Authenticator auth = new Authenticator() {
                //override the getPasswordAuthentication method
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(emailDto.getFrom(), "app-password");
                }
            };
            Session session = Session.getInstance(props, auth);

            EmailUtil.sendEmail(session, emailDto.getTo(), emailDto.getSubject(), emailDto.getBody());


        } catch (JsonProcessingException exception) {
            throw new RuntimeException(exception.getMessage());
        }
    }

}
