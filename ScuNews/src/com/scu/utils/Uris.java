package com.scu.utils;

/**
 * 连接地址串常量
 *
 */
public class Uris {
	
	//新闻页根地址 网络空间安全学院
	public static String scuNoticeRoot = "http://ccs.scu.edu.cn";
	//新闻或通知类型
	public class NoticeType{
		public static final int NEWS = 1;
		public static final int NOTIFICATION = 2;
	}
	//新闻页大纲地址，需要根据传入参数转换格式--http://ccs.scu.edu.cn/news/index_1.html
	public static String noticeTitlePage(int pageNow, int noticeType){
		String url = null;
		switch(noticeType){
		case NoticeType.NEWS:
			url =  String.format(scuNoticeRoot + "/news/index_%d.html", pageNow);

			break;							
		case NoticeType.NOTIFICATION:
			
			url = String.format(scuNoticeRoot + "/notice/index_%d.html", pageNow);
			break;
		}
		return url;
	}
	
	//网页html格式保存路径
	public static String tmpFileName = "C:/tmp.htm";
	//转换成csv格式后保存路径
	public static String resultFileName = "C:/test.csv";		
}


