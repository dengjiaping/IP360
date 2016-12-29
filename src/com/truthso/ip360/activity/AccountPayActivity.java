package com.truthso.ip360.activity;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.alipay.sdk.app.PayTask;
import com.lidroid.xutils.db.annotation.Check;
import com.truthso.ip360.bean.ZfbPayBean;
import com.truthso.ip360.net.ApiCallback;
import com.truthso.ip360.net.ApiManager;
import com.truthso.ip360.net.BaseHttpResponse;
import com.truthso.ip360.system.Toaster;
import com.truthso.ip360.utils.CheckUtil;
import com.truthso.ip360.utils.PayResult;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;

/**
 * @despriction :个人中心->账户充值
 * 
 * @author wsx_summer Email:wangshaoxia@truthso.com
 * @date 创建时间：2016-7-22下午8:22:12
 * @version 1.0
 * @Copyright (c) 2016 真相网络科技（北京）.Co.Ltd. All rights reserved.
 */

public class AccountPayActivity extends BaseActivity implements View.OnClickListener {
	private String accountBalance,money;//账户余额
	private static final int SDK_PAY_FLAG = 1;
	private static final int SDK_AUTH_FLAG = 2;
	private EditText et_chongzhi_jine;
	private Button btn_sure;
	@Override
	public void initData() {
		accountBalance = getIntent().getStringExtra("accountBalance");
	}
	@Override
	public void initView() {
		btn_sure= (Button) findViewById(R.id.btn_sure);
		btn_sure.setOnClickListener(this);

		et_chongzhi_jine= (EditText) findViewById(R.id.et_chongzhi_jine);

	}
	@Override
	public int setLayout() {
		return R.layout.activity_account_pay;
	}

	@Override
	public String setTitle() {
		return "账户充值";
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.btn_sure:
				money= et_chongzhi_jine.getText().toString().trim();
				if (!CheckUtil.isEmpty(money)){
					if (CheckUtil.isFormatAccount(money)){
						payment();
					}else{
						Toaster.showToast(AccountPayActivity.this,"请输入正确的充值金额！");
					}
				}else{
					Toaster.showToast(AccountPayActivity.this,"请输入充值金额！");
				}


				break;
		}
	}
	private void payment() {
		ApiManager.getInstance().getOrderInfo(money, new ApiCallback() {
			@Override
			public void onApiResult(int errorCode, String message, BaseHttpResponse response) {
				ZfbPayBean bean= (ZfbPayBean) response;
				if(bean.getCode()==200){
				String orderInfo=bean.getDatas().getText();
					if(orderInfo!=null){
						payV2(orderInfo);
					}
				}else{
					Toast.makeText(AccountPayActivity.this, "获取数据失败", Toast.LENGTH_SHORT).show();
				}

			}

			@Override
			public void onApiResultFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
				Toast.makeText(AccountPayActivity.this, "获取数据失败", Toast.LENGTH_SHORT).show();
			}
		});


	}

	/**
	 * 支付宝支付业务
	 *
	 * @param
	 */
	public void payV2(final String orderInfo) {

		/*if (TextUtils.isEmpty(APPID) || TextUtils.isEmpty(RSA_PRIVATE)) {
			new AlertDialog.Builder(this).setTitle("警告").setMessage("需要配置APPID | RSA_PRIVATE")
					.setPositiveButton("确定", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialoginterface, int i) {
							//
							finish();
						}
					}).show();
			return;
		}*/

		/**
		 * 这里只是为了方便直接向商户展示支付宝的整个支付流程；所以Demo中加签过程直接放在客户端完成；
		 * 真实App里，privateKey等数据严禁放在客户端，加签过程务必要放在服务端完成；
		 * 防止商户私密数据泄露，造成不必要的资金损失，及面临各种安全风险；
		 *
		 * orderInfo的获取必须来自服务端；
		 */
		/*Map<String, String> params = OrderInfoUtil2_0.buildOrderParamMap(APPID);
		String orderParam = OrderInfoUtil2_0.buildOrderParam(params);
		String sign = OrderInfoUtil2_0.getSign(params, RSA_PRIVATE);
		final String orderInfo = orderParam + "&" + sign;*/

		Runnable payRunnable = new Runnable() {

			@Override
			public void run() {
				PayTask alipay = new PayTask(AccountPayActivity.this);
				Log.i("msp","orderInfo2:"+orderInfo);
				Map<String, String> result = alipay.payV2(orderInfo, true);
				Log.i("msp", result.toString());

				Message msg = new Message();
				msg.what = SDK_PAY_FLAG;
				msg.obj = result;
				mHandler.sendMessage(msg);
			}
		};

		Thread payThread = new Thread(payRunnable);
		payThread.start();
	}


	private Handler mHandler = new Handler() {
		@SuppressWarnings("unused")
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case SDK_PAY_FLAG: {
					@SuppressWarnings("unchecked")
					PayResult payResult = new PayResult((Map<String, String>) msg.obj);
					/**
					 对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
					 */
					String resultInfo = payResult.getResult();// 同步返回需要验证的信息
					String resultStatus = payResult.getResultStatus();
					// 判断resultStatus 为9000则代表支付成功
					if (TextUtils.equals(resultStatus, "9000")) {
						// 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
						Toast.makeText(AccountPayActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
					} else {
						// 该笔订单真实的支付结果，需要依赖服务端的异步通知。
						Toast.makeText(AccountPayActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
					}
					break;
				}
				/*case SDK_AUTH_FLAG: {
					@SuppressWarnings("unchecked")
					AuthResult authResult = new AuthResult((Map<String, String>) msg.obj, true);
					String resultStatus = authResult.getResultStatus();

					// 判断resultStatus 为“9000”且result_code
					// 为“200”则代表授权成功，具体状态码代表含义可参考授权接口文档
					if (TextUtils.equals(resultStatus, "9000") && TextUtils.equals(authResult.getResultCode(), "200")) {
						// 获取alipay_open_id，调支付时作为参数extern_token 的value
						// 传入，则支付账户为该授权账户
						Toast.makeText(PayDemoActivity.this,
								"授权成功\n" + String.format("authCode:%s", authResult.getAuthCode()), Toast.LENGTH_SHORT)
								.show();
					} else {
						// 其他状态值则为授权失败
						Toast.makeText(PayDemoActivity.this,
								"授权失败" + String.format("authCode:%s", authResult.getAuthCode()), Toast.LENGTH_SHORT).show();

					}
					break;
				}*/
				default:
					break;
			}
		};
	};
}
