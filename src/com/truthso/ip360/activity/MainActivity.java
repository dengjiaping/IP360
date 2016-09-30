package com.truthso.ip360.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;
import android.widget.RadioGroup;

import com.truthso.ip360.fragment.BaseFragment;
import com.truthso.ip360.fragment.CloudEvidence;
import com.truthso.ip360.fragment.HomeFragment;
import com.truthso.ip360.fragment.NativeEvidence;
import com.truthso.ip360.fragment.PersonalCenter;
import com.truthso.ip360.fragment.TransList;
import com.truthso.ip360.net.ApiCallback;
import com.truthso.ip360.net.ApiManager;
import com.truthso.ip360.net.BaseHttpResponse;
import com.truthso.ip360.utils.FragmentTabUtils;
import com.truthso.ip360.utils.FragmentTabUtils.OnRgsExtraCheckedChangedListener;


public class MainActivity extends FragmentActivity implements
OnRgsExtraCheckedChangedListener {
	private RadioGroup radioGroup;
	private SharedPreferences sp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 去除标题栏
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.activity_main);
		initView();
	}

	// 初始化控件
	private void initView() {
		List<Fragment> fragmentList = new ArrayList<Fragment>();
	
		fragmentList.add(new HomeFragment());// 首页（现在取证）
		 fragmentList.add(new CloudEvidence());// 云端证据
		 fragmentList.add(new NativeEvidence());//本地证据
		 fragmentList.add(new TransList());//传输列表
		 fragmentList.add(new PersonalCenter());//个人中心
		radioGroup = (RadioGroup) findViewById(R.id.main_RadioGroup);
		FragmentTabUtils fragmentTabUtils = new FragmentTabUtils(
				getSupportFragmentManager(), fragmentList, R.id.main_fragment,
				radioGroup);


		
	}

	@Override
	public void OnRgsExtraCheckedChanged(RadioGroup radioGroup, int checkedId,
			int index) {

	}

	public void replaceFragment(Fragment argFragment, String argName) {
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.main_fragment, argFragment, argName).commit();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		BaseFragment baseFragment = (BaseFragment) getSupportFragmentManager().findFragmentByTag("fragment");
		if(baseFragment.onKeyDown(keyCode, event)){
			return true;
		}else{
			return super.onKeyDown(keyCode, event);
		}
		
	}
	@Override
	protected void onSaveInstanceState(Bundle outState) {
//		super.onSaveInstanceState(outState);
	}
}