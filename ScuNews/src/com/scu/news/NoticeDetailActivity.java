package com.scu.news;
/**
 * ��Ҫ�ǽ�������ҳ��Ĳ����Լ����ݵĻ�ú���ʾ
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
		
        //��ǰҳ�õ�url
        this.getParamsFromPrevious();
        
        textViewTitle = (TextView) this.findViewById(R.id.titletext);
        textViewTitle.setText(windowTitle);
        
        if(url != "")
        	this.convertHtmlData();   
	}


	private void getParamsFromPrevious(){
		Intent intent = this.getIntent();
		windowTitle = intent.getStringExtra("windowTitle");
		url = intent.getStringExtra("noticeUrl");	//�õ�������ҳ��ַ
	}

	private void convertHtmlData() {
		this.showProgressDialog(this, "���Ժ�", "���ڼ�������...");
		new Thread(new Runnable() {			
			@Override
			public void run() {
				try {
					//��������URL��ȡԭʼhtml����,����ʽ�����µ�html���ݣ���Web��ʾ
					//���ÿ��Ʋ�NoticeDetailService��getNewsDetailsHtml����
					htmlString = NoticeDetailService.getNewsDetailsHtml(url);
					//Handler ����Ϣ���л���,Handler.sendMessage(new Message). ���������ָ Handler �ڷ�����Ϣ��ʱ����Ҫ����һ���µĶ���
					/*
					 �ù�Handler���˶�֪����Message�����ֻ�ȡ����
					Message msg = new Message();
					����
					Message msg = handler.obtainMessage();
					�����ַ����������ǣ�ǰ����new�ģ���Ҫ�����ڴ�ռ䣻�����Ǵ�global Message pool��ȡ��������������٣�
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
	private void setWebViewContent() {//������ʾ����/֪ͨ����
		webView = (WebView) findViewById(R.id.webView); // ��� ����/֪ͨ ���ݵ����
		webViewProgressbar = (ProgressBar) findViewById(R.id.webViewProgressbar);
		webViewProgressbar.setMax(100);
		
//		webView.loadUrl("file:///android_asset/example.html");

		/*
		 android�ṩ��webView�ռ�ר�����������ҳ��
		 �ڳ�����װ��webView�ؼ����������ԣ����磺��ɫ�����塢Ҫ���ʵ���ַ�ȡ�
		 ͨ��loadUrl�������õ�ǰwebView��Ҫ���ʵ���ַ��
		 �ڴ���webViewʱ��ϵͳ�и�һĬ�ϵ����ã�����ͨ��webView.getSettings���õ�������á�

		 WebSettings���÷����� 
		 setAllowFileAccess ���û��ֹWebView�����ļ�����
		 setBlockNetworkImage �Ƿ���ʾ����ͼ��
		 setBuiltInZoomControls �����Ƿ�֧������ 
		 setCacheMode ���û����ģʽ  
		 setDefaultFontSize ����Ĭ�ϵ������С 
		 setDefaultTextEncodingName �����ڽ���ʱʹ�õ�Ĭ�ϱ��� 
		 setFixedFontFamily ���ù̶�ʹ�õ�����
		 setJavaSciptEnabled �����Ƿ�֧��Javascript
	     setLayoutAlgorithm ���ò��ַ�ʽ  
		 setLightTouchEnabled ��������꼤�ѡ��
		 setSupportZoom �����Ƿ�֧�ֱ佹
		 setLoadWithOverviewMode �Ƿ����ȫ��
		 setUseWideViewPort ������������� 
		 setDisplayZoomControls(false); �趨���ſؼ�����
		 */
		WebSettings settings = webView.getSettings(); //ͨ��getSettings()���WebSettings��
		settings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN); 
		settings.setUseWideViewPort(true); 
        settings.setLoadWithOverviewMode(true); 
		settings.setSupportZoom(true);
		settings.setDisplayZoomControls(false);
	    settings.setDefaultTextEncodingName("utf-8");

	    WebSettings.ZoomDensity zoomDensity = WebSettings.ZoomDensity.FAR ;//Ĭ������ģʽ��ZoomDensity.FAR
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
	            Toast.makeText(NoticeDetailActivity.this, "���д���" + description, Toast.LENGTH_SHORT).show();
	        }
	    });
	    
	    
	    //WebView�е�JavaScript����ʹ�� setWebChromeClient
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
	    
	    // loadDataWithBaseURL()����һ��HTML����Ƭ�Ρ�
	    webView.loadDataWithBaseURL(null, htmlString, "text/html", "utf-8", null);
	}
}
