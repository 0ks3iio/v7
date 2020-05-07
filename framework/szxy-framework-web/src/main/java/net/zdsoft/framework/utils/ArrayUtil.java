package net.zdsoft.framework.utils;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;

public class ArrayUtil {

	public static String print(Object[] array){
		StringBuilder sb=new StringBuilder();
		for(int i=0;i<array.length;i++){
			sb.append(","+array[i]);
		}
		return sb.toString().substring(1);
	}
	
	public static String print(Object[] array,String pre){
		StringBuilder sb=new StringBuilder();
		for(int i=0;i<array.length;i++){
			sb.append(pre+array[i]);
		}
		return sb.toString().substring(1);
	}

	public static String[] toArray(Collection<String> os){
		if(isEmpty(os)){
			return ArrayUtils.EMPTY_STRING_ARRAY;
		}
		return os.toArray(new String[os.size()]);
	}
	
	public static List<String> toList(String[] os){
		List<String> strs = new ArrayList<String>();
		if(ArrayUtils.isEmpty(os)) {
			return strs;
		}
		for(String s : os) {
			strs.add(s);
		}
		return strs;
	}

	private static boolean isEmpty(Collection os){
		return CollectionUtils.isEmpty(os);
	}

	public static String[] concat(String[] one, String[] other) {
		String[] target = new String[one.length + other.length];
		System.arraycopy(one, 0, target, 0, one.length);
		System.arraycopy(other, 0, target, one.length, other.length);
		return target;
	}
}
