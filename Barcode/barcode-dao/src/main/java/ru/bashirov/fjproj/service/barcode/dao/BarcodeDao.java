package ru.bashirov.fjproj.service.barcode.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.bashirov.fjproj.service.barcode.Constants;
import ru.bashirov.fjproj.service.barcode.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
            st = con.prepareCall("insert into barcode\n" +
                    "(type, id, data, method)\n" +
                    "values(?, ?, ?, ?)");
            st.setInt(1, type);
            st.setInt(2, id);
            st.setString(3, data);
            st.setInt(4, method);
            st.executeUpdate();
            con.commit();

            LOGGER.debug("added barcode with type {} and id {}", type, id);
        } finally {
            Util.closeSt(st);
        }
    }

    @Override
    public void addUserVisit(Connection con, int userId) throws SQLException {
        CallableStatement st = null;
        try {
            st = con.prepareCall("insert into barcode_auth_history\n" +
                    "(user_id, auth_date)\n" +
                    "values(?, to_timestamp(?,'yyyy-mm-dd hh24-mi-ss'))");
            st.setInt(1, userId);
            st.setString(2, Util.getTimestamp());
            st.executeUpdate();

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
            st = con.prepareCall("select invnumber,net_number from iob_passport where passport_id=?");
            st.setInt(1, passportId);
            st.execute();
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
        String data = null;
        try {
            List<String> info = new ArrayList<>(1);
            st = con.prepareCall("SELECT data FROM barcode WHERE id=?");
            st.setInt(1, buildingId);
            rs = st.executeQuery();

            if (rs.next()) {
                data = rs.getString("data");
            }

            info.add(data);
            info.add("123");

            LOGGER.debug("got data {} for id = {}", data, buildingId);
            return info;
        } finally {
            Util.closeRsAndSt(rs, st);
        }
    }
}
