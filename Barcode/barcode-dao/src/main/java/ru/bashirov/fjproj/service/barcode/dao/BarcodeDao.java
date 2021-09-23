package ru.bashirov.fjproj.service.barcode.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import oracle.jdbc.OracleTypes;
import ru.bashirov.fjproj.service.barcode.Constants;
import ru.bashirov.fjproj.service.barcode.Util;

public class BarcodeDao implements BarcodeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BarcodeDao.class);

    @Override
    public Connection getConnection() throws SQLException {
    	Connection con = DriverManager.getConnection(
                Constants.URL, Constants.USER, Constants.PASSWORD);
        con.setAutoCommit(false);
        return con;
    }

    @Override
    public void addBarcode(Connection con, int type, int id, String data, int method) throws SQLException {
        CallableStatement st = null;
        try {
            st = con.prepareCall("call fjproj.add_barcode(?, ?, ?, ?)");
            st.setInt(1, type);
            st.setInt(2, id);
            st.setString(3, data);
            st.setInt(4, method);
            st.executeQuery();

            LOGGER.debug("added barcode with type {} and id {}", type, id);
        } finally {
            Util.closeSt(st);
        }
    }

    @Override
    public void addUserVisit(Connection con, int userId) throws SQLException {
        CallableStatement st = null;
        try {
            st = con.prepareCall("call fjproj.add_user_visit(?, ?)");
            st.setInt(1, userId);
            st.setString(2, Util.getTimestamp());
            st.executeQuery();

            LOGGER.debug("added user getting barcode with userId {}", userId);
        } finally {
            Util.closeSt(st);
        }
    }

    @Override
    public List<String> getInfoByPassportId(Connection con, int passportId) throws SQLException {
        CallableStatement st = null;
        ResultSet rs = null;
        try {
            List<String> info = new ArrayList<String>(2);
            st = con.prepareCall("call fjproj.get_info_by_passport_id");
            st.registerOutParameter(1, OracleTypes.CURSOR);
            st.setInt(2, passportId);
            st.executeQuery();
            rs = (ResultSet) st.getObject(1);

            if (rs.next()) {
                info.add(rs.getString("inv_number"));
                info.add(rs.getString("net_number"));
            }

            LOGGER.debug("got additional info for passport_id {}", passportId);
            return info;
        } finally {
            Util.closeRsAndSt(rs, st);
        }
    }

    @Override
    public List<String> getInfoByBuildingId(Connection con, int buildingId) throws SQLException {
        CallableStatement st = null;
        ResultSet rs = null;
        try {
            List<String> info = new ArrayList<String>(2);
            st = con.prepareCall("call fjproj.get_info_by_building_id(?, ?)");
            st.registerOutParameter(1, OracleTypes.CURSOR);
            st.setInt(2, buildingId);
            st.executeQuery();
            rs = (ResultSet) st.getObject(1);

            if (rs.next()) {
                info.add(rs.getString("inv_number"));
                info.add(rs.getString("net_number"));
            }

            LOGGER.debug("got additional info for passport_id {}", buildingId);
            return info;
        } finally {
            Util.closeRsAndSt(rs, st);
        }
    }
}
