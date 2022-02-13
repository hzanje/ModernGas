package com.moderngas.service;

import com.moderngas.exception.BadRequestException;
import com.moderngas.pojo.superadmin.AdminEntityDto;

public interface SuperAdminService {

    String createAdmin(Long superId, AdminEntityDto adminEntityDto) throws BadRequestException;
}
