package com.truthso.ip360.bean;

/**
 * @despriction : 赠送量
 *
 * @author wsx_summer Email:wangshaoxia@truthso.com
 * @date 创建时间：2017/3/31 11:08
 * @version 1.1
 * @Copyright (c) 2016 真相网络科技（北京）.Co.Ltd. All rights reserved.
 */
public class GiftsProduct {
    private int type;//业务类型
    private String unit;//数量单位 业务量的单位。如次、分钟、周。
    private int giftsCount;//赠送量
    private int usedCount;//累计使用量
    private int balanceCount;//剩余量freeCount-usedCount。负值时为透支。
    private String contractStart;//合同开始时间
    private String contractEnd;//合同结束时间

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public int getGiftsCount() {
        return giftsCount;
    }

    public void setGiftsCount(int giftsCount) {
        this.giftsCount = giftsCount;
    }

    public int getUsedCount() {
        return usedCount;
    }

    public void setUsedCount(int usedCount) {
        this.usedCount = usedCount;
    }

    public int getBalanceCount() {
        return balanceCount;
    }

    public void setBalanceCount(int balanceCount) {
        this.balanceCount = balanceCount;
    }

    public String getContractStart() {
        return contractStart;
    }

    public void setContractStart(String contractStart) {
        this.contractStart = contractStart;
    }

    public String getContractEnd() {
        return contractEnd;
    }

    public void setContractEnd(String contractEnd) {
        this.contractEnd = contractEnd;
    }
}
