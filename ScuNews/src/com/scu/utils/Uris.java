package com.scu.utils;

/**
 * ���ӵ�ַ������
 *
 */
public class Uris {
	
	//����ҳ����ַ ����ռ䰲ȫѧԺ
	public static String scuNoticeRoot = "http://ccs.scu.edu.cn";
	//���Ż�֪ͨ����
	public class NoticeType{
		public static final int NEWS = 1;
		public static final int NOTIFICATION = 2;
	}
	//����ҳ��ٵ�ַ����Ҫ���ݴ������ת����ʽ--http://ccs.scu.edu.cn/news/index_1.html
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
	
	//��ҳhtml��ʽ����·��
	public static String tmpFileName = "C:/tmp.htm";
	//ת����csv��ʽ�󱣴�·��
	public static String resultFileName = "C:/test.csv";		
}


