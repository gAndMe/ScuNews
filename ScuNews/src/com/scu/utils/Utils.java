package com.scu.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;

/**
 * ��Ŀ��ʹ�õ��Ĺ�������
 * �Ի��ҳ���ַ������и�ʽ������ �Ա����ֻ�����ʾ
 */
public class Utils{	
	/**
	 * �õ��౾��Ķ������ڼ򻯴�����д������ÿ��Ϊ�˵���ĳ����̬������newһ���¶���
	 * @return ����һ���µ�PublicUtils�������ڵ��ö�̬����
	 */
	public static Utils getThis() {
		return new Utils();
	}
	
	/**
	 * ȥ���ַ����еĻس�����
	 * @param content ������ַ���
	 * @return ����ȥ���س����к�Ľ���ַ���
	 */
	public static String removeEnterKeys(String content){
		if(content.contains("\r"))
			content = content.replaceAll("\r", "");
		
		if(content.contains("\n"))
			content = content.replaceAll("\n", "");
		
		return content;
	}
		
	/**
	 * ������������Ԫ���ö���������������ȥ�����Ķ���
	 * @param <T> ���뼯�����ͣ�����Ϊ��������
	 * @param rowTdList һ�������е�Ԫ���ı����ݵļ���
	 * @return ���ؽ����е�Ԫ��Ԫ���ö������Ӻ���ַ���
	 */
	public static <T> String joinListElementsWithComma(ArrayList<T> rowTdList){
		String joinedString = "";
		for(int i = 0; i < rowTdList.size(); i++)
			joinedString += rowTdList.get(i) + ",";
		
		joinedString = joinedString.substring(0, joinedString.length() - 1); 
		return joinedString;
	}	
	
	/**
	 * ������������Ԫ���ö���������������ȥ�����Ķ���
	 * @param <T> �����������ͣ�����Ϊ��������
	 * @param rowTdList һ�������е�Ԫ���ı����ݵļ���
	 * @return ���ؽ����е�Ԫ��Ԫ���ö������Ӻ���ַ���
	 */
	public static <T> String joinArrayElementsWithComma(T[] rowTdList){
		String joinedString = "";
		for(int i = 0; i < rowTdList.length; i++)
			joinedString += rowTdList[i] + ",";
		
		joinedString = joinedString.substring(0, joinedString.length() - 1); 
		return joinedString;
	}

	/**
	 * ͨ��HttpGet��ȡҳ�����ݣ����ָ�������ļ��򱣴棬����ֻ����ҳ���ı�����
	 * @param httpclient Http����
	 * @param url get�����õ�Url��ַ������������Token
	 * @param saveFileName ��ȡ���ص�ԭʼ��ҳ���ݴ�ŵ�·�������Ϊ���򲻱���
	 * @return ����ԭʼ��ҳ�ַ�������
	 */
	public static String getPageByHttpGet(HttpClient httpclient, String url, String saveFileName){
		try {
			HttpGet httpget = new HttpGet(url);
			HttpResponse response = httpclient.execute(httpget);			
			HttpEntity entity = response.getEntity();
			String pageContent = EntityUtils.toString(entity, "GB2312");
			//���������ļ�·����Ϊ���򱣴��ȡ����ԭʼ��ҳ�����򲻱��棬��������ҳ����
			if (entity != null && saveFileName != null)
				Utils.saveContentToFile(saveFileName, pageContent, false);
			//����ԭʼ��ҳ����
			return pageContent;
		} catch (Exception e){
			e.printStackTrace();
		} 
		return null;
	}
	
	/**
	 * ���ļ�����ָ���ļ������ڴ�
	 * @param filePath �ļ�·��
	 * @return �����ļ������ַ�������
	 */
	public static String getFileContent(String filePath){
		BufferedReader bufferedReader = null;
		File file = new File(filePath);
		try {
			bufferedReader = new BufferedReader(new FileReader(file));
			StringBuffer sbWholeData = new StringBuffer();
			String sLineData = null;
			while ((sLineData = bufferedReader.readLine()) != null){
				sbWholeData.append(sLineData);
			}
			return sbWholeData.toString();
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			try {
				bufferedReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * ��ָ�����ļ��б��浽ָ�����ַ�������
	 * @param fileName �ļ�����·��
	 * @param content Ҫ������ļ�����
	 * @param mode д��ģʽ��trueΪ׷�ӣ�falseΪ����д��
	 */
	public static void saveContentToFile(String fileName, String content, boolean mode) {
		if(fileName == null) return;
		
		FileOutputStream fileOutputStream = null;
		PrintStream printStream = null;
		File file = new File(fileName);
		try {
			if (!file.exists()) file.createNewFile();
			fileOutputStream = new FileOutputStream(file, mode);
			printStream = new PrintStream(fileOutputStream);
			printStream.println(content);
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			try {
				printStream.close();
				fileOutputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * ��ָ�����ļ��б��浽ָ�����ַ�����������
	 * @param fileName �ļ�����
	 * @param contentList Ҫ������ļ����ݼ���
	 */
	public static void saveContentToFile(String fileName, List<String> contentList, boolean mode) {
		if(fileName == null) return;
		
		FileOutputStream fileOutputStream = null;
		PrintStream printStream = null;
		File file = new File(fileName);
		try {
			if (!file.exists()) file.createNewFile();
			fileOutputStream = new FileOutputStream(file, mode);
			printStream = new PrintStream(fileOutputStream);
			for(int i = 0; i < contentList.size(); i++)
				printStream.println(contentList.get(i));
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			try {
				printStream.close();
				fileOutputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}	
	
	/**
	 * ʹ��ָ�����Ӵ��ָ��ַ���
	 * @param srcString ԭʼ�ַ���
	 * @param splitFlag ָ���ķָ���
	 * @return ���طָ����´�����ԭ���ָ���Ա�Ƿֿ����ַ������飬
	 * �����n���ָ��ǣ��򷵻�����Ĵ�СΪn+1�����������2���ָ��ǣ����м�Ҳռ������һ��Ԫ�أ�����Ϊ��
	 */
	public static String[] splitString(String srcString, String splitFlag){
		int size = getThis().subStringCount(srcString, splitFlag) + 1;
		String[] result = new String[size];
		for(int i = 0; i < size; i++){
			if(i == size - 1){
				result[i] = srcString;
				break;
			}
			
			if(srcString.indexOf(splitFlag) > 0)
				result[i] = srcString.substring(0, srcString.indexOf(splitFlag));
			else
				result[i] = "";
			
			if(srcString.indexOf(splitFlag) < srcString.length())
				srcString = srcString.substring(srcString.indexOf(splitFlag) + 1);
			else
				srcString = "";
		}
 
		return result;
	}
	
	/**
	 * �õ�һ���ַ�����ָ���Ӵ��ĸ������õݹ�ʵ��	
	 * @param str ԭʼ�ַ���
	 * @param flag �Ӵ�����
	 * @param count ȫ�ֱ��������ڼ�¼�Ӵ�����
	 * @return ����ָ���Ӵ��ĸ���
	 */
	private int count = 0;
	public int subStringCount(String str, String flag) {
		if (str.indexOf(flag) != -1) {
			count++;
			subStringCount(str.substring(str.indexOf(flag) + flag.length()), flag);
			return count;
		}
		return 0;
	}
	


	/**
	 * ʹ��ָ����������ʽ���ַ����з���������ʽ�������Ӵ��滻Ϊָ�����ַ���
	 * @param sourceString ԭʼ�ַ���
	 * @param regexString ָ����������ʽ
	 * @param replaceString �滻��Ŀ���Ӵ�
	 * @param index replaceString �滻���ʽ��ԭ����Ӧ��λ��
	 * @return �����滻��Ľ���ַ���
	 */
	public static String regexReplace(String sourceString, String regexString, String replaceString, int index){
		Pattern p = null;  
        Matcher m = null;  
        String value = null;  
        String subSrcTmp, subRslTmp;        

        // ȥ��<>��ǩ����֮�������  (<P style[^>]*><SPAN)
        p = Pattern.compile(regexString);  
        m = p.matcher(sourceString);   
        //�����whileѭ��ʽ����ѭ��ƥ���滻�����ҵ������з���ƥ�������ִ����滻Ϊ�����滻������  
        while (m.find()) { 
        	subSrcTmp = m.group(0);
            value = m.group(index);  
            subRslTmp = subSrcTmp.replace(value, replaceString);
            sourceString = sourceString.replace(subSrcTmp, subRslTmp); 
        }   
        return sourceString;  
	}
	
	public static String regexReplace(String sourceString, String regexString, String replaceString){		
        return regexReplace(sourceString, regexString, replaceString, 1);  
	}
}
