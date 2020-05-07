package net.zdsoft.newgkelective.data.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import org.apache.commons.collections.CollectionUtils;

public class SplitUtils {
	/**
	 * 将一个对集合的操作 分为多次 执行，有返回值
	 * @param srcList
	 * @param opration
	 * @param maxLine
	 * @return
	 */
	public static <K,T> List<T> doSplit(List<K> srcList, Function<List<K>,List<T>> opration, int maxLine){
		List<T> list = new ArrayList<>();
		if(CollectionUtils.isEmpty(srcList) || maxLine<1) {
			return list;
		}
		
		int st=0;
		int end = 0;
		while(st<srcList.size()) {
			end += maxLine;
			if(end > srcList.size())
				end = srcList.size();
			List<K> subList = srcList.subList(st, end);
			List<T> res = opration.apply(subList);
			if(CollectionUtils.isNotEmpty(res))
				list.addAll(res);
			
			st = end;
		}
		
		return list;
	}
	/**
	 * 将一个对集合的操作 分为多次 执行,无返回值
	 * @param srcList
	 * @param opration
	 * @param maxLine
	 * @return
	 */
	public static <T> void doSplit(List<T> srcList, int maxLine, Consumer<List<T>> opration){
		List<Object> arrayList = new ArrayList<>();
		doSplit(srcList, (list) -> {
			opration.accept(list);
			return arrayList;
		}, maxLine);
	}
}
