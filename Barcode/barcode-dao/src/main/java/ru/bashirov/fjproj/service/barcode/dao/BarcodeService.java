package ru.bashirov.fjproj.service.barcode.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface BarcodeService {
    public Connection getConnection() throws SQLException;
    public void addBarcode(Connection con, int type, int id, String data, int method) throws SQLException;
    public List<String> getInfoByPassportId(Connection con, int passportId) throws SQLException;
    public List<String> getInfoByBuildingId(Connection con, int buildingId) throws SQLException;
    public void addUserVisit(Connection con, int userId) throws SQLException;
}
