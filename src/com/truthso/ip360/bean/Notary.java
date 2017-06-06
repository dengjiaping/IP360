package com.truthso.ip360.bean;

/**
 * Created by Administrator on 2017/6/6.
 */

public class Notary {
    private String cityCode;
    private int notaryId;
    private String notaryName;

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public int getNotaryId() {
        return notaryId;
    }

    public void setNotaryId(int notaryId) {
        this.notaryId = notaryId;
    }

    public String getNotaryName() {
        return notaryName;
    }

    public void setNotaryName(String notaryName) {
        this.notaryName = notaryName;
    }
}
