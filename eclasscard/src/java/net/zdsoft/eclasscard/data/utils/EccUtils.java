package net.zdsoft.eclasscard.data.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.zdsoft.eclasscard.data.constant.EccConstants;
import net.zdsoft.framework.utils.StringUtils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Sets;

public class EccUtils {
	private static final Logger log = LoggerFactory.getLogger(EccUtils.class);
	
	/**
	  * 得到上周周日
	  * 
	  * @return yyyy-MM-dd
	  */
	 public static Date getSundayOfLastWeek() {
		 Calendar c = Calendar.getInstance();
		 int day_of_week = c.get(Calendar.DAY_OF_WEEK)-1;
		 if (day_of_week == 0){
			 day_of_week = 7;
		 }
		 c.add(Calendar.DATE, -day_of_week);
		 return c.getTime();
	 }

	 public static String getSectionName(int sectionNumber){
			switch (sectionNumber) {
			case 1:
				return "第一节";
			case 2:
				return "第二节";
			case 3:
				return "第三节";
			case 4:
				return "第四节";
			case 5:
				return "第五节";
			case 6:
				return "第六节";
			case 7:
				return "第七节";
			case 8:
				return "第八节";
			case 9:
				return "第九节";
			case 10:
				return "第十节";
			case 11:
				return "第十一节";
			case 12:
				return "第十二节";
			case 13:
				return "第十三节";
			case 14:
				return "第十四节";
			case 15:
				return "第十五节";
			case 16:
				return "第十六节";
			default:
				return "第一节";
			}
			
		}
	 /**
	     * 结果返回的是json数组
	     * 
	     * @param result
	     * @return
	     */
	    public static JSONArray getResultArray(String result,String name) {
	    	if(StringUtils.isBlank(result) || StringUtils.isBlank(name))
	    		return new JSONArray();
	    	JSONArray jsonArray = JSONArray.parseArray(result);
	    	if(jsonArray==null||jsonArray.size()==0){
	    		return new JSONArray();
	    	}
	    	JSONObject jsonParam = jsonArray.getJSONObject(0);
	    	if(jsonParam.containsKey(name)){
	    		return jsonParam.getJSONArray(name);
	    	}
	    	return new JSONArray();
	    }
	    /**
	     * 结果返回的String
	     * 
	     * @param result
	     * @return
	     */
	    public static String getResultStr(String result,String name) {
	    	if(StringUtils.isBlank(result) || StringUtils.isBlank(name))
	    		return "";
	    	JSONArray jsonArray = JSONArray.parseArray(result);
	    	if(jsonArray==null||jsonArray.size()==0){
	    		return "";
	    	}
	    	JSONObject jsonParam = jsonArray.getJSONObject(0);
	    	if(jsonParam.containsKey(name)){
	    		return jsonParam.getString(name);
	    	}
	    	return "";
	    }
	    
	    /**
	     * 过滤富文本标签
	     * @param htmlStr
	     * @return
	     * 2017年12月24日
	     */
		public static String delHTMLTag(String htmlStr){ 
	        String regEx_script="<script[^>]*?>[\\s\\S]*?<\\/script>"; //定义script的正则表达式 
	        String regEx_style="<style[^>]*?>[\\s\\S]*?<\\/style>"; //定义style的正则表达式 
	        String regEx_html="<[^>]+>"; //定义HTML标签的正则表达式 
	         
	        Pattern p_script=Pattern.compile(regEx_script,Pattern.CASE_INSENSITIVE); 
	        Matcher m_script=p_script.matcher(htmlStr); 
	        htmlStr=m_script.replaceAll(""); //过滤script标签 
	         
	        Pattern p_style=Pattern.compile(regEx_style,Pattern.CASE_INSENSITIVE); 
	        Matcher m_style=p_style.matcher(htmlStr); 
	        htmlStr=m_style.replaceAll(""); //过滤style标签 
	         
	        Pattern p_html=Pattern.compile(regEx_html,Pattern.CASE_INSENSITIVE); 
	        Matcher m_html=p_html.matcher(htmlStr); 
	        htmlStr=m_html.replaceAll(""); //过滤html标签 

	        return htmlStr.trim(); //返回文本字符串 
	    } 
	    /**
	     * 获取学生照片
	     * @param dirId
	     * @param filePath
	     * @param sex
	     * @return
	     */
		public static String showPicUrl(String dirId,String filePath,Integer sex){ 
			String defaultPath = "";
			 if (sex != null && 2 == sex) {
				 defaultPath = "/static/jscrop/images/portrait_big_female.png";
             }else {
            	 defaultPath = "/static/jscrop/images/portrait_big_male.png";
             }
			if(StringUtils.isNotBlank(filePath)){
				try {
					filePath = URLEncoder.encode(filePath,"UTF-8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}else{
				filePath = "";
			}
			if(dirId==null){
				dirId = "";
			}
			String showPicUrl = "/common/showpicture?dirId=" + dirId + "&filePath="
					+ filePath + "&defaultPath=" + defaultPath;
			return showPicUrl;
		}
		
		/**
	     * 标准版-获取学生照片
	     * @param dirId
	     * @param filePath
	     * @param sex
	     * @return
	     */
		public static String showPictureUrl(String filePath,Integer sex,String time){ 
			String showPicUrl = "";
			if (StringUtils.isBlank(filePath)) {
				if (sex != null && 2 == sex) {
					showPicUrl = "/static/eclasscard/standard/show/images/female.png";
				}else {
					showPicUrl = "/static/eclasscard/standard/show/images/male.png";
				}
			} else {
				try {
					filePath = URLEncoder.encode(filePath,"UTF-8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				showPicUrl = "/common/showpicture?filePath="+ filePath+"&time="+time;
			}
			return showPicUrl;
		}
		
		/**
		 * 去除文件后缀
		 * @param fileName
		 * @return
		 */
		public static String getFileNameNoExt(String fileName){
			int pointIndex = fileName.lastIndexOf(".");
			return pointIndex > 0 && pointIndex < fileName.length() ? fileName.substring(0, pointIndex) : fileName;
		}
		
		/**
		 * 发送httpPost请求
		 * @param url
		 * @param parmMap
		 * @return
		 * @throws Exception
		 */
		public static String sendHttpPost(String url, Map<String, String> parmMap) throws Exception{
			CloseableHttpClient client = HttpClients.createDefault();
			CloseableHttpResponse response = null;
			BufferedReader in = null;
			try {
				//post
				List<NameValuePair> parms = new ArrayList<NameValuePair>();
				for(String key : parmMap.keySet()){
					parms.add(new BasicNameValuePair(key, parmMap.get(key)));
				}
				UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(parms);
				//表单提交
				HttpPost post = new HttpPost(url);
	            post.setEntity(formEntity);
	            
				response = client.execute(post);
	            StringBuilder result = new StringBuilder();
	            if(response.getStatusLine().getStatusCode() ==  HttpStatus.SC_OK){
	            	 HttpEntity entity = response.getEntity();
	                 if(entity!=null){
	                 	in = new BufferedReader(new InputStreamReader(entity.getContent()));
	                 	String line = null;
	                 	while((line = in.readLine())!=null){
	                 		result.append(line);
	                 	}
	                 }
	                 log.info("推送httpPost请求URL"+url+"推送数据="+parmMap.toString());
	            }else{
	            	log.error("推送httpPost请求失败：URL"+url+"推送数据="+parmMap.toString()+",response:"+response.getStatusLine());
	            }
	            
	           
	            return result.toString();
			}catch(Exception e){
				throw e;
			}finally{
				try {
					if(response!=null){
						response.close();
					}
					if (in != null)
						in.close();
					if(client!=null){
						client.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
//		public static void main(String[] args) {
//	    	Calendar calendar = Calendar.getInstance();
//	    	System.out.println(calendar.get(Calendar.YEAR));
//	    	System.out.println(calendar.get(Calendar.MONTH));
//	    	System.out.println(calendar.get(Calendar.DAY_OF_MONTH));
//	    	System.out.println(calendar.get(Calendar.HOUR_OF_DAY));
//	    	System.out.println(calendar.get(Calendar.MINUTE));
//	    	Set<String> stuIds = Sets.newHashSet();
//	    	Map<String,Set<String>> map = Maps.newHashMap();
//	    	Map<String,JSONArray> mapTO = Maps.newHashMap();
//	    	stuIds.add("222");
//	    	stuIds.add("332");
//	    	stuIds.add("444");
//	    	stuIds.add("222");
//	    	map.put("666", stuIds);
//	    	JSONArray array = new JSONArray();
//			JSONObject obj = new JSONObject();
//			obj.put("studentIds", stuIds);
//			obj.put("map", map);
//			array.add(obj);
//			String str1 = EccUtils.getResultStr(array.toJSONString(), "map");
//			mapTO = (Map)JSONObject.parseObject(str1);
//			for(String key:mapTO.keySet()){
//				for(String key1:mapTO.get(key)){
//					System.out.println(key1);
//				}
//			}
//			for (Map.Entry<String, Set<String>> entry : mapTO.entrySet()) {
//				for (String str : entry.getValue()) {
//				}
//			}
//			JSONArray strings = getResultArray(array.toJSONString(), "studentIds");
//			for (int i = 0; i < strings.size(); i++) {
//				System.out.println(strings.get(i));
//			}
//			JSONObject obj = new JSONObject();
//			obj.put("studentId", "444");
//			obj.put("isLeave", "false");
//			obj.put("state", 4);//已销假
//			array.add(obj);
//			
//			System.out.println(getResultStr(array.toJSONString(), "state"));
//		}
	    
	    /**
	     * 截取img的src
	     * @param htmlStr
	     * @return
	     */
	    public static List<String> getImgSrc(String htmlStr){   
	        String img="";   
	        Pattern p_image;   
	        Matcher m_image;   
	        List<String> pics = new ArrayList<String>();
	        String regEx_img = "<img.*src\\s*=\\s*(.*?)[^>]*?>"; 
	        p_image = Pattern.compile(regEx_img,Pattern.CASE_INSENSITIVE);   
	        m_image = p_image.matcher(htmlStr);
	        while(m_image.find()){   
	        	img = img + "," + m_image.group();   
	        	Matcher m  = Pattern.compile("src\\s*=\\s*\"?(.*?)(\"|>|\\s+)").matcher(img);
	            while(m.find()){
	                pics.add(m.group(1));
	            }
	        }   
	           return pics;   
	    }  
	    
	    /**
	     * 过滤掉A标签
	     * @param htmlStr
	     * @return
	     */
	    public static String filtrationA(String htmlStr){
	    	String aString = "<a\\s+(?:(?!</a>).)*?>|</a>";
			Pattern pattern = Pattern.compile(aString,Pattern.CASE_INSENSITIVE);
			Matcher matcher = pattern.matcher(htmlStr);
			return matcher.replaceAll("");
	    }
	    
	    /**
	     * 全屏内容需要走定时任务的类型
	     * @return
	     */
	    public static Set<String> fullScreenTaskSet(){
	    	Set<String> set = Sets.newHashSet();
	    	set.add(EccConstants.ECC_FULL_OBJECT_TYPE03);
	    	set.add(EccConstants.ECC_FULL_OBJECT_TYPE04);
	    	set.add(EccConstants.ECC_FULL_OBJECT_TYPE05);
	    	return set;
	    }
	    
	    /**
	     * 去除时间字符串中的年份
	     * @param dateStr
	     * @return
	     */
	    public static String dateStrRMyear(String dateStr){
	    	if(StringUtils.isBlank(dateStr) || dateStr.length()<=5){
	    		return "";
	    	}
	    	return dateStr.substring(5, dateStr.length());
	    }
	    
	    /**
	     * 获取字符串的长度，如果有中文，则每个中文字符计为2位
	     * @param value 指定的字符串
	     * @return 字符串的长度
	     */
	    public static int length(String value) {
	        int valueLength = 0;
	        String chinese = "[\u0391-\uFFE5]";
	        /* 获取字段值的长度，如果含中文字符，则每个中文字符长度为2，否则为1 */
	        for (int i = 0; i < value.length(); i++) {
	            /* 获取一个字符 */
	            String temp = value.substring(i, i + 1);
	            /* 判断是否为中文字符 */
	            if (temp.matches(chinese)) {
	                /* 中文字符长度为2 */
	                valueLength += 2;
	            } else {
	                /* 其他字符长度为1 */
	                valueLength += 1;
	            }
	        }
	        return valueLength;
	    }
	    /**
	     * 获取字符串Unicode长度前length位
	     * @param value
	     * @param length
	     * @return
	     */
	    public static String getSubStr(String value,int length) {
	        int valueLength = 0;
	        String chinese = "[\u0391-\uFFE5]";
	        for (int i = 0; i < value.length(); i++) {
	            String temp = value.substring(i, i + 1);
	            if (temp.matches(chinese)) {
	                valueLength += 2;
	            } else {
	                valueLength += 1;
	            }
	            if(valueLength>=length){
	            	return value.substring(0, i)+"...";
	            }
	        }
	        return value;
	    }
	    
	    /**
	     * 时间类型HH:mm添加完整
	     * @param timeStr
	     * @return
	     */
	    public static String addTimeStr(String timeStr){
			if(StringUtils.isNotBlank(timeStr)){
				if(timeStr.length()==4){
					timeStr="0"+timeStr;
				}
			}
			return timeStr;
		}
}
