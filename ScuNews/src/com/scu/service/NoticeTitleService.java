package com.scu.service;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.AndFilter;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.OrFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.lexer.Lexer;
import org.htmlparser.util.NodeIterator;
import org.htmlparser.util.NodeList;

import android.util.Log;

import com.scu.model.ScuNoticesBean;
import com.scu.utils.Utils;
import com.scu.utils.Uris;

/**
 * ��ȡ�Ĵ���ѧ����ռ䰲ȫѧԺ���Ż�֪ͨ ����
 * 
 */
public class NoticeTitleService {
	/**
	 * ����ָ����ҳ���ź���Ϣ���ͣ�����|֪ͨ����ȡ���ݼ���
	 * @param pageNow Ҫ��ȡ��ҳ��
	 * @param noticeType ��Ϣ���ͣ�NoticeType.NEWSΪѧԺ���ţ�NoticeType.NOTIFICATIONΪѧԺ֪ͨ
	 * @return ���ص�ָ��ҳ���ݼ���
	 */
	public static List<ScuNoticesBean> getNewsList(int pageNow, int noticeType){		
		//ҳ�������������ݵļ��ϣ�ÿ�����е�Ԫ�������ö�������
		ArrayList<ScuNoticesBean> listScuNews = new ArrayList<ScuNoticesBean>();		
		try {
			String htmlContent = getNewsHtmlContent(pageNow, noticeType);
			//���⴦��
			if(htmlContent == null)
			{
				System.out.println("2");
				return listScuNews;
			}
				
			System.out.println("3");
			
			//�������Ƭ��
			String content = htmlContent.substring(htmlContent.indexOf("<div class=\"wrap\">")+18, htmlContent.indexOf("<div class=\"pages\">")).trim();
 			content = content.substring(content.indexOf("newslist\">")+10, content.indexOf("</ul>")).trim();
 			
			Lexer lexer = new Lexer(content);
			Parser parser = new Parser(lexer);	
			parser.setEncoding("GB2312");
			
			int count =0;
			while(!content.isEmpty())
			{
				
				ScuNoticesBean snb = new ScuNoticesBean();
				String subContent = content.substring(0, content.indexOf("</li>")+5).trim();
				
				snb.noticeTitle = subContent.substring(subContent.indexOf("title=\"")+7, subContent.indexOf("\"> "));
				snb.noticeTime = subContent.substring(subContent.indexOf("<span>")+6, subContent.indexOf("</span>"));
				snb.noticeContentLink = Uris.scuNoticeRoot + subContent.substring(subContent.indexOf("href=\"")+6, subContent.indexOf("\" title"));
				listScuNews.add(snb);//��ӵ�������
				
				
				//������ɼ��������ݶν�ȥ����µ�content
				content = content.substring(subContent.length(), content.length()).trim();
				count++;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	
			return listScuNews;
	}
	
	/**
	 * ����ָ����ҳ���ź���Ϣ���ͣ�����|֪ͨ����ȡԭʼhtml����
	 * @param pageNow Ҫ��ȡ��ҳ��
	 * @param noticeType ��Ϣ���ͣ�NoticeType.NEWSΪѧԺ���ţ�NoticeType.NOTIFICATIONΪѧԺ֪ͨ
	 * @return ���ص�ָ��ҳhtmlԭʼ������Ϣ�������ȡ��Ϣ�����򷵻�null
	 */
	public static String getNewsHtmlContent(int pageNow, int noticeType){
		HttpClient httpClient = new DefaultHttpClient();
		try {	
			String newsUrl = Uris.noticeTitlePage(pageNow, noticeType);
			
			String content = Utils.getPageByHttpGet(httpClient, newsUrl, null);
				
			return content;
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			httpClient.getConnectionManager().shutdown();			
		}
		System.out.println("sadasdas");
		return null;
	}
}
