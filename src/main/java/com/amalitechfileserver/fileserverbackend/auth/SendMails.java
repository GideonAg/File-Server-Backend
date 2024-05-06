package com.amalitechfileserver.fileserverbackend.auth;

import com.amalitechfileserver.fileserverbackend.dto.FileShareDto;
import com.amalitechfileserver.fileserverbackend.entity.FileEntity;
import com.amalitechfileserver.fileserverbackend.entity.UserEntity;
import com.amalitechfileserver.fileserverbackend.entity.UserToken;
import com.amalitechfileserver.fileserverbackend.repository.UserTokenRepository;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class SendMails {

    @Value("${spring.mail.username}")
    private String fileServerEmail;

    private final UserTokenRepository userTokenRepository;
    private final JavaMailSender javaMailSender;

    public void sendVerificationEmail(UserEntity user) {
        String token = getUserToken(user);
        String mailSubject = "File Server Account Verification";
        String mailBody = """
                Account Verification,
                
                Kindly tap on the link below to verify your account
                
                http://localhost:8080/auth/register/verify?token=%s
                """;

        sendMail(user.getEmail(), mailSubject, mailBody, token);
    }

    public void sendPasswordResetEmail(UserEntity user) {
        String token = getUserToken(user);
        String mailSubject = "File Server Password Reset Link";
        String mailBody = """
                Password Reset,
                
                Kindly tap on the link below to reset your account password
                
                http://localhost:8080/auth/register/verify?token=%s
                """;

        sendMail(user.getEmail(), mailSubject, mailBody, token);
    }

    private String getUserToken(UserEntity user) {
        String token = UUID.randomUUID().toString();
        UserToken userToken = UserToken.builder()
                .token(token)
                .user(user)
                .build();
        userTokenRepository.save(userToken);
        return token;
    }

    private void sendMail(String userEmail, String mailSubject, String mailBody, String token) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(fileServerEmail);
        mailMessage.setTo(userEmail);
        mailMessage.setSubject(mailSubject);
        String text = String.format(mailBody, token);
        mailMessage.setText(text);
        javaMailSender.send(mailMessage);
        System.out.println(text);
    }

    public void sendFileShareEmail(FileShareDto fileShareDto, FileEntity fetchedFile) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fileShareDto.getSenderEmail());
        message.setTo(fileShareDto.getReceiverEmail());
        message.setSubject(fileShareDto.getSenderEmail() + " sent you a file from File Server");
        message.setText(String.valueOf(fetchedFile));
        javaMailSender.send(message);
    }
}
