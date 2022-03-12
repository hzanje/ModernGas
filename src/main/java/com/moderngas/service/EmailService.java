package com.moderngas.service;

import javax.mail.MessagingException;

public interface EmailService {

    void sendMail(String to, String subject, String body) throws MessagingException;


}
