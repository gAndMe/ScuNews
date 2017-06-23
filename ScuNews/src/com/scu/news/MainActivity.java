package com.scu.news;

/**
 * 
 * ��ʼ����İ�ť�¼�
 */
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.scu.config.R;
import com.scu.utils.Uris.NoticeType;

public class MainActivity extends BasicActivity {
	private Button btnNews;		//���Ű�ť
	private Button btnNotification;		//֪ͨ��ť
	
	@Override
	public int getContentViewId() {      
        return R.layout.activity_main;	//��ҳ
	}
	
	@Override
	public void doAfterCreate() {		 
        this.findAndSetButtons();
	}
	
	//�������ź�֪ͨ��ť�ļ�����
	private void findAndSetButtons() {
		btnNews = (Button) this.findViewById(R.id.btn_news);
		btnNotification = (Button) this.findViewById(R.id.btn_notification);
		
		btnNews.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				/*
				  ÿ��Ӧ�ó��������ɸ�Activity��ɣ�ÿһ��Activity����һ��Ӧ�ó������û����н����Ĵ��ڣ�
				  ���ֲ�ͬ�Ľ������档��Ϊÿһ��Acticity������һ�������Ծ������ڸ���Activity֮�������ת��
				  ��Android����������ǿ�Intent����ɵġ�
				  ��ͨ��startActivity()��������һ��Intent��ϵͳ��
				  ϵͳ��������Intent�������ҵ���Ӧ��Activity����ʹ���Activity��������Ӧ���У�
				  Ҳ���������ַ�����������
				 */
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, ScuNoticeActivity.class);
				/*
				 Intent����Я���Ķ���key-value���ݣ������ͨ������putExtra()�����������ݣ�
				 ÿһ��key��Ӧһ��value���ݡ���Ҳ����ͨ������Bundle�������洢�������ݣ�
				 Ȼ��ͨ������putExtras()�������������ݡ���������key������Ҫ�����ð�����ǰ׺��
				 Ȼ���ټ�����������������֤key��Ψһ�ԡ�
				 */
				//putExtra()�������ݲ����������Ϳ����ڽ�����ת��ʱ��һЩֵ���ݵ�����ת��ȥ�Ľ��棬
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
