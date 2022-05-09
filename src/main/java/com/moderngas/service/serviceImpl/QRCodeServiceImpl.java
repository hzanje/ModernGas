package com.moderngas.service.serviceImpl;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.moderngas.exception.BadRequestException;
import com.moderngas.security.AESUtil;
import com.moderngas.service.QRCodeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

@Service
public class QRCodeServiceImpl implements QRCodeService {

    private static Logger log = LoggerFactory.getLogger(QRCodeServiceImpl.class.getName());

    @Value("${base.doc.path}")
    private String baseDocPath;

    @Value("${cylinder.qr.path}")
    private String cylinderCodeQRPath;


    @Override
    public String generateAndSaveQRCode(Long userId, String code) throws BadRequestException {
        log.info("QRCodeServiceImpl :: generateQRCode >>> code : {} ", code);
        String qrCodeText = AESUtil.createCylinderCodeJsonEncryption(code);
        String filePath = baseDocPath + userId +  cylinderCodeQRPath + "/CYL_" + code + ".jpeg";
        int size = 300;
        String fileType = "jpeg";
        File qrFile = new File(filePath);
        try {
            createQRImage(qrFile, qrCodeText, size, fileType);
        } catch (IOException ioException) {
            log.error(Arrays.toString(ioException.getStackTrace()));
            throw new BadRequestException("Unable to save QR Code in Storage");
        } catch (WriterException writerException) {
            log.error(Arrays.toString(writerException.getStackTrace()));
            throw new BadRequestException("Unable to generate QR Code");
        }
        return filePath;
    }

    private static void createQRImage(File qrFile, String qrCodeText, int size, String fileType)
            throws WriterException, IOException {
        HashMap<EncodeHintType, ErrorCorrectionLevel> hintMap = new HashMap<>();
        hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix byteMatrix = qrCodeWriter.encode(qrCodeText, BarcodeFormat.QR_CODE, size, size, hintMap);
        int matrixWidth = byteMatrix.getWidth();
        BufferedImage image = new BufferedImage(matrixWidth, matrixWidth, BufferedImage.TYPE_INT_RGB);
        image.createGraphics();

        Graphics2D graphics = (Graphics2D) image.getGraphics();
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, matrixWidth, matrixWidth);
        // Paint and save the image using the ByteMatrix
        graphics.setColor(Color.BLACK);

        for (int i = 0; i < matrixWidth; i++) {
            for (int j = 0; j < matrixWidth; j++) {
                if (byteMatrix.get(i, j)) {
                    graphics.fillRect(i, j, 1, 1);
                }
            }
        }
        if (!qrFile.getParentFile().exists()) {
            qrFile.getParentFile().mkdirs();
        }
        ImageIO.write(image, fileType, qrFile);
    }
}
