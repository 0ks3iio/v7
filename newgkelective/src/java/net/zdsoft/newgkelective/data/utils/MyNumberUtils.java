package net.zdsoft.newgkelective.data.utils;

import java.util.ArrayList;
import java.util.List;

public class MyNumberUtils {
	/**
	 * 创建一个从1到n的递增数列
	 * @param size
	 * @return
	 */
	public static List<String> getNumList(Integer size){
		ArrayList<String> list = new ArrayList<>();
		if(size==null || size<0)
			return list;
		for(int i=0;i<size;i++){
			list.add(i+1+"");
		}
		return list;
	}
}
