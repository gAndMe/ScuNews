package com.scu.news;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.RelativeLayout;

import com.scu.config.R;

public abstract class BasicActivity extends Activity {
	private RelativeLayout layoutGoback;
	public ProgressDialog progressDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
        //强制屏幕竖屏显示
  		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
  		//去掉系统标题栏
  		requestWindowFeature(Window.FEATURE_NO_TITLE);
  		setContentView(getContentViewId());
  		doAfterCreate();
	}

	public abstract int getContentViewId();
	public void doAfterCreate(){
		//返回按钮
        layoutGoback = (RelativeLayout) this.findViewById(R.id.backtxt_btndiy);
        layoutGoback.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
	}

	public void showProgressDialog(Context context, String title, String message) {
		progressDialog = new ProgressDialog(context, R.style.dialog);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressDialog.setIcon(android.R.drawable.ic_dialog_info);
		progressDialog.setTitle(title);
		progressDialog.setMessage(message);
	    progressDialog.show();
	}
	//获取屏幕分辨率及相关控件高度
	public int getScreenWidth() {
		// 屏幕分辨率
		DisplayMetrics mDisplayMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
		return mDisplayMetrics.widthPixels;
	}
}
