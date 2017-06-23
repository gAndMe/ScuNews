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
 * 获取四川大学网络空间安全学院新闻或通知 标题
 * 
 */
public class NoticeTitleService {
	/**
	 * 根据指定的页面编号和信息类型（新闻|通知）获取数据集合
	 * @param pageNow 要获取的页面
	 * @param noticeType 信息类型，NoticeType.NEWS为学院新闻，NoticeType.NOTIFICATION为学院通知
	 * @return 返回当指定页数据集合
	 */
	public static List<ScuNoticesBean> getNewsList(int pageNow, int noticeType){		
		//页面内所有行内容的集合，每行所有单元格内容用逗号连接
		ArrayList<ScuNoticesBean> listScuNews = new ArrayList<ScuNoticesBean>();		
		try {
			String htmlContent = getNewsHtmlContent(pageNow, noticeType);
			//特殊处理
			if(htmlContent == null)
			{
				System.out.println("2");
				return listScuNews;
			}
				
			System.out.println("3");
			
			//获得数据片段
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
				listScuNews.add(snb);//添加到集合里
				
				
				//把上面采集过的数据段截去获得新的content
				content = content.substring(subContent.length(), content.length()).trim();
				count++;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	
			return listScuNews;
	}
	
	/**
	 * 根据指定的页面编号和信息类型（新闻|通知）获取原始html数据
	 * @param pageNow 要获取的页面
	 * @param noticeType 信息类型，NoticeType.NEWS为学院新闻，NoticeType.NOTIFICATION为学院通知
	 * @return 返回当指定页html原始内容信息，如果获取信息出错则返回null
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
