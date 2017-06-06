package com.truthso.ip360.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/6/6.
 */

public class NotarCity {
    private List<City> city;
    private List<Notary> notary;
    private List<Province> province;

    public List<City> getCity() {
        return city;
    }

    public void setCity(List<City> city) {
        this.city = city;
    }

    public List<Notary> getNotary() {
        return notary;
    }

    public void setNotary(List<Notary> notary) {
        this.notary = notary;
    }

    public List<Province> getProvince() {
        return province;
    }

    public void setProvince(List<Province> province) {
        this.province = province;
    }
}
