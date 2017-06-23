package com.scu.news;

/**
 * ListView ҳ���߼� (�����б�ҳ��)
 * ��Ҫ���� ����/֪ͨҳ�� �б� ���� �����
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
	private int noticeType;	//����1 ֪ͨ2
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
		
		//��MainActivity�е�intent.putExtra("NoticeType", NoticeType.NEWS)�õ�noticeType
		noticeType = intent.getIntExtra("NoticeType", NoticeType.NEWS);
		
        textViewTitle = (TextView) this.findViewById(R.id.titletext);
        
        //activity_notice.xml ��Ĭ�����õ��� ѧԺ���ţ�������֤������2�Ļ������ñ���Ϊ ѧԺ֪ͨ
		if(noticeType == NoticeType.NOTIFICATION) textViewTitle.setText("֪ͨ����");		
	}
    
    private RelativeLayout layoutViewFootProgressBar; 
    private boolean isLoadFinish = false;
	@SuppressLint("ServiceCast") 
	private void setListView() {
		//�����б�
		listViewNotices = (ListView) this.findViewById(R.id.listview);
		listNotices = new ArrayList<ScuNoticesBean>();
		adapterNews = new AdapterNotices(ScuNoticeActivity.this, listNotices);
		
		/*android�ĺ�̨�����ںܶ�service��������ϵͳ����ʱ��SystemServer������֧��ϵͳ����������
		getSystemService��Android����Ҫ��һ��API������Activity��һ��������
		���ݴ����NAME��ȡ�ö�Ӧ��Object��Ȼ��ת������Ӧ�ķ������
		
		LAYOUT_INFLATER_SERVICE  ���ض���LayoutInflater  ȡ��xml�ﶨ���view*/
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		//������Ӧ��ԴID�������View�������й����ж�̬����
		layoutViewFootProgressBar = (RelativeLayout) inflater.inflate(R.layout.item_listview_foot_progressbar, null);
		// ���ҳ�Ž�����
		listViewNotices.addFooterView(layoutViewFootProgressBar);
		listViewNotices.setAdapter(adapterNews);
		listViewNotices.removeFooterView(layoutViewFootProgressBar);
		
		//����item�������
		listViewNotices.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> listView, View itemView, int index, long id) {
				if(isLoadFinish){
					Intent intent = new Intent();
					
					//��ת��NoticeDetailActivity
					intent.setClass(ScuNoticeActivity.this, NoticeDetailActivity.class);
					//putExtra()�������ݲ����������Ϳ����ڽ�����ת��ʱ��һЩֵ���ݵ�����ת��ȥ�Ľ��棬
					intent.putExtra("windowTitle", textViewTitle.getText().toString()); //windowTitle��ǩ ����NoticeDetailActivityҪ��
					//noticeUrl ���  noticeContentLink(��ϸ������ַ����),��ת������NoticeDetailActivity����
					intent.putExtra("noticeUrl", listNotices.get(index).noticeContentLink);
					
					startActivity(intent);
				}
			}			
		});
		
		//���û�������
		listViewNotices.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) { }
			
			@Override
			public void onScroll(AbsListView listView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				int lastItemIndex = listView.getLastVisiblePosition();//��ǰ��Ļ���һ����¼ID
				if(lastItemIndex + 1 == totalItemCount && isLoadFinish){//�ж������Ƿ�ﵽ�������һ����¼
					//����������ɱ�־
					isLoadFinish = false;
					//����ҳ���ȡ��������
					try {
						addDataAtBackground();
					} catch (Exception e) {
						e.printStackTrace();
					} 
				}
			}
		});
		
		this.showProgressDialog(this, "���Ժ�", "���ڼ�������...");
		this.addDataAtBackground();		
	}

	
	private void addDataAtBackground(){ //��̨���� �������
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
		public void handleMessage(Message msg) {//Android��Ϣ�������,����Ҫ�����ǽ�����Ϣ�ķ�װ������ͬʱ����ָ����Ϣ�Ĳ�����ʽ��
			switch(msg.what){
			case 1:
				listNoticePageNow++;
				adapterNews.notifyDataSetChanged();
				isLoadFinish = true;
				progressDialog.cancel();
				break;
			case 2:
				Toast.makeText(ScuNoticeActivity.this, "����û��������", 1).show();
				break;
			}
			listViewNotices.removeFooterView(layoutViewFootProgressBar);
		}		
	};	
}
