package com.truthso.ip360.bean;

import com.truthso.ip360.net.BaseHttpResponse;
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

    public class  NotarCity{
        private String provinceName;
        private int provinceCode;
        private String cityName;
        private String cityCode;
        private String notaryName;

        public String getProvinceName() {
            return provinceName;
        }

        public void setProvinceName(String provinceName) {
            this.provinceName = provinceName;
        }

        public int getProvinceCode() {
            return provinceCode;
        }

        public void setProvinceCode(int provinceCode) {
            this.provinceCode = provinceCode;
        }

        public String getCityName() {
            return cityName;
        }

        public void setCityName(String cityName) {
            this.cityName = cityName;
        }

        public String getCityCode() {
            return cityCode;
        }

        public void setCityCode(String cityCode) {
            this.cityCode = cityCode;
        }

        public String getNotaryName() {
            return notaryName;
        }

        public void setNotaryName(String notaryName) {
            this.notaryName = notaryName;
        }
    }
}
