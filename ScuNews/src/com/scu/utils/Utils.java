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
 * 项目中使用到的公共工具
 * 对获得页面字符串进行格式化处理 以便在手机上显示
 */
public class Utils{	
	/**
	 * 得到类本身的对象，用于简化代码书写，不用每次为了调用某个动态方法都new一个新对象
	 * @return 返回一个新的PublicUtils对象，用于调用动态方法
	 */
	public static Utils getThis() {
		return new Utils();
	}
	
	/**
	 * 去掉字符串中的回车换行
	 * @param content 传入的字符串
	 * @return 返回去掉回车换行后的结果字符串
	 */
	public static String removeEnterKeys(String content){
		if(content.contains("\r"))
			content = content.replaceAll("\r", "");
		
		if(content.contains("\n"))
			content = content.replaceAll("\n", "");
		
		return content;
	}
		
	/**
	 * 将集合中所有元素用逗号连接起来，并去掉最后的逗号
	 * @param <T> 传入集合类型，必须为对象类型
	 * @param rowTdList 一行中所有单元格文本内容的集合
	 * @return 返回将所有单元格元素用逗号连接后的字符串
	 */
	public static <T> String joinListElementsWithComma(ArrayList<T> rowTdList){
		String joinedString = "";
		for(int i = 0; i < rowTdList.size(); i++)
			joinedString += rowTdList.get(i) + ",";
		
		joinedString = joinedString.substring(0, joinedString.length() - 1); 
		return joinedString;
	}	
	
	/**
	 * 将数组中所有元素用逗号连接起来，并去掉最后的逗号
	 * @param <T> 传入数组类型，必须为对象类型
	 * @param rowTdList 一行中所有单元格文本内容的集合
	 * @return 返回将所有单元格元素用逗号连接后的字符串
	 */
	public static <T> String joinArrayElementsWithComma(T[] rowTdList){
		String joinedString = "";
		for(int i = 0; i < rowTdList.length; i++)
			joinedString += rowTdList[i] + ",";
		
		joinedString = joinedString.substring(0, joinedString.length() - 1); 
		return joinedString;
	}

	/**
	 * 通过HttpGet获取页面内容，如果指定保存文件则保存，否则只返回页面文本内容
	 * @param httpclient Http连接
	 * @param url get连接用的Url地址，包含参数和Token
	 * @param saveFileName 获取返回的原始网页内容存放的路径，如果为空则不保存
	 * @return 返回原始网页字符串内容
	 */
	public static String getPageByHttpGet(HttpClient httpclient, String url, String saveFileName){
		try {
			HttpGet httpget = new HttpGet(url);
			HttpResponse response = httpclient.execute(httpget);			
			HttpEntity entity = response.getEntity();
			String pageContent = EntityUtils.toString(entity, "GB2312");
			//如果传入的文件路径不为空则保存获取到的原始网页，否则不保存，公返回网页内容
			if (entity != null && saveFileName != null)
				Utils.saveContentToFile(saveFileName, pageContent, false);
			//返回原始网页内容
			return pageContent;
		} catch (Exception e){
			e.printStackTrace();
		} 
		return null;
	}
	
	/**
	 * 读文件，将指定文件读入内存
	 * @param filePath 文件路径
	 * @return 返回文件内容字符串数据
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
	 * 向指定的文件中保存到指定的字符串内容
	 * @param fileName 文件保存路径
	 * @param content 要保存的文件内容
	 * @param mode 写入模式，true为追加，false为覆盖写入
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
	 * 向指定的文件中保存到指定的字符串集合内容
	 * @param fileName 文件名称
	 * @param contentList 要保存的文件内容集合
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
	 * 使用指定的子串分割字符串
	 * @param srcString 原始字符串
	 * @param splitFlag 指定的分割标记
	 * @return 返回分格后的新串，将原串分割成以标记分开的字符串数组，
	 * 如果有n个分割标记，则返回数组的大小为n+1，如果有连续2个分割标记，则中间也占用数组一个元素，内容为空
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
	 * 得到一个字符串中指定子串的个数，用递归实现	
	 * @param str 原始字符串
	 * @param flag 子串内容
	 * @param count 全局变量，用于记录子串个数
	 * @return 返回指定子串的个数
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
	 * 使用指定的正则表达式将字符串中符合正则表达式条件的子串替换为指定的字符串
	 * @param sourceString 原始字符串
	 * @param regexString 指定的正则表达式
	 * @param replaceString 替换的目标子串
	 * @param index replaceString 替换表达式中原串对应的位置
	 * @return 返回替换后的结果字符串
	 */
	public static String regexReplace(String sourceString, String regexString, String replaceString, int index){
		Pattern p = null;  
        Matcher m = null;  
        String value = null;  
        String subSrcTmp, subRslTmp;        

        // 去掉<>标签及其之间的内容  (<P style[^>]*><SPAN)
        p = Pattern.compile(regexString);  
        m = p.matcher(sourceString);   
        //下面的while循环式进行循环匹配替换，把找到的所有符合匹配规则的字串都替换为你想替换的内容  
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
