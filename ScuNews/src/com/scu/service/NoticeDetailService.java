package com.scu.service;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

import com.scu.utils.Utils;
import com.scu.utils.Uris;

/**
 * ��ȡ�Ĵ���ѧ����ռ䰲ȫѧԺ���Ż򹫸���ϸ���ݣ�����ʽ�����µ�html���ݣ���Web��ʾ
 * ������� �ַ�������  ���ݸ�ʽ������
 */
public class NoticeDetailService {
	/**
	 * ��������URL��ȡԭʼhtml���ݣ�ת�����Լ������ʽ��html�ļ�����չʾ
	 * @param url Ҫ��ȡ��ҳ������
	 * @return ���ص�ָ��ҳhtmlԭʼ������Ϣ�������ȡ��Ϣ�����򷵻�null
	 */
	public static String getNewsDetailsHtml(String url){		
		//��ԭʼ���ݸ�ʽ�������html����
		String noticeDetailHtml = null;		
		try {
			noticeDetailHtml = getOriginalHtmlPage(url);
			if(noticeDetailHtml == null)
				return null;

			//�ҵ��������ݶΣ�3��Table
			
			String findStart = "<div class=\"detail-title\">";
			
			String findend = "<div class=\"detail-bottom clearfix\">";
			noticeDetailHtml = noticeDetailHtml.substring(noticeDetailHtml.indexOf(findStart), noticeDetailHtml.indexOf(findend));
			//�޸ı���������ʽ
			noticeDetailHtml = noticeDetailHtml.replace(findStart, 
					"<TABLE style=\"table-layout:fixed;FONT-SIZE:18pt;FONT-FAMILY:����;mso-ascii-font-family:Calibri;" +
					"mso-ascii-theme-font:minor-latin;mso-fareast-font-family:����;mso-fareast-theme-font:minor-fareast;" +
					"mso-hansi-font-family:Calibri;mso-hansi-theme-font:minor-latin\" " +
					"class=pcenter_t2 cellSpacing=0 cellPadding=0 width=100% align=center border=0>");
			
			//�޸�����widthΪ��Ӧ��Ļ
			noticeDetailHtml = Utils.regexReplace(noticeDetailHtml, "<[^>]*width=([ ]*[^ ^>]+)[^>]*>", "100%");

			//ǿ��widthΪ100%
			noticeDetailHtml = Utils.regexReplace(noticeDetailHtml, "<[^>]*(WIDTH:[ ]*[^ ^;^\"^>]+)[^>]*>", "WIDTH:100%");
			
			//ǿ��Table��������ַ�Ҳ���Ի���
			//ԭʼ������style��ǩ����û��table-layout:fixed���ԣ������table-layout:fixed����
			noticeDetailHtml = Utils.regexReplace(noticeDetailHtml, "<TABLE[^>]*(style=\")(?:(?!table-layout:fixed)[^>])*>", "style=\"table-layout:fixed;");
			//ԭʼ������style��ǩ�������style��ǩ
			noticeDetailHtml = Utils.regexReplace(noticeDetailHtml, "(<TABLE)(?:(?!style=)[^>])*>", "<TABLE style=\"table-layout:fixed;\"");

			//ǿ��Td��ʽΪ������ǻ���
			//ԭʼ������style��ǩ����û��word-break:break-all���ԣ������word-break:break-all����
			noticeDetailHtml = Utils.regexReplace(noticeDetailHtml, "<TD[^>]*(style=\")(?:(?!word-break:break-all)[^>])*>", "style=\"word-break:break-all; ");	
			//ԭʼ������style��ǩ�������style��ǩ��word-break:break-all����
			noticeDetailHtml = Utils.regexReplace(noticeDetailHtml, "(<TD)(?:(?!style=)[^<])*>", "<TD style=\"word-break:break-all;\"");		
			
			//ʹ��������ʽ�滻��ͼƬ��ʾ����ʽ����������ʾ��ʽ"<P style="LINE-HEIGHT: 150%; TEXT-INDENT: 21pt; mso-char-indent-count: 2.0000" class=MsoNormal>
			noticeDetailHtml = Utils.regexReplace(noticeDetailHtml, "<P[^>]*(style=\"[^>^\"]*\")[^>]*>(<[^P][^>]*>)*<IMG", "");
			
			//���¸�ʽ��ͼƬ��ʾ�����ӿ�ȿؼ���ʽΪ��Ӧ��Ļ���<IMG style="FILTER: ; WIDTH: 600px; HEIGHT: 400px" border=0 hspace=0 alt="" src
			//ԭʼ������style��ǩ����ı�WIDTH����ֵΪ100%��HEIGHT����Ϊauto
			noticeDetailHtml = Utils.regexReplace(noticeDetailHtml, "<IMG[^>]*(style=)[^>]*src=", "style=\"FILTER: ; WIDTH: 100%; HEIGHT: auto\"");
			//ԭʼ������style��ǩ�������style��ǩ������WIDTH����ֵΪ100%��HEIGHT����Ϊauto
			noticeDetailHtml = Utils.regexReplace(noticeDetailHtml, "(<IMG)(?:(?!style=)[^>])*>", "<IMG style=\"FILTER: ; WIDTH: 100%; HEIGHT: auto\"");
			
			//Ϊ��ȡ���������ͷ����β����ǩ�������µ�������html
			noticeDetailHtml = getHtmlHead() + noticeDetailHtml + getHtmlTail();
			
			//��������ɵ�html���ݣ��Ա����
			System.out.println("noticeDetailHtml::"+noticeDetailHtml);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return noticeDetailHtml;
	}
	
	/**
	 * ��������URL��ȡԭʼhtml����
	 * @param url Ҫ��ȡ��ҳ������
	 * @return ���ص�ָ��ҳhtmlԭʼ������Ϣ�������ȡ��Ϣ�����򷵻�null
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
	 * ����Htmlͷ��
	 */
	private static String getHtmlHead(){
		return "<html><head><title></title><meta http-equiv=\"Content-Type\" content=\"text/html; charset=GBK\"><meta name=\"viewport\" content=\"width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1\"></head><body leftmargin='0' topmargin='0'>";
	}

	/**
	 * ����Htmlβ��
	 */
	private static String getHtmlTail(){
		return "</body></html>";
	}
	
}
