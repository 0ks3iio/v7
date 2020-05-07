package net.zdsoft.framework.entity;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.math.NumberUtils;

public class ExcelStat {
	private Map<String, Double> statSumMap = new HashMap<String, Double>();
	private Map<String, Integer> statCountMap = new HashMap<String, Integer>();

	public void sum(String key, String value) {
		Double v = statSumMap.get(key + "_sum");
		if (v == null) {
			v = 0.0;
		}
		
		statSumMap.put(key + "_sum", v + NumberUtils.toDouble(value));
	}

	public void count(String key) {
		Integer v = statCountMap.get(key + "_count");
		if (v == null) {
			v = 0;
		}
		statCountMap.put(key + "_count", v + 1);
	}

	public String getSum(String key) {
		return String.format("%.2f", statSumMap.get(key + "_sum"));
	}

	public String getCount(String key) {
		return "" + statCountMap.get(key + "_count");
	}
}
