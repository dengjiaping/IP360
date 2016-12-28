package com.truthso.ip360.activity;

import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;


/**
 * Created by summer on 2016/12/24.
 */

public class RealNameInfoActivity extends BaseActivity {

    private TextView tv_realname;
    private TextView tv_idcard;
    private String realName,cardId;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */

    @Override
    public void initData() {

    }

    @Override
    public void initView() {
        realName = getIntent().getStringExtra("realName");
        cardId = getIntent().getStringExtra("cardId");
        StringBuffer sb=new StringBuffer();
        for (int a=0;a<realName.length()-1;a++){
            sb.append("*");
        }
        sb.append( realName.charAt(realName.length()-1));
        String realNameFormat=sb.toString();
//        String strRealName = realName.substring(realName.indexOf(realName.charAt(0)),realName.length()-1);
//        String realNameFormat = realName.replace(strRealName,"*");
        String  cardIdFormat = cardId.replace(cardId.substring(cardId.length()-14,cardId.length()-4),"**********");
        tv_realname = (TextView) findViewById(R.id.tv_realname);
        tv_realname.setText(realNameFormat);
        tv_idcard = (TextView) findViewById(R.id.tv_idcard);
        tv_idcard.setText(cardIdFormat);
    }

    @Override
    public int setLayout() {
        return R.layout.activity_aleady_realname;
    }

    @Override
    public String setTitle() {
        return "实名认证";
    }

}
