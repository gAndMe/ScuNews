package com.scu.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.scu.config.R;
import com.scu.model.ScuNoticesBean;

public class AdapterNotices extends BaseAdapter {
	List<ScuNoticesBean> listNotices;	//���� ʱ��  ��ϸ������ַ����
	LayoutInflater layoutInflater;
	/*����������findViewById()����ͬ����LayoutInflater��������res/layout/�µ�xml�����ļ�������ʵ������
	     ��findViewById()����xml�����ļ��µľ���widget�ؼ�(��Button��TextView��)��
	�������ã�
	1������һ��û�б����������Ҫ��̬����Ľ��棬����Ҫʹ��LayoutInflater.inflate()�����룻
	2������һ���Ѿ�����Ľ��棬�Ϳ���ʹ��Activity.findViewById()������������еĽ���Ԫ�ء�
	LayoutInflater ��һ�������࣬���ĵ�������������
	public abstract class LayoutInflater extends Object
	��� LayoutInflater ʵ�������ַ�ʽ
	1. LayoutInflater inflater = getLayoutInflater();//����Activity��getLayoutInflater() 
	2. LayoutInflater inflater = LayoutInflater.from(context);  
	3. LayoutInflater inflater =  (LayoutInflater)context.getSystemService
	                              (Context.LAYOUT_INFLATER_SERVICE);
	*/
	private DataWrapper dataWrapper;

	public AdapterNotices(Context context, List<ScuNoticesBean> listNews) {
		this.listNotices = listNews;
		this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	// �õ���������
	@Override
	public int getCount() {
		return listNotices.size();
	}

	// �������������õ������ж�Ӧ������
	@Override
	public Object getItem(int index) {
		return listNotices.get(index);
	}

	@Override
	public long getItemId(int index) {
		return index;
	}

	private final class DataWrapper {	//���ʱ��  ����
		TextView noticeTime;	
		TextView noticeTitle;
	}

	@Override
	public View getView(int index, View convertView, ViewGroup parent) {
		final ScuNoticesBean snb = listNotices.get(index);

		if (convertView == null) {
			//1������һ��û�б����������Ҫ��̬����Ľ��棬����Ҫʹ��LayoutInflater.inflate()�����룻
			convertView = layoutInflater.inflate(R.layout.item_listview_notice, null);
			
			dataWrapper = new DataWrapper();
			//2������һ���Ѿ�����Ľ��棬�Ϳ���ʹ��Activity.findViewById()������������еĽ���Ԫ�ء�
			dataWrapper.noticeTime = (TextView) convertView.findViewById(R.id.notice_time);
			dataWrapper.noticeTitle = (TextView) convertView.findViewById(R.id.notice_title);
			
			convertView.setTag(dataWrapper);
		} else {
			dataWrapper = (DataWrapper) convertView.getTag();
		}

		dataWrapper.noticeTime.setText("�������ڣ�" + snb.noticeTime);
		dataWrapper.noticeTitle.setText(snb.noticeTitle);

		return convertView;
	}
}
