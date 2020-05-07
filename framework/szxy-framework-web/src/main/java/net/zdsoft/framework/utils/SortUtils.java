package net.zdsoft.framework.utils;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.lang3.math.NumberUtils;

/**
 * 排序工具类
 * @author shenke
 * @since 2017/1/10 13:16
 */
public class SortUtils {

    /**
     *  升序排列
     * @param xpath orderId path example: ModelDto model.displayOrder
     * @param <T> xpath对应的值必须可以转换成int
     * @return
     */
    public static <T> void ASC(List<T> os ,final String xpath){
        try {
            if(CollectionUtils.isEmpty(os)) return;
            Collections.sort(os,getComparator(xpath,true));
        } catch (Exception e){
            System.out.println(ExceptionUtils.getStackTrace(e));
        }
    }

    /**
     * 降序排列
     * @param os
     * @param xpath xpath对应的值必须可以转换成int
     * @param <T>
     */
    public static <T> void DESC(List<T> os , final String xpath){
        try {
            if(CollectionUtils.isEmpty(os)) return ;
            Collections.sort(os,getComparator(xpath,false));
        } catch (Exception e){
            System.out.println(ExceptionUtils.getStackTrace(e));
        }
    }

    private static <T> Comparator<T> getComparator(final String xpath,final boolean asc){
        return new Comparator<T>() {
            @Override
            public int compare(T o1, T o2) {
                try{
                	String s1 = BeanUtils.getProperty(o1,xpath);
                	String s2 = BeanUtils.getProperty(o2,xpath);
                	if(StringUtils.isNotBlank(s1) && StringUtils.isNotBlank(s2)) {
                		if(NumberUtils.isNumber(s1) && NumberUtils.isNumber(s2))
                			return (asc ?1:-1)*(NumberUtils.toFloat(s1) > NumberUtils.toFloat(s2) ? 1 : -1);
                		else
                			return (asc ?1:-1)*(s1.compareTo(s2));
                	}else
                		return -1;
                }catch(Exception e){
                    e.printStackTrace();
                    return -1;
                }
            }
        };
    }

}
