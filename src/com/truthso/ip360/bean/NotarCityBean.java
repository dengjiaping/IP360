package com.truthso.ip360.bean;

import com.truthso.ip360.net.BaseHttpResponse;

import java.util.List;

/**
 * @despriction :获取公证城市及名称
 *
 * @author wsx_summer Email:wangshaoxia@truthso.com
 * @date 创建时间：2017/6/1 11:42
 * @version 1.3
 * @Copyright (c) 2016 真相网络科技（北京）.Co.Ltd. All rights reserved.
 */
public class NotarCityBean extends BaseHttpResponse {
//    {"msg":"success","code":200,"datas":[{"cityName":"市辖区","provinceCode":"110000","cityCode":"110100","notaryName":"北京-长安公证处","provinceName":"北京市"}]}
    private NotarCity datas;

    public NotarCity getDatas() {
        return datas;
    }

    public void setDatas(NotarCity datas) {
        this.datas = datas;
    }

    public class  NotarCity {
//        private String provinceName;
//        private int provinceCode;
//        private String cityName;
//        private String cityCode;
//        private String notaryName;
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
    public class City{
//
//        "cityName": "市辖区",
//                "provinceCode": "110000",
//                "cityCode": "110100"
        private String cityName;
        private String provinceCode;
        private String cityCode;

        public String getCityName() {
            return cityName;
        }

        public void setCityName(String cityName) {
            this.cityName = cityName;
        }

        public String getProvinceCode() {
            return provinceCode;
        }

        public void setProvinceCode(String provinceCode) {
            this.provinceCode = provinceCode;
        }

        public String getCityCode() {
            return cityCode;
        }

        public void setCityCode(String cityCode) {
            this.cityCode = cityCode;
        }
    }
    public class Notary{
//        "cityCode": "110100",
//                "notaryId": 1,
//                "notaryName": "北京-长安公证处"
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
    public class Province{
//        "provinceCode": "110000",
//                "provinceName": "北京市"
        private String provinceCode;
        private String provinceName;

        public String getProvinceCode() {
            return provinceCode;
        }

        public void setProvinceCode(String provinceCode) {
            this.provinceCode = provinceCode;
        }

        public String getProvinceName() {
            return provinceName;
        }

        public void setProvinceName(String provinceName) {
            this.provinceName = provinceName;
        }
    }
}
