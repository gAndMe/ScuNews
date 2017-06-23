package com.scu.news;

/**
 * ListView 页面逻辑 (新闻列表页面)
 * 主要处理 新闻/通知页面 列表 下拉 点击等
 */

import java.util.ArrayList;
import java.util.List;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.scu.model.ScuNoticesBean;
import com.scu.service.NoticeTitleService;
import com.scu.utils.Uris.NoticeType;
import com.scu.adapters.AdapterNotices;
import com.scu.config.R;

public class ScuNoticeActivity extends BasicActivity {
	private int noticeType;	//新闻1 通知2
	private TextView textViewTitle;
	
	private ListView listViewNotices;
	private List<ScuNoticesBean> listNotices;
	private int listNoticePageNow = 1;
	private AdapterNotices adapterNews;
	
	
	@Override
	public int getContentViewId() {      
        return R.layout.activity_notice;
	}
		
	@Override
	public void doAfterCreate() {
		super.doAfterCreate();

        this.getParamsFromPrevious();
        
		this.setListView();  
	}

	
	private void getParamsFromPrevious(){
		Intent intent = this.getIntent();
		
		//从MainActivity中的intent.putExtra("NoticeType", NoticeType.NEWS)得到noticeType
		noticeType = intent.getIntExtra("NoticeType", NoticeType.NEWS);
		
        textViewTitle = (TextView) this.findViewById(R.id.titletext);
        
        //activity_notice.xml 里默认设置的是 学院新闻，这里验证传参是2的话，设置标题为 学院通知
		if(noticeType == NoticeType.NOTIFICATION) textViewTitle.setText("通知公告");		
	}
    
    private RelativeLayout layoutViewFootProgressBar; 
    private boolean isLoadFinish = false;
	@SuppressLint("ServiceCast") 
	private void setListView() {
		//设置列表
		listViewNotices = (ListView) this.findViewById(R.id.listview);
		listNotices = new ArrayList<ScuNoticesBean>();
		adapterNews = new AdapterNotices(ScuNoticeActivity.this, listNotices);
		
		/*android的后台运行在很多service，它们在系统启动时被SystemServer开启，支持系统的正常工作
		getSystemService是Android很重要的一个API，它是Activity的一个方法，
		根据传入的NAME来取得对应的Object，然后转换成相应的服务对象
		
		LAYOUT_INFLATER_SERVICE  返回对象LayoutInflater  取得xml里定义的view*/
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		//返回相应资源ID所代表的View，在运行过程中动态加载
		layoutViewFootProgressBar = (RelativeLayout) inflater.inflate(R.layout.item_listview_foot_progressbar, null);
		// 添加页脚进度条
		listViewNotices.addFooterView(layoutViewFootProgressBar);
		listViewNotices.setAdapter(adapterNews);
		listViewNotices.removeFooterView(layoutViewFootProgressBar);
		
		//设置item点击监听
		listViewNotices.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> listView, View itemView, int index, long id) {
				if(isLoadFinish){
					Intent intent = new Intent();
					
					//跳转到NoticeDetailActivity
					intent.setClass(ScuNoticeActivity.this, NoticeDetailActivity.class);
					//putExtra()用来传递参数，那样就可以在界面跳转的时候将一些值传递到你跳转过去的界面，
					intent.putExtra("windowTitle", textViewTitle.getText().toString()); //windowTitle标签 后面NoticeDetailActivity要用
					//noticeUrl 存放  noticeContentLink(详细内容网址链接),跳转到后面NoticeDetailActivity接收
					intent.putExtra("noticeUrl", listNotices.get(index).noticeContentLink);
					
					startActivity(intent);
				}
			}			
		});
		
		//设置滑动监听
		listViewNotices.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) { }
			
			@Override
			public void onScroll(AbsListView listView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				int lastItemIndex = listView.getLastVisiblePosition();//当前屏幕最后一条记录ID
				if(lastItemIndex + 1 == totalItemCount && isLoadFinish){//判断往下是否达到数据最后一条记录
					//锁定加载完成标志
					isLoadFinish = false;
					//根据页码获取最新数据
					try {
						addDataAtBackground();
					} catch (Exception e) {
						e.printStackTrace();
					} 
				}
			}
		});
		
		this.showProgressDialog(this, "请稍候", "正在加载数据...");
		this.addDataAtBackground();		
	}

	
	private void addDataAtBackground(){ //后台处理 添加数据
		listViewNotices.addFooterView(layoutViewFootProgressBar);
		new Thread(new Runnable() {			
			@Override
			public void run() {
				try {
					List<ScuNoticesBean> newNoticesList = NoticeTitleService.getNewsList(listNoticePageNow, noticeType);
					
					if(newNoticesList != null && newNoticesList.size() > 0){
						listNotices.addAll(newNoticesList);					
						mHandler.sendEmptyMessage(1);
					}else{
						mHandler.sendEmptyMessage(2);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	@SuppressLint({ "HandlerLeak", "ShowToast" }) 
	private Handler mHandler = new Handler(){		
		@Override
		public void handleMessage(Message msg) {//Android消息处理机制,的主要功能是进行消息的封装，并且同时可以指定消息的操作形式。
			switch(msg.what){
			case 1:
				listNoticePageNow++;
				adapterNews.notifyDataSetChanged();
				isLoadFinish = true;
				progressDialog.cancel();
				break;
			case 2:
				Toast.makeText(ScuNoticeActivity.this, "后面没有数据了", 1).show();
				break;
			}
			listViewNotices.removeFooterView(layoutViewFootProgressBar);
		}		
	};	
}
