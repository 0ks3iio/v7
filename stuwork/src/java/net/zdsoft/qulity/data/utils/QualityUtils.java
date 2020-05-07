package net.zdsoft.qulity.data.utils;

import java.math.BigDecimal;

public class QualityUtils {
	/**
	 * float转成指定小数位的Double类型
	 * @param v 传入的值
	 * @param scale 保留的小数位
	 * @return
	 */
	public static Double roundDouble(float v,int scale){
        if(scale<0){
            throw new IllegalArgumentException(
                "The scale must be a positive integer or zero");
        }
        BigDecimal b = new BigDecimal(Float.toString(v));
        BigDecimal one = new BigDecimal("1");
        return b.divide(one,scale,BigDecimal.ROUND_HALF_UP).doubleValue();
    }
	/**
	 * float转成指定小数位的float类型
	 * @param v 传入的值
	 * @param scale 保留的小数位
	 * @return
	 */
	public static Float roundFloat(float v,int scale){
		if(scale<0){
			throw new IllegalArgumentException(
					"The scale must be a positive integer or zero");
		}
		BigDecimal b = new BigDecimal(Float.toString(v));
		BigDecimal one = new BigDecimal("1");
		return b.divide(one,scale,BigDecimal.ROUND_HALF_UP).floatValue();
	}

}
