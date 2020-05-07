package net.zdsoft.newgkelective.data.optaplanner.c3f7sectioning.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;

public class ArrangeUtils {

	public static void main(String[] args) {
		
//	 arrangementSelect(new String[] {  
//                "1", "2", "3", "4"  
//        }, 2);
//	 
	 
	 List<String[]> a = arrangementSelectString(new String[] {  
			 "1", "2", "3", "4"  
		        }, 4);  
		
		 combinationSelect(new String[] {  
	                "1", "2", "3", "4", "5"  
	        }, 3);
	 System.out.println();
	}

	
	public static List<String[]> arrangementSelectString(String[] dataList, int n) {
		List<String[]> list = new ArrayList<String[]>((int)arrangement(dataList.length, n));
		arrangementSelectString(dataList, new String[n], 0, list);
		return list;
	}
	
	public static List<Integer[]> arrangementSelectInteger(Integer[] dataList, int n) {
		List<Integer[]> list = new ArrayList<Integer[]>((int)arrangement(dataList.length, n));
		arrangementSelectInteger(dataList, new Integer[n], 0, list);
		return list;
	}
	
	
	/**
	 * 排列选择
	 * 
	 * @param dataList
	 *            待选列表
	 * @param resultList
	 *            前面（resultIndex-1）个的排列结果
	 * @param resultIndex
	 *            选择索引，从0开始
	 */
	public static void arrangementSelectString(String[] dataList, String[] resultList, int resultIndex, List<String[]> list) {
		int resultLen = resultList.length;
		if (resultIndex >= resultLen) { // 全部选择完时，输出排列结果
			String[] newResultList = new String[resultList.length];
			System.arraycopy(resultList, 0, newResultList, 0, resultList.length);
			list.add(newResultList);
//			System.out.println(Arrays.asList(resultList));
			return;
		}

		// 递归选择下一个
		for (int i = 0; i < dataList.length; i++) {
			// 判断待选项是否存在于排列结果中
			boolean exists = false;
			for (int j = 0; j < resultIndex; j++) {
				if (dataList[i].equals(resultList[j])) {
					exists = true;
					break;
				}
			}
			if (!exists) { // 排列结果不存在该项，才可选择
				resultList[resultIndex] = dataList[i];
				arrangementSelectString(dataList, resultList, resultIndex + 1, list);
			}
		}
	}
	
	/**
	 * 排列选择
	 * 
	 * @param dataList
	 *            待选列表
	 * @param resultList
	 *            前面（resultIndex-1）个的排列结果
	 * @param resultIndex
	 *            选择索引，从0开始
	 */
	public static void arrangementSelectInteger(Integer[] dataList, Integer[] resultList, int resultIndex, List<Integer[]> list) {
		int resultLen = resultList.length;
		if (resultIndex >= resultLen) { // 全部选择完时，输出排列结果
			Integer[] newResultList = new Integer[resultList.length];
			System.arraycopy(resultList, 0, newResultList, 0, resultList.length);
			list.add(newResultList);
//			System.out.println(Arrays.asList(resultList));
			return;
		}

		// 递归选择下一个
		for (int i = 0; i < dataList.length; i++) {
			// 判断待选项是否存在于排列结果中
			boolean exists = false;
			for (int j = 0; j < resultIndex; j++) {
				if (dataList[i].equals(resultList[j])) {
					exists = true;
					break;
				}
			}
			if (!exists) { // 排列结果不存在该项，才可选择
				resultList[resultIndex] = dataList[i];
				arrangementSelectInteger(dataList, resultList, resultIndex + 1, list);
			}
		}
	}
	
	
//-------------------------------------------

	/**
	 * 排列选择（从列表中选择n个排列）
	 * 
	 * @param dataList
	 *            待选列表
	 * @param n
	 *            选择个数
	 */
	public static void arrangementSelect(String[] dataList, int n) {
		System.out.println(String.format("A(%d, %d) = %d", dataList.length, n, arrangement(dataList.length, n)));
		arrangementSelect(dataList, new String[n], 0);
	}

	/**
	 * 排列选择
	 * 
	 * @param dataList
	 *            待选列表
	 * @param resultList
	 *            前面（resultIndex-1）个的排列结果
	 * @param resultIndex
	 *            选择索引，从0开始
	 */
	private static void arrangementSelect(String[] dataList, String[] resultList, int resultIndex) {
		int resultLen = resultList.length;
		if (resultIndex >= resultLen) { // 全部选择完时，输出排列结果
			System.out.println(Arrays.asList(resultList));
			return;
		}

		// 递归选择下一个
		for (int i = 0; i < dataList.length; i++) {
			// 判断待选项是否存在于排列结果中
			boolean exists = false;
			for (int j = 0; j < resultIndex; j++) {
				if (dataList[i].equals(resultList[j])) {
					exists = true;
					break;
				}
			}
			if (!exists) { // 排列结果不存在该项，才可选择
				resultList[resultIndex] = dataList[i];
				arrangementSelect(dataList, resultList, resultIndex + 1);
			}
		}
	}

	/**
	 * 组合选择（从列表中选择n个组合）
	 * 
	 * @param dataList
	 *            待选列表
	 * @param n
	 *            选择个数
	 */
	public static void combinationSelect(String[] dataList, int n) {
		System.out.println(String.format("C(%d, %d) = %d", dataList.length, n, combination(dataList.length, n)));
		combinationSelect(dataList, 0, new String[n], 0);
	}

	/**
	 * 组合选择
	 * 
	 * @param dataList
	 *            待选列表
	 * @param dataIndex
	 *            待选开始索引
	 * @param resultList
	 *            前面（resultIndex-1）个的组合结果
	 * @param resultIndex
	 *            选择索引，从0开始
	 */
	private static void combinationSelect(String[] dataList, int dataIndex, String[] resultList, int resultIndex) {
		int resultLen = resultList.length;
		int resultCount = resultIndex + 1;
		if (resultCount > resultLen) { // 全部选择完时，输出组合结果
			System.out.println(Arrays.asList(resultList));
			return;
		}

		// 递归选择下一个
		for (int i = dataIndex; i < dataList.length + resultCount - resultLen; i++) {
			resultList[resultIndex] = dataList[i];
			combinationSelect(dataList, i + 1, resultList, resultIndex + 1);
		}
	}

	/**
	 * 计算阶乘数，即n! = n * (n-1) * ... * 2 * 1
	 * 
	 * @param n
	 * @return
	 */
	public static long factorial(int n) {
		return (n > 1) ? n * factorial(n - 1) : 1;
	}

	/**
	 * 计算排列数，即A(n, m) = n!/(n-m)!
	 * 
	 * @param n
	 * @param m
	 * @return
	 */
	public static long arrangement(int n, int m) {
		return (n >= m) ? factorial(n) / factorial(n - m) : 0;
	}

	/**
	 * 计算组合数，即C(n, m) = n!/((n-m)! * m!)
	 * 
	 * @param n
	 * @param m
	 * @return
	 */
	public static long combination(int n, int m) {
		return (n >= m) ? factorial(n) / factorial(n - m) / factorial(m) : 0;
	}

	public static <T> void listSwap(List<T> list, int swapIndex, int toIndex) {
		if (CollectionUtils.isEmpty(list) || swapIndex == toIndex) {
			return;
		}
		Collections.swap(list, swapIndex, toIndex);
	}

	/**
	 * 
	 * @param list
	 * @param startIndex
	 *            inclusive
	 * @return
	 */
	public static <T> List<T> subList(List<T> list, int startIndex) {
		if (list == null) {
			throw new RuntimeException("list cannot be null!");
		}
		if (startIndex > list.size()) {
			throw new IndexOutOfBoundsException("start index out of size!");
		}

		List<T> newList = new ArrayList<T>();
		for (int i = startIndex; i < list.size(); i++) {
			newList.add(list.get(i));
		}
		return newList;
	}

	/**
	 * 
	 * @param list
	 * @param startIndex
	 *            inclusive
	 * @param endIndex
	 *            exclusive
	 * @return
	 */
	public static <T> List<T> subList(List<T> list, int startIndex, int endIndex) {
		if (list == null) {
			throw new RuntimeException("list cannot be null!");
		}
		if (startIndex > list.size()) {
			throw new IndexOutOfBoundsException("start index out of size!");
		}
		if (endIndex > list.size()) {
			throw new IndexOutOfBoundsException("end index out of size!");
		}
		List<T> newList = new ArrayList<T>();
		for (int i = startIndex; i < endIndex; i++) {
			newList.add(list.get(i));
		}
		return newList;
	}

	/**
	 * 8分3份 = 3，3，2
	 * 
	 * @param total
	 * @return
	 */
	public static int[] getBatchArray(int divide, int total) {
		// 余数
		int remainder = 0;
		// 商
		int quotient = 0;
		// 每${PCKC!}的总课程数
		int[] alreadySet = new int[divide];
		quotient = total / divide;
		remainder = total % divide;
		// 一门课的每${PCKC!}的课程数
		Integer[] batchArray = new Integer[divide];
		for (int i = 0; i < divide; i++) {
			batchArray[i] = quotient;
		}
		int index = 0;
		if (remainder > 0) {
			index = smallestIndex(alreadySet);
			while (remainder > 0) {
				batchArray[(index + divide) % divide] = batchArray[(index + divide) % divide] + 1;
				remainder--;
				index++;
			}
		}
		for (int i = 0; i < divide; i++) {
			alreadySet[i] = alreadySet[i] + batchArray[i];
		}
		return alreadySet;
	}

	/**
	 * 10分 按4为一份 = 4，4，2
	 * 
	 * @param total
	 * @return
	 */
	public static int[] getFulledArray(int divide, int total) {
		if (total <= divide) {
			return new int[] { total };
		}
		int left = total;
		List<Integer> list = new ArrayList<Integer>();
		while (left > divide) {
			list.add(divide);
			left -= divide;
		}
		if (left > 0) {
			list.add(left);
		}
		return ArrayUtils.toPrimitive(list.toArray(new Integer[0]));
	}

	// 最大的索引，但小于x
	public static int largestIndex(int[] array, int x) {
		int index = 0;
		int largest = Integer.MIN_VALUE;
		for (int i = 0; i < array.length; i++) {
			if (array[i] > largest && array[i] < x) {
				largest = array[i];
				index = i;
			}
		}
		return index;
	}

	// 最大的索引
	public static int largestIndex(int[] array) {
		int index = 0;
		int largest = array[0];
		for (int i = 1; i < array.length; i++) {
			if (array[i] > largest) {
				largest = array[i];
				index = i;
			}
		}
		return index;
	}

	// 最小的索引
	public static int smallestIndex(int[] array) {
		int index = 0;
		int smallest = array[0];
		for (int i = 1; i < array.length; i++) {
			if (array[i] < smallest) {
				smallest = array[i];
				index = i;
			}
		}
		return index;
	}

	// 最小的索引
	public static int smallestIndex(int[] array, List<Integer> notInIndexs) {
		int index = 0;
		if (array.length == notInIndexs.size()) {
			return index;
		}
		Collections.sort(notInIndexs);
		for (Integer integer : notInIndexs) {
			if (integer != index) {
				break;
			}
			index++;
		}
		int smallest = array[index];

		for (int i = index + 1; i < array.length; i++) {
			if (array[i] < smallest && !notInIndexs.contains(i)) {
				smallest = array[i];
				index = i;
			}
		}
		return index;
	}

	public static <T> List<T> arrayToArrayList(T[] arr) {
		List<T> list = new ArrayList<T>();
		for (T t : arr) {
			list.add(t);
		}
		return list;
	}

	public static <T> Set<T> arrayToHashSet(T[] arr) {
		Set<T> set = new HashSet<T>();
		for (T t : arr) {
			set.add(t);
		}
		return set;
	}
}
