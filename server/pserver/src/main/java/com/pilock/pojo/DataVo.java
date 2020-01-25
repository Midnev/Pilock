package com.pilock.pojo;

public class DataVo {
    private String message;
    private String createdDate;
    private String deviceId;


    public DataVo(String message, String deviceId) {
        this.message = message;
        this.deviceId = deviceId;
    }

    public String toMessage(){

        return deviceId + message + createdDate;
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
