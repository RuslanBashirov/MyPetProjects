package ru.bashirov.fjproj.service.barcode.entity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Asset {
    
    private int type;
    private int id;
    private String invNumber;
    private String netNumber;
    
    public Asset(int type, int id, String invNumber, String netNumber) {
        super();
        this.type = type;
        this.id = id;
        this.invNumber = invNumber;
        this.netNumber = netNumber;
    }
    
    public int getType() {
        return type;
    }
    public void setType(int type) {
        this.type = type;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getInvNumber() {
        return invNumber;
    }
    public void setInvNumber(String invNumber) {
        this.invNumber = invNumber;
    }
    public String getNetNumber() {
        return netNumber;
    }
    public void setNetNumber(String netNumber) {
        this.netNumber = netNumber;
    }

    @Override
    public String toString() {
        return "Asset [type=" + type + ", id=" + id + ", invNumber=" + invNumber + ", netNumber=" + netNumber + "]";
    }
    
    public String getJson() {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        String json = gson.toJson(this);
        return json;
    }
}
