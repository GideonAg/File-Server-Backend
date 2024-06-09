package com.amalitechfileserver.fileserverbackend.auth;

import com.amalitechfileserver.fileserverbackend.dto.FileShareDto;
import com.amalitechfileserver.fileserverbackend.entity.FileEntity;
import com.amalitechfileserver.fileserverbackend.entity.UserEntity;
import com.amalitechfileserver.fileserverbackend.entity.UserToken;
import com.amalitechfileserver.fileserverbackend.repository.FileRepository;
import com.amalitechfileserver.fileserverbackend.repository.UserTokenRepository;
import jakarta.activation.DataHandler;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import jakarta.mail.util.ByteArrayDataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static com.amalitechfileserver.fileserverbackend.service.FileServerServiceImpl.getFilename;

@Component
@RequiredArgsConstructor
public class SendMails {

    @Value("${spring.mail.username}")
    private String fileServerEmail;

    @Value("${file.server.base-url}")
    private String fileServerBaseUrl;

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
                
                %s
                """;

        String url = fileServerBaseUrl + "/auth/register/verify?token=" + token;

        try {
            sendMail(user.getEmail(), mailSubject, mailBody, url);
        } catch (Exception exception) {
            throw new RuntimeException("Failed to send verification email, try again later");
        }
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

        try {
            sendMail(user.getEmail(), mailSubject, mailBody, token);
        } catch (Exception exception) {
            throw new RuntimeException("Failed to send password reset email, try again later");
        }
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

    private void sendMail(String userEmail, String mailSubject, String mailBody, String url) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setFrom(fileServerEmail);
        mailMessage.setTo(userEmail);
        mailMessage.setSubject(mailSubject);
        String text = String.format(mailBody, url);
        mailMessage.setText(text);

        javaMailSender.send(mailMessage);
    }

    @Async
    public void sendFileShareEmail(FileShareDto fileShareDto, FileEntity fetchedFile) {

        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeBodyPart mimeBodyPart = getMimeBodyPart(fileShareDto, fetchedFile, message);

            MimeMultipart multipart = new MimeMultipart();
            multipart.addBodyPart(mimeBodyPart);
            message.setContent(multipart);

            javaMailSender.send(message);
            fetchedFile.setNumberOfShares(fetchedFile.getNumberOfShares() + 1);
            fileRepository.save(fetchedFile);
        } catch(Exception e) {
            throw new RuntimeException("Failed to send file, try again later");
        }
    }

    private MimeBodyPart getMimeBodyPart(FileShareDto fileShareDto, FileEntity fetchedFile, MimeMessage message) throws MessagingException {

        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message, true);
        mimeMessageHelper.setTo(fileShareDto.getReceiverEmail());
        mimeMessageHelper.setFrom(fileServerEmail);
        mimeMessageHelper.setSubject("File sent to you from File Server");

        String filename = getFilename(fetchedFile);
        MimeBodyPart mimeBodyPart = new MimeBodyPart();
        ByteArrayDataSource dataSource = new ByteArrayDataSource(fetchedFile.getFile(), fetchedFile.getFileType());

        mimeBodyPart.setDataHandler(new DataHandler(dataSource));
        mimeBodyPart.setFileName(filename);
        return mimeBodyPart;
    }
}
