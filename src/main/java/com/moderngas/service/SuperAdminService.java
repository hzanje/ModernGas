package com.moderngas.service;

import com.moderngas.exception.BadRequestException;
import com.moderngas.pojo.superadmin.AdminEntityDto;

import javax.mail.MessagingException;
import java.security.NoSuchAlgorithmException;

public interface SuperAdminService {

    String createAdmin(Long superId, AdminEntityDto adminEntityDto) throws BadRequestException, MessagingException, NoSuchAlgorithmException;

}
