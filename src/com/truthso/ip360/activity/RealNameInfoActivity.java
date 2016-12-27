package com.truthso.ip360.activity;

import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

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
    private GoogleApiClient client;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) and run LayoutCreator again
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("RealNameInfo Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}
