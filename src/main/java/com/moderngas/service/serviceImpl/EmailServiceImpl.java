package com.moderngas.service.serviceImpl;

import com.moderngas.enums.MailSubject;
import com.moderngas.exception.BadRequestException;
import com.moderngas.jpaentity.UserEntity;
import com.moderngas.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.*;
import java.nio.charset.StandardCharsets;

@Service
public class EmailServiceImpl implements EmailService {

    private static Logger log = LoggerFactory.getLogger(EmailServiceImpl.class.getName());

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String fromMail;


    @Override
    public void sendMail(UserEntity userEntity, String password, MailSubject mailSubject) throws MessagingException, BadRequestException {
        try {
            log.info("Sending mail to {}", userEntity.getEmail());
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
             StandardCharsets.UTF_8.name());
            mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(userEntity.getEmail()));
            mimeMessage.setFrom(new InternetAddress(fromMail));
            mimeMessage.setSubject(mailSubject.getName());

            Multipart multipart = new MimeMultipart();
            MimeBodyPart mimeBodyPart = new MimeBodyPart();
            mimeBodyPart.setContent("<html><body>" +
                    "<p>" + generateMailBody(userEntity, password, mailSubject) + "</p><br>" +
                    "</body></html>", "text/html;charset=utf-8");
            multipart.addBodyPart(mimeBodyPart);
            mimeMessage.setContent(multipart);
            javaMailSender.send(mimeMessage);

        } catch (MailException | AddressException mex) {
            throw mex;
        } catch (MessagingException | BadRequestException e ) {
            throw e;
        }
    }

    @Override
    public String generateMailBody(UserEntity userEntity, String password, MailSubject mailSubject) throws BadRequestException {
        String mailBody = null;
        switch (mailSubject) {
            case MAIL_SUBJECT_FORGET_PASSWORD -> {
                mailBody = "Hi " + userEntity.getName() + ", <Br>" + "Have you forget your password to Modern Gas App, Don't worry we have provided a temporary password below, " +
                        "<Br><Br>Password : <Strong>" + password + "</Strong>" +
                        "<Br>Now you may directly login to Modern Gas Account with one time password. " +
                        "<Br><Br>Thanks & Regards, <Br> Team ModernGas";
            }
            case MAIL_SUBJECT_NEW_PASSWORD -> {
                mailBody = "Hi " + userEntity.getName() + ", <Br>" + "A Warm Welcome to Modern Gas App, We have provided a temporary password below, " +
                        "<Br><Br>Password : <Strong>" + password + "</Strong>" +
                        "<Br>Now you may directly login to Modern Gas Account with one time password. " +
                        "<Br><Br>Thanks & Regards, <Br> Team ModernGas";
            }
        }
        return mailBody;
    }
}
