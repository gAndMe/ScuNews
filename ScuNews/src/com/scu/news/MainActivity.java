package com.scu.news;

/**
 * 
 * 开始界面的按钮事件
 */
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.scu.config.R;
import com.scu.utils.Uris.NoticeType;

public class MainActivity extends BasicActivity {
	private Button btnNews;		//新闻按钮
	private Button btnNotification;		//通知按钮
	
	@Override
	public int getContentViewId() {      
        return R.layout.activity_main;	//主页
	}
	
	@Override
	public void doAfterCreate() {		 
        this.findAndSetButtons();
	}
	
	//设置新闻和通知按钮的监听器
	private void findAndSetButtons() {
		btnNews = (Button) this.findViewById(R.id.btn_news);
		btnNotification = (Button) this.findViewById(R.id.btn_notification);
		
		btnNews.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				/*
				  每个应用程序都有若干个Activity组成，每一个Activity都是一个应用程序与用户进行交互的窗口，
				  呈现不同的交互界面。因为每一个Acticity的任务不一样，所以经常互在各个Activity之间进行跳转，
				  在Android中这个动作是靠Intent来完成的。
				  你通过startActivity()方法发送一个Intent给系统，
				  系统会根据这个Intent帮助你找到对应的Activity，即使这个Activity在其他的应用中，
				  也可以用这种方法启动它。
				 */
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, ScuNoticeActivity.class);
				/*
				 Intent可以携带的额外key-value数据，你可以通过调用putExtra()方法设置数据，
				 每一个key对应一个value数据。你也可以通过创建Bundle对象来存储所有数据，
				 然后通过调用putExtras()方法来设置数据。对于数据key的名字要尽量用包名做前缀，
				 然后再加上其他，这样来保证key的唯一性。
				 */
				//putExtra()用来传递参数，那样就可以在界面跳转的时候将一些值传递到你跳转过去的界面，
				intent.putExtra("NoticeType", NoticeType.NEWS);
				startActivity(intent);
			}
		});

		btnNotification.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, ScuNoticeActivity.class);
				intent.putExtra("NoticeType", NoticeType.NOTIFICATION);
				startActivity(intent);
			}
		});	
	}
}
