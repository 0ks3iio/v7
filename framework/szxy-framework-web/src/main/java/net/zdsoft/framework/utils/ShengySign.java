package net.zdsoft.framework.utils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ShengySign {
	private static final Map<String,String> Key_Map=new HashMap<String,String>();
	public static final Map<String,String> provinceMap=new HashMap<String,String>();
	static{
		Key_Map.put("0","Z");
		Key_Map.put("1","0");
		Key_Map.put("2","T");
		Key_Map.put("3","t");
		Key_Map.put("4","F");
		Key_Map.put("5","f");
		Key_Map.put("6","S");
		Key_Map.put("7","s");
		Key_Map.put("8","E");
		Key_Map.put("9","N");
		Key_Map.put("-","L");
		Key_Map.put(":","D");
		Key_Map.put(" ","a");
		
		provinceMap.put("江苏","1");
		provinceMap.put("北京","834");
		provinceMap.put("天津","835");
		provinceMap.put("山西","837");
		provinceMap.put("内蒙古","838");
		provinceMap.put("辽宁","839");
		provinceMap.put("吉林","840");
		provinceMap.put("黑龙江","841");
		provinceMap.put("上海","842");
		provinceMap.put("浙江","843");
		provinceMap.put("安徽","844");
		provinceMap.put("福建","845");
		provinceMap.put("江西","846");
		provinceMap.put("山东","847");
		provinceMap.put("河南","848");
		provinceMap.put("湖北","849");
		provinceMap.put("湖南","850");
		provinceMap.put("广东","851");
		provinceMap.put("广西","852");
		provinceMap.put("重庆","854");
		provinceMap.put("四川","855");
		provinceMap.put("贵州","856");
		provinceMap.put("云南","857");
		provinceMap.put("西藏","858");
		provinceMap.put("陕西","859");
		provinceMap.put("甘肃","860");
		provinceMap.put("青海","861");
		provinceMap.put("宁夏","862");
		provinceMap.put("新疆","1120");
		provinceMap.put("河北","1128");
	}
	
	
	
	public static String getShengySign(){
		String sign = DateUtils.date2String(new Date(), "yyyy-MM-dd HH:mm:ss");
		System.out.println(sign);
		for(String key:Key_Map.keySet()){
			sign = sign.replaceAll(key, Key_Map.get(key));
		}
		Random random = new Random();//随机类初始化
		int number = random.nextInt(20);
		return sign.substring(0,number)+"A"+sign.substring(number, sign.length());
	}
	
	public static void main(String[] args) {
		
		System.out.println(getShengySign());
	}
	
}
