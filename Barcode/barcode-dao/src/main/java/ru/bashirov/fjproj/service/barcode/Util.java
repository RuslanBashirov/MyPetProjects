package ru.bashirov.fjproj.service.barcode;

import java.sql.Connection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class Util {
    
    public static String getTimestamp() {
        String YYYYMMDD_HHMMSS = "yyyy-MM-dd HH-mm-ss";
        DateTimeFormatter yyyyMMddHHmmssFormatter = DateTimeFormat.forPattern(YYYYMMDD_HHMMSS); 
        String timestamp = new LocalDateTime().toString(yyyyMMddHHmmssFormatter);
        
        return timestamp;
    }
    
    public static void closeCon(Connection con) throws SQLException{
        if (con != null) {
            con.close();
        }
    }
    
    public static void closeRsAndSt(ResultSet rs, Statement st) throws SQLException{
        if (rs != null) {
            rs.close();
        }
        
        if (st != null) {
            st.close();
        }
    }
    
    public static void closeRs(ResultSet rs) throws SQLException{
        if (rs != null) {
            rs.close();
        }
    }
    
    public static void closeSt(Statement st) throws SQLException{
        if (st != null) {
            st.close();
        }
    }

}
