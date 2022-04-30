package com.moderngas.service;

import com.moderngas.exception.BadRequestException;

public interface QRCodeService {

    String generateAndSaveQRCode(String code) throws BadRequestException;
}
