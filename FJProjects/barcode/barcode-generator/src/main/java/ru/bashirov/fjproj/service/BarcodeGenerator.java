package ru.bashirov.fjproj.service;

import java.io.ByteArrayOutputStream;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Writer;
import com.google.zxing.WriterException;
import com.google.zxing.aztec.AztecWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.datamatrix.DataMatrixWriter;
import com.google.zxing.qrcode.QRCodeWriter;

public class BarcodeGenerator {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(BarcodeGenerator.class);
    
    public static byte[] getQrCodeImage(String text, int width, int height) throws WriterException, IOException {
        Writer writer = new QRCodeWriter();
        byte[] image = getImage(text, width, height, writer, BarcodeFormat.QR_CODE);
        return image;
    }

    public static byte[] getDataMatrixImage(String text, int width, int height) throws WriterException, IOException {
        Writer writer = new DataMatrixWriter();
        return getImage(text, width, height, writer, BarcodeFormat.DATA_MATRIX);
    }

    public static byte[] getAztecImage(String text, int width, int height) throws WriterException, IOException {
        Writer writer = new AztecWriter();
        return getImage(text, width, height, writer, BarcodeFormat.AZTEC);
    }

    private static byte[] getImage(String text, int width, int height, Writer writer, BarcodeFormat format)
            throws WriterException, IOException {
        BitMatrix bitMatrix = writer.encode(text, format, width, height); 
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", baos);
        byte[] pngData = baos.toByteArray();
        LOGGER.debug("created {} image with text: {}", format, text);
        return pngData;
    }

}