package com.truthso.ip360.view;


import android.app.Service;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.truthso.ip360.activity.R;

	public class MainActionBar extends LinearLayout {
		private Context context;
		private Button acition_bar_left;
		private Button acition_bar_right;
		private TextView acition_bar_title;
		private TextView acition_bar_msgnum;
		private RelativeLayout layout_container;
		private View layout;

		public MainActionBar(Context context, AttributeSet attrs) {
			super(context, attrs);
			this.context = context;
			initView();
		}

		public MainActionBar(Context context) {
			super(context);
			this.context = context;
			initView();
		}
		public void setActionBarOnClickListener(View.OnClickListener listener) {
			acition_bar_title.setOnClickListener(listener);
			acition_bar_left.setOnClickListener(listener);
			acition_bar_right.setOnClickListener(listener);
		}

		private void initView() {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Service.LAYOUT_INFLATER_SERVICE);
			layout = inflater.inflate(R.layout.layout_action_bar, this);
			acition_bar_left = (Button) layout.findViewById(R.id.acition_bar_left);// 返回
			layout_container = (RelativeLayout) layout.findViewById(R.id.layout_container);
			acition_bar_right = (Button) layout.findViewById(R.id.acition_bar_right);// 分享
			acition_bar_title = (TextView) layout.findViewById(R.id.acition_bar_title);// 标题�?
			acition_bar_msgnum = (TextView) layout.findViewById(R.id.acition_bar_msgnum);// 标题�?
		}


		public void setAcition_bar_msgnumGone() {
			acition_bar_msgnum.setVisibility(View.GONE);
		}

		public void setAcition_bar_msgnumVisible() {
			acition_bar_msgnum.setVisibility(View.VISIBLE);
		}

		public void setMsgNum(String num) {
			acition_bar_msgnum.setText(num);
		}

		

		public void setActionBarBG(int drable) {
			layout_container.setBackgroundResource(drable);
		}

		public void setTitle(String title) {
			acition_bar_title.setText(title);
		}

		public void setTitleBackground(int resourceId) {
			acition_bar_title.setBackgroundDrawable(getResources().getDrawable(resourceId));
		}

		public void setLeftGone() {
			acition_bar_left.setVisibility(View.GONE);
		}
		

		public void setLeftVisible() {
			acition_bar_left.setVisibility(View.VISIBLE);
		}

		public void setRightGone() {
			acition_bar_right.setVisibility(View.GONE);
		}
		public void setRightText(String text) {
			acition_bar_right.setText(text);
		}

	public void setRightVisible() {
		acition_bar_right.setVisibility(View.VISIBLE);
	}
	public void setRightEnable() {
		acition_bar_right.setEnabled(true);
	}
	public void setRightDisEnable() {
		acition_bar_right.setEnabled(false);
	}

	public void setLeftIcon(int resourceId) {
		//acition_bar_left.setImageResource(resourceId);
		acition_bar_left.setBackgroundDrawable(getResources().getDrawable(resourceId));
	}

	public void setLeftText(String  text) {
		//acition_bar_left.setImageResource(resourceId);
		acition_bar_left.setText(text);
	}
	public void setRightIcon(int resourceId) {
		acition_bar_right.setBackgroundDrawable(getResources().getDrawable(resourceId));
	}
	public void setRightOnClick(boolean bool) {
		if (bool) {
			acition_bar_right.setEnabled(true);
		} else {
			acition_bar_right.setEnabled(false);
		}
	}

}
