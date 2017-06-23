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
	List<ScuNoticesBean> listNotices;	//标题 时间  详细内容网址链接
	LayoutInflater layoutInflater;
	/*作用类似于findViewById()。不同点是LayoutInflater是用来找res/layout/下的xml布局文件，并且实例化；
	     而findViewById()是找xml布局文件下的具体widget控件(如Button、TextView等)。
	具体作用：
	1、对于一个没有被载入或者想要动态载入的界面，都需要使用LayoutInflater.inflate()来载入；
	2、对于一个已经载入的界面，就可以使用Activity.findViewById()方法来获得其中的界面元素。
	LayoutInflater 是一个抽象类，在文档中如下声明：
	public abstract class LayoutInflater extends Object
	获得 LayoutInflater 实例的三种方式
	1. LayoutInflater inflater = getLayoutInflater();//调用Activity的getLayoutInflater() 
	2. LayoutInflater inflater = LayoutInflater.from(context);  
	3. LayoutInflater inflater =  (LayoutInflater)context.getSystemService
	                              (Context.LAYOUT_INFLATER_SERVICE);
	*/
	private DataWrapper dataWrapper;

	public AdapterNotices(Context context, List<ScuNoticesBean> listNews) {
		this.listNotices = listNews;
		this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	// 得到数据总数
	@Override
	public int getCount() {
		return listNotices.size();
	}

	// 根据数据索引得到集合中对应的数据
	@Override
	public Object getItem(int index) {
		return listNotices.get(index);
	}

	@Override
	public long getItemId(int index) {
		return index;
	}

	private final class DataWrapper {	//存放时间  标题
		TextView noticeTime;	
		TextView noticeTitle;
	}

	@Override
	public View getView(int index, View convertView, ViewGroup parent) {
		final ScuNoticesBean snb = listNotices.get(index);

		if (convertView == null) {
			//1、对于一个没有被载入或者想要动态载入的界面，都需要使用LayoutInflater.inflate()来载入；
			convertView = layoutInflater.inflate(R.layout.item_listview_notice, null);
			
			dataWrapper = new DataWrapper();
			//2、对于一个已经载入的界面，就可以使用Activity.findViewById()方法来获得其中的界面元素。
			dataWrapper.noticeTime = (TextView) convertView.findViewById(R.id.notice_time);
			dataWrapper.noticeTitle = (TextView) convertView.findViewById(R.id.notice_title);
			
			convertView.setTag(dataWrapper);
		} else {
			dataWrapper = (DataWrapper) convertView.getTag();
		}

		dataWrapper.noticeTime.setText("发布日期：" + snb.noticeTime);
		dataWrapper.noticeTitle.setText(snb.noticeTitle);

		return convertView;
	}
}
