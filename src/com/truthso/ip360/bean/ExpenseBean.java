package com.truthso.ip360.bean;

import com.truthso.ip360.net.BaseHttpResponse;

/**
 * 扣费
 * Created by summer on 2016/12/12.
 */

public class ExpenseBean extends BaseHttpResponse{
    private  Expense datas;

    public Expense getDatas() {
        return datas;
    }

    public void setDatas(Expense datas) {
        this.datas = datas;
    }

    public class Expense {
        private int status;//0-正常付款；1-欠费

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }
    }
}
