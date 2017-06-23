package com.scu.news;
/**
 * 主要是介绍新闻页面的布局以及内容的获得和显示
 * 
 */

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.scu.config.R;
import com.scu.service.NoticeDetailService;

public class NoticeDetailActivity extends BasicActivity {

	private TextView textViewTitle;
	private WebView webView;
	private ProgressBar webViewProgressbar;
	private String windowTitle = "";
	private String url = "";
	private String htmlString = "";

	@Override
	public int getContentViewId() {      
        return R.layout.activity_notice_detail;
	}
		
	@Override
	public void doAfterCreate() {
		super.doAfterCreate();
		
        //从前页得到url
        this.getParamsFromPrevious();
        
        textViewTitle = (TextView) this.findViewById(R.id.titletext);
        textViewTitle.setText(windowTitle);
        
        if(url != "")
        	this.convertHtmlData();   
	}


	private void getParamsFromPrevious(){
		Intent intent = this.getIntent();
		windowTitle = intent.getStringExtra("windowTitle");
		url = intent.getStringExtra("noticeUrl");	//得到新闻网页地址
	}

	private void convertHtmlData() {
		this.showProgressDialog(this, "请稍候", "正在加载数据...");
		new Thread(new Runnable() {			
			@Override
			public void run() {
				try {
					//根据新闻URL获取原始html数据,并格式化成新的html内容，供Web显示
					//调用控制层NoticeDetailService的getNewsDetailsHtml方法
					htmlString = NoticeDetailService.getNewsDetailsHtml(url);
					//Handler 的消息队列机制,Handler.sendMessage(new Message). 这个方法是指 Handler 在发送消息的时候，需要发送一个新的对象。
					/*
					 用过Handler的人都知道，Message有两种获取方法
					Message msg = new Message();
					或者
					Message msg = handler.obtainMessage();
					这两种方法的区别是，前者是new的，需要开辟内存空间；后者是从global Message pool中取，性能消耗相对少；
					 */
					mHandler.sendMessage(mHandler.obtainMessage(22, htmlString));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();			
	}

	@SuppressLint("HandlerLeak") 
	private Handler mHandler = new Handler(){		
		@Override
		public void handleMessage(Message msg) {
	        setWebViewContent();
		}
	};	
		
	@SuppressWarnings("deprecation")
	private void setWebViewContent() {//设置显示新闻/通知内容
		webView = (WebView) findViewById(R.id.webView); // 获得 新闻/通知 内容的组件
		webViewProgressbar = (ProgressBar) findViewById(R.id.webViewProgressbar);
		webViewProgressbar.setMax(100);
		
//		webView.loadUrl("file:///android_asset/example.html");

		/*
		 android提供了webView空间专门用来浏览网页。
		 在程序中装载webView控件，设置属性，比如：颜色、字体、要访问的网址等。
		 通过loadUrl方法设置当前webView需要访问的网址。
		 在创建webView时，系统有个一默认的设置，我们通过webView.getSettings来得到这个设置。

		 WebSettings常用方法： 
		 setAllowFileAccess 启用或禁止WebView访问文件数据
		 setBlockNetworkImage 是否显示网络图像
		 setBuiltInZoomControls 设置是否支持缩放 
		 setCacheMode 设置缓冲的模式  
		 setDefaultFontSize 设置默认的字体大小 
		 setDefaultTextEncodingName 设置在解码时使用的默认编码 
		 setFixedFontFamily 设置固定使用的字体
		 setJavaSciptEnabled 设置是否支持Javascript
	     setLayoutAlgorithm 设置布局方式  
		 setLightTouchEnabled 设置用鼠标激活被选项
		 setSupportZoom 设置是否支持变焦
		 setLoadWithOverviewMode 是否充满全屏
		 setUseWideViewPort 可任意比例缩放 
		 setDisplayZoomControls(false); 设定缩放控件隐藏
		 */
		WebSettings settings = webView.getSettings(); //通过getSettings()获得WebSettings，
		settings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN); 
		settings.setUseWideViewPort(true); 
        settings.setLoadWithOverviewMode(true); 
		settings.setSupportZoom(true);
		settings.setDisplayZoomControls(false);
	    settings.setDefaultTextEncodingName("utf-8");

	    WebSettings.ZoomDensity zoomDensity = WebSettings.ZoomDensity.FAR ;//默认缩放模式是ZoomDensity.FAR
	    int screenDensity = getResources().getDisplayMetrics().densityDpi;
	    System.out.println("--------------------------------------\n");
	    System.out.println("--------------------------------------"+screenDensity);
	    System.out.println("--------------------------------------\n");
	    switch (screenDensity){
	    case DisplayMetrics.DENSITY_LOW :
	        zoomDensity = WebSettings.ZoomDensity.CLOSE;
	        break ;
	    case DisplayMetrics.DENSITY_MEDIUM :
	        zoomDensity = WebSettings.ZoomDensity.MEDIUM;
	        break ;
	    case DisplayMetrics.DENSITY_HIGH :
	        zoomDensity = WebSettings.ZoomDensity.FAR;
	        break ;
	    }	    	    
	    settings.setDefaultZoom(zoomDensity) ; 
		
	    webView.clearCache(true);

	    webView.setWebViewClient(new WebViewClient(){
	        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl)
	        {
	            Toast.makeText(NoticeDetailActivity.this, "运行错误：" + description, Toast.LENGTH_SHORT).show();
	        }
	    });
	    
	    
	    //WebView中的JavaScript代码使用 setWebChromeClient
	    webView.setWebChromeClient(new WebChromeClient(){
	          @Override
	          public void onProgressChanged(WebView view, int newProgress) {
				webViewProgressbar.setProgress(newProgress);
				if (newProgress == 100) {
					webViewProgressbar.setVisibility(View.GONE);
					progressDialog.cancel();
				}
			}
		});
	    
	    // loadDataWithBaseURL()加载一段HTML代码片段。
	    webView.loadDataWithBaseURL(null, htmlString, "text/html", "utf-8", null);
	}
}
