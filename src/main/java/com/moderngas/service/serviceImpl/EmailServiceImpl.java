package com.moderngas.service.serviceImpl;

import com.moderngas.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender javaMailSender;


    @Override
    public void sendMail(String to, String subject, String body) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name());
            mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
            mimeMessage.setFrom(new InternetAddress("abhishekmoderngas@gmail.com"));
            mimeMessage.setSubject(subject);

            Multipart multipart = new MimeMultipart();
            MimeBodyPart mimeBodyPart = new MimeBodyPart();
            mimeBodyPart.setContent("<html><body>" +
                    "<p>" + body + "</p><br><br>" +
                    "<img src='cid:identifier1234'></body></html>", "text/html;charset=utf-8");
            multipart.addBodyPart(mimeBodyPart);
            mimeMessage.setContent(multipart);

            /*FileSystemResource res = new FileSystemResource(new File("fileToAttach"));
            helper.addInline("projectImg", res);*/

            javaMailSender.send(mimeMessage);

        } catch (MailException | AddressException mex) {
            mex.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
