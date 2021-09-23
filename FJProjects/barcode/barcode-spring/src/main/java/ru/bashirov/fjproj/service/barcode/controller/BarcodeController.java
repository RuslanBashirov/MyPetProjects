package ru.bashirov.fjproj.service.barcode.controller;

import java.sql.Connection;

import java.sql.SQLException;
import java.util.Base64;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import ru.bashirov.fjproj.service.BarcodeGenerator;
import ru.bashirov.fjproj.service.barcode.Util;
import ru.bashirov.fjproj.service.barcode.dao.BarcodeDao;
import ru.bashirov.fjproj.service.barcode.dao.BarcodeService;
import ru.bashirov.fjproj.service.barcode.entity.Asset;

@RestController
@RequestMapping("/api")
public class BarcodeController {

    private static final Logger LOGGER = LoggerFactory.getLogger(BarcodeController.class);

    private BarcodeService dao;

    public BarcodeController() throws SQLException {
        this.dao = new BarcodeDao();
    }

    @GetMapping("/getBarcodeImage")
    public @ResponseBody ResponseEntity<byte[]> getBarcodeImage(@RequestParam int type, @RequestParam int id,
            @RequestParam int method, @RequestParam int userId) {
        LOGGER.trace("start");
        ResponseEntity<byte[]> image = null;
        try {
            byte[] barcodeBytes = processGetBarcodeBytes(type, id, method, userId);
            image = ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(barcodeBytes);
        } catch (SQLException sql) {
            LOGGER.error("SQLException while closing connection");
        } finally {
            LOGGER.trace("stop");
        }
        return image;
    }

    @GetMapping("/getBarcodeBase64")
    public String getBarcode(@RequestParam int type, @RequestParam int id, @RequestParam int method,
            @RequestParam int userId) {
        LOGGER.trace("start");
        String base64 = null;
        try {
            byte[] barcodeBytes = processGetBarcodeBytes(type, id, method, userId);
            base64 = Base64.getEncoder().encodeToString(barcodeBytes);
        } catch (SQLException sql) {
            LOGGER.error("SQLException while closing connection");
        } finally {
            LOGGER.trace("stop");
        }
        return base64;
    }

    @GetMapping("/addBarcode")
    public void addBarcode(@RequestParam int type, @RequestParam int id, @RequestParam int method,
            @RequestParam String data) {
        LOGGER.trace("start");
        try {
            processAddBarcode(type, id, data, method);
        } catch (SQLException sql) {
            LOGGER.error("SQLException while closing connection");
        } finally {
            LOGGER.trace("stop");
        }
    }

    private byte[] processGetBarcodeBytes(int type, int id, int method, int userId) throws SQLException {
        LOGGER.trace("start");
        Connection con = null;
        byte[] barcode = null;
        try {
            con = dao.getConnection();
            List<String> info;

            if (type == 1) {
                info = dao.getInfoByPassportId(con, id); // 
            } else if (type == 2) {
                info = dao.getInfoByBuildingId(con, id); // 
            } else {
                LOGGER.error("wrong input type parameter: type = {}", type);
                return null;
            }

            Asset asset = new Asset(type, id, info.get(0), info.get(1));
            if (method == 1) {
                barcode = BarcodeGenerator.getQrCodeImage(asset.getJson(), 160, 160);
            } else if (method == 2) {
                barcode = BarcodeGenerator.getDataMatrixImage(asset.getJson(), 160, 160);
            } else if (method == 3) {
                barcode = BarcodeGenerator.getAztecImage(asset.getJson(), 160, 160);
            } else {
                LOGGER.error("wrong input method parameter: method = {}", method);
            }

            dao.addUserVisit(con, userId);
        } catch (SQLException sql) {
            LOGGER.error("SQLException", sql);
        } catch (Throwable t) {
            LOGGER.error("Unknown exception", t);
        } finally {
            Util.closeCon(con);
            LOGGER.trace("stop");
        }
        return barcode;
    }

    private void processAddBarcode(int type, int id, String data, int method) throws SQLException {
        LOGGER.trace("start");
        Connection con = null;
        try {
            con = dao.getConnection();
            dao.addBarcode(con, type, id, data, method);
        } catch (SQLException sql) {
            LOGGER.error("SQLException ", sql);
        } finally {
        	Util.closeCon(con);
            LOGGER.trace("stop");
        }
    }
}
