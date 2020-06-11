package com.moderngas.service;

import javax.mail.MessagingException;

public interface EmailService {

    public void sendMail(String to, String subject, String body) throws MessagingException;



}
