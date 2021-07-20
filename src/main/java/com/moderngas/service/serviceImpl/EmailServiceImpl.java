package com.moderngas.service.serviceImpl;

import com.moderngas.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

@Slf4j
@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String fromMail;


    @Override
    public void sendMail(String to, String subject, String body) throws MessagingException {
        try {
            log.info("Sending mail to {}", to);
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            /** MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name()); */
            mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
            mimeMessage.setFrom(new InternetAddress(fromMail));
            mimeMessage.setSubject(subject);

            Multipart multipart = new MimeMultipart();
            MimeBodyPart mimeBodyPart = new MimeBodyPart();
            mimeBodyPart.setContent("<html><body>" +
                    "<p>" + body + "</p><br>" +
                    "</body></html>", "text/html;charset=utf-8");
            multipart.addBodyPart(mimeBodyPart);
            mimeMessage.setContent(multipart);
            javaMailSender.send(mimeMessage);

        } catch (MailException | AddressException mex) {
            throw mex;
        } catch (MessagingException e) {
            throw e;
        }
    }
}
