package com.pilock.pojo;

import java.util.Date;

public class DataVo {
    private int dataId;
    private String message;
    private String createdDate;
    private String deviceId;

    public DataVo(int dataId, String deviceId, String message, String createdDate) {
        this.dataId = dataId;
        this.message = message;
        this.createdDate = createdDate;
        this.deviceId = deviceId;
    }
    public DataVo(int dataId, String deviceId,String message, Date date) {
        this.dataId = dataId;
        this.message = message;
        this.createdDate = date.toString();
        this.deviceId = deviceId;
    }

    public DataVo(String message, String deviceId) {
        this.message = message;
        this.deviceId = deviceId;
    }

    public String toMessage(){
        String res = deviceId +" "+ message + " "+createdDate;
        System.out.println("to msg: "+res);
        return res;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }
}
