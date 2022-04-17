package com.moderngas.service;

import com.moderngas.enums.MailSubject;
import com.moderngas.exception.BadRequestException;
import com.moderngas.jpaentity.UserEntity;

import javax.mail.MessagingException;

public interface EmailService {

    void sendMail(UserEntity userEntity, String password, MailSubject mailSubject) throws MessagingException, BadRequestException;

    String generateMailBody(UserEntity userEntity, String password, MailSubject mailSubject) throws BadRequestException;


}
