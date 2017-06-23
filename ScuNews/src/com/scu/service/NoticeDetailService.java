package com.scu.service;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

import com.scu.utils.Utils;
import com.scu.utils.Uris;

/**
 * 获取四川大学网络空间安全学院新闻或公告详细内容，并格式化成新的html内容，供Web显示
 * 爬虫程序 字符串处理  数据格式化处理
 */
public class NoticeDetailService {
	/**
	 * 根据新闻URL获取原始html数据，转换成自己所需格式的html文件用于展示
	 * @param url 要获取的页面内容
	 * @return 返回当指定页html原始内容信息，如果获取信息出错则返回null
	 */
	public static String getNewsDetailsHtml(String url){		
		//将原始内容格式化后的新html内容
		String noticeDetailHtml = null;		
		try {
			noticeDetailHtml = getOriginalHtmlPage(url);
			if(noticeDetailHtml == null)
				return null;

			//找到有用数据段，3个Table
			
			String findStart = "<div class=\"detail-title\">";
			
			String findend = "<div class=\"detail-bottom clearfix\">";
			noticeDetailHtml = noticeDetailHtml.substring(noticeDetailHtml.indexOf(findStart), noticeDetailHtml.indexOf(findend));
			//修改标题字体样式
			noticeDetailHtml = noticeDetailHtml.replace(findStart, 
					"<TABLE style=\"table-layout:fixed;FONT-SIZE:18pt;FONT-FAMILY:宋体;mso-ascii-font-family:Calibri;" +
					"mso-ascii-theme-font:minor-latin;mso-fareast-font-family:宋体;mso-fareast-theme-font:minor-fareast;" +
					"mso-hansi-font-family:Calibri;mso-hansi-theme-font:minor-latin\" " +
					"class=pcenter_t2 cellSpacing=0 cellPadding=0 width=100% align=center border=0>");
			
			//修改所有width为适应屏幕
			noticeDetailHtml = Utils.regexReplace(noticeDetailHtml, "<[^>]*width=([ ]*[^ ^>]+)[^>]*>", "100%");

			//强制width为100%
			noticeDetailHtml = Utils.regexReplace(noticeDetailHtml, "<[^>]*(WIDTH:[ ]*[^ ^;^\"^>]+)[^>]*>", "WIDTH:100%");
			
			//强制Table遇到半角字符也可以换行
			//原始串含有style标签，且没有table-layout:fixed属性，则添加table-layout:fixed属性
			noticeDetailHtml = Utils.regexReplace(noticeDetailHtml, "<TABLE[^>]*(style=\")(?:(?!table-layout:fixed)[^>])*>", "style=\"table-layout:fixed;");
			//原始串不含style标签，则添加style标签
			noticeDetailHtml = Utils.regexReplace(noticeDetailHtml, "(<TABLE)(?:(?!style=)[^>])*>", "<TABLE style=\"table-layout:fixed;\"");

			//强制Td样式为遇到半角换行
			//原始串含有style标签，且没有word-break:break-all属性，则添加word-break:break-all属性
			noticeDetailHtml = Utils.regexReplace(noticeDetailHtml, "<TD[^>]*(style=\")(?:(?!word-break:break-all)[^>])*>", "style=\"word-break:break-all; ");	
			//原始串不含style标签，则添加style标签和word-break:break-all属性
			noticeDetailHtml = Utils.regexReplace(noticeDetailHtml, "(<TD)(?:(?!style=)[^<])*>", "<TD style=\"word-break:break-all;\"");		
			
			//使用正则表达式替换掉图片显示的样式段落缩进显示样式"<P style="LINE-HEIGHT: 150%; TEXT-INDENT: 21pt; mso-char-indent-count: 2.0000" class=MsoNormal>
			noticeDetailHtml = Utils.regexReplace(noticeDetailHtml, "<P[^>]*(style=\"[^>^\"]*\")[^>]*>(<[^P][^>]*>)*<IMG", "");
			
			//重新格式化图片显示，增加宽度控件样式为适应屏幕宽度<IMG style="FILTER: ; WIDTH: 600px; HEIGHT: 400px" border=0 hspace=0 alt="" src
			//原始串含有style标签，则改变WIDTH属性值为100%，HEIGHT属性为auto
			noticeDetailHtml = Utils.regexReplace(noticeDetailHtml, "<IMG[^>]*(style=)[^>]*src=", "style=\"FILTER: ; WIDTH: 100%; HEIGHT: auto\"");
			//原始串不含style标签，则添加style标签，设置WIDTH属性值为100%，HEIGHT属性为auto
			noticeDetailHtml = Utils.regexReplace(noticeDetailHtml, "(<IMG)(?:(?!style=)[^>])*>", "<IMG style=\"FILTER: ; WIDTH: 100%; HEIGHT: auto\"");
			
			//为获取的内容添加头部和尾部标签，生成新的完整的html
			noticeDetailHtml = getHtmlHead() + noticeDetailHtml + getHtmlTail();
			
			//输出新生成的html内容，以便调试
			System.out.println("noticeDetailHtml::"+noticeDetailHtml);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return noticeDetailHtml;
	}
	
	/**
	 * 根据新闻URL获取原始html数据
	 * @param url 要获取的页面内容
	 * @return 返回当指定页html原始内容信息，如果获取信息出错则返回null
	 */
	public static String getOriginalHtmlPage(String url){
		HttpClient httpClient = new DefaultHttpClient();
		try {	
			return Utils.getPageByHttpGet(httpClient, url, null);			
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			httpClient.getConnectionManager().shutdown();			
		}
		return null;
	}

	/**
	 * 生成Html头部
	 */
	private static String getHtmlHead(){
		return "<html><head><title></title><meta http-equiv=\"Content-Type\" content=\"text/html; charset=GBK\"><meta name=\"viewport\" content=\"width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1\"></head><body leftmargin='0' topmargin='0'>";
	}

	/**
	 * 生成Html尾部
	 */
	private static String getHtmlTail(){
		return "</body></html>";
	}
	
}
