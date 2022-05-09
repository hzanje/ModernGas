package com.moderngas.service;

import com.moderngas.exception.BadRequestException;

public interface QRCodeService {

    String generateAndSaveQRCode(Long userId, String code) throws BadRequestException;
}
