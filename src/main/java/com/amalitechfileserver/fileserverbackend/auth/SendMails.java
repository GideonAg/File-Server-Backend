package com.amalitechfileserver.fileserverbackend.auth;

import com.amalitechfileserver.fileserverbackend.dto.FileShareDto;
import com.amalitechfileserver.fileserverbackend.entity.FileEntity;
import com.amalitechfileserver.fileserverbackend.entity.UserEntity;
import com.amalitechfileserver.fileserverbackend.entity.UserToken;
import com.amalitechfileserver.fileserverbackend.exception.InputBlank;
import com.amalitechfileserver.fileserverbackend.repository.FileRepository;
import com.amalitechfileserver.fileserverbackend.repository.UserTokenRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class SendMails {

    @Value("${spring.mail.username}")
    private String fileServerEmail;

    private final FileRepository fileRepository;
    private final UserTokenRepository userTokenRepository;
    private final JavaMailSender javaMailSender;

    @Async
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

    @Async
    public void sendPasswordResetEmail(UserEntity user) {
        String token = getUserToken(user);
        String mailSubject = "File Server Password Reset Link";
        String mailBody = """
                Password Reset,
                
                Kindly tap on the link below to reset your account password
                
                http://localhost:5173/update-password/%s
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
    }

    @Async
    public void sendFileShareEmail(FileShareDto fileShareDto, FileEntity fetchedFile) throws MessagingException, InputBlank {
        if (fileShareDto.getReceiverEmail().isBlank())
            throw new InputBlank("Receiver email is required");

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message, true);
        mimeMessageHelper.setTo(fileShareDto.getReceiverEmail());
        mimeMessageHelper.setFrom(fileServerEmail);
        mimeMessageHelper.setSubject("File sent to you from File Server");

        ByteArrayResource byteArrayResource = new ByteArrayResource(fetchedFile.getFile());
        mimeMessageHelper.addAttachment(
                fetchedFile.getTitle(), byteArrayResource, fetchedFile.getFileType()
        );

        javaMailSender.send(message);
        fetchedFile.setNumberOfShares(fetchedFile.getNumberOfShares() + 1);
        fileRepository.save(fetchedFile);
    }
}
