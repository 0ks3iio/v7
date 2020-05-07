package net.zdsoft.career.data.utils;

import java.util.List;

public class SortResultType {
	
	public static void Perm(String[] array,int start,int end,List<String> typelist){
		if(start == end) {
			String str = "";
			for(int i=0;i<=2;i++){
				str += array[i].substring(0, 1);
			}
			typelist.add(str);
		}else{
			for(int i=start;i<=end;i++){
				if (Integer.parseInt(array[start].substring(2))!=Integer.parseInt(array[i].substring(2))) {
					continue;
				}
				Swap(array,start,i);
				Perm(array,start+1,end,typelist);
				Swap(array,start,i);
			}
		}
	}
	
	static void Swap(String[] a,int one,int two){
		String temp ;
		temp = a[one] ;
		a[one] = a[two];
		a[two] = temp ;
	}
}
