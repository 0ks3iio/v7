package net.zdsoft.bigdata.extend.data.utils;

import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.utils.StringUtils;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;

public class MultiConvertUtils {

	public static String getMultiConvertData(List<Json> dataList,
			List<Json> paramList, List<Json> resultFieldList,
			List<Json> rowDimensionList, List<Json> columnDimensionList,
			List<Json> indexList) {
		Map<String, Map<String, LinkedList<Json>>> resultMap = new HashMap<String, Map<String, LinkedList<Json>>>();

		// 每个列维度有多少个值
		Map<String, LinkedList<String>> columnDimensionMap = new HashMap<String, LinkedList<String>>();
		List<String> rowDiemsionkeys = new LinkedList<String>();

		// 排序
		Map<String, String> sortMap = new HashMap<String, String>();

		for (Json row : rowDimensionList) {
			if (StringUtils.isNotBlank(row.getString("orderJson"))) {
				Map<String, String> tempMap = JSON.parseObject(
						row.getString("orderJson"),
						new TypeReference<Map<String, String>>() {
						});
				for (String key : tempMap.keySet()) {
					sortMap.put(row.get("key") + key, tempMap.get(key));
				}
			}
		}
		if (!sortMap.isEmpty()) {
			for (Json data : dataList) {
				StringBuffer orderKey = new StringBuffer();
				for (Json row : rowDimensionList) {
					if (StringUtils.isNotBlank(row.getString("orderJson"))) {
						String orderValue = sortMap.get(row.getString("key")
								+ data.getString(row.getString("key")));
						if (StringUtils.isNotBlank(orderValue))
							orderKey.append(orderValue);
						else
							orderKey.append("999");
					}
				}
				data.put("orderJson", orderKey.toString());
			}
			dataList.sort(new Comparator<Json>() {
				@Override
				public int compare(Json o1, Json o2) {
					return o1.getString("orderJson").compareTo(
							o2.getString("orderJson"));
				}
			});
		}

		for (Json data : dataList) {
			// key=所有的行维度row_dimension
			StringBuffer rowKey = new StringBuffer();
			for (Json row : rowDimensionList) {
				if (rowKey.length() == 0)
					rowKey.append(data.get(row.get("key")));
				else
					rowKey.append(";").append(data.get(row.get("key")));
			}
			// key是所有的
			Map<String, LinkedList<Json>> columnMap = resultMap.get(rowKey
					.toString());
			if (columnMap == null) {
				columnMap = new HashMap<String, LinkedList<Json>>();
				rowDiemsionkeys.add(rowKey.toString());
			}
			if (CollectionUtils.isNotEmpty(columnDimensionList)) {
				// 将一个列维度所有的值存放在这里
				for (Json column : columnDimensionList) {
					LinkedList<String> columnNameList = columnDimensionMap
							.get(column.get("key"));
					if (CollectionUtils.isEmpty(columnNameList))
						columnNameList = new LinkedList<String>();
					if (!columnNameList.contains(String.valueOf(data.get(column.get("key"))))) {
						columnNameList
								.add(String.valueOf(data.get(column.get("key"))));
						columnDimensionMap.put(String.valueOf(column.get("key")),
								columnNameList);
					}
				}
			}

			StringBuffer columnKey = new StringBuffer();

			for (Json column : columnDimensionList) {
				columnKey.append(data.get(column.get("key")));
			}
			if (columnKey.length() == 0)
				columnKey = rowKey;

			LinkedList<Json> columnValueList = columnMap.get(columnKey
					.toString());
			if (CollectionUtils.isEmpty(columnValueList))
				columnValueList = new LinkedList<Json>();
			for (Json index : indexList) {
				Json columnJson = new Json();
				columnJson.put("name", data.get(index.get("name")));
				columnJson.put("value", data.get(index.get("key")));// 指标值
				columnValueList.add(columnJson);
				columnMap.put(columnKey.toString(), columnValueList);
			}
			resultMap.put(rowKey.toString(), columnMap);
		}

		// 组装头
		// 计算多少列 行维度的个数+列维度个数的乘积
		Map<Integer, Integer> columnNumMap = new HashMap<Integer, Integer>();
		if (columnDimensionList.size() == 1) {
			columnNumMap.put(0, indexList.size());
		} else if (columnDimensionList.size() == 2) {
			columnNumMap.put(1, indexList.size());
			columnNumMap.put(
					0,
					columnDimensionMap.get(
							columnDimensionList.get(1).get("key")).size()
							* indexList.size());
		} else {
			for (int i = columnDimensionList.size() - 1; i >= 0; i--) {
				LinkedList<String> _columnNameList = columnDimensionMap
						.get(columnDimensionList.get(i).get("key"));
				if (CollectionUtils.isNotEmpty(_columnNameList)) {
					if (i == columnDimensionList.size() - 1) {
						columnNumMap.put(i - 1, _columnNameList.size()
								* indexList.size());
					} else {
						for (int j = i + 1; j <= columnDimensionList.size() - 1; j++) {
							if (columnNumMap.containsKey(j - 1)) {
								columnNumMap.put(i - 1, _columnNameList.size()
										* columnNumMap.get(j - 1));
								break;
							}
						}
					}
				}
			}
			columnNumMap.put(columnDimensionList.size() - 1, indexList.size());
		}

		StringBuffer head = new StringBuffer();
		int totalColumnSize = 1;
		int num = 0;
		int nextColumnNum = 1;
		for (Json column : columnDimensionList) {
			LinkedList<String> _columnNameList = columnDimensionMap.get(column
					.get("key"));
			if (CollectionUtils.isNotEmpty(_columnNameList)) {
				head.append("<tr>");
				head.append("<th style='text-align:center' colspan='")
						.append(rowDimensionList.size()).append("'>")
						.append(column.get("name")).append("</th>");
				for (int k = 1; k <= nextColumnNum; k++) {
					for (String columnName : _columnNameList) {
						head.append("<th style='text-align:center' colspan='")
								.append(columnNumMap.get(num)).append("'>")
								.append(columnName).append("</th>");
					}
				}
				totalColumnSize = totalColumnSize * _columnNameList.size();
				head.append("</tr>");
				nextColumnNum = nextColumnNum * _columnNameList.size();
			}
			num++;
		}
		totalColumnSize = totalColumnSize * indexList.size();
		// 递归计算所有组合的columnkeys
		LinkedList<String> columnDiemsionkeys = new LinkedList<String>();
		multiRound(columnDimensionList, columnDimensionMap, columnDiemsionkeys,
				StringUtils.EMPTY, 0);

		// 组装指标列
		head.append("<tr>");
		for (Json row : rowDimensionList) {
			head.append("<th style='text-align:center'>")
					.append(row.get("name")).append("</th>");
		}
		if (CollectionUtils.isNotEmpty(columnDiemsionkeys)) {
			for (String columnKey : columnDiemsionkeys) {
				for (Json index : indexList) {
					head.append("<th style='text-align:center'>")
							.append(index.get("name")).append("</th>");
				}
			}
		} else {
			for (Json index : indexList) {
				head.append("<th style='text-align:center'>")
						.append(index.get("name")).append("</th>");
			}
		}
		head.append("</tr>");

		// 合并单元格用
		Map<String, Json> cellMap = new HashMap<String, Json>();
		Map<String, Integer> cellNumMap = new HashMap<String, Integer>();
		int rowIndex = 0;
		for (String rowKey : rowDiemsionkeys) {
			String[] rowKeyArray = rowKey.split(";");
			for (int k = 0; k < rowKeyArray.length; k++) {
				Json lastRow = cellMap.get((rowIndex - 1) + "-" + k
						+ rowKeyArray[k]);
				if (lastRow != null) {
					if (lastRow.get("name").equals(rowKeyArray[k])) {
						String index_name = lastRow.get("index").toString()
								+ "-" + k + lastRow.get("name").toString();
						cellNumMap.put(index_name,
								cellNumMap.get(index_name) + 1);
						cellMap.put(rowIndex + "-" + k + rowKeyArray[k],
								lastRow);
					}
				} else {
					Json nowRow = new Json();
					nowRow.put("index", rowIndex);
					nowRow.put("name", rowKeyArray[k]);
					nowRow.put("column", k);
					cellMap.put(rowIndex + "-" + k + rowKeyArray[k], nowRow);
					cellNumMap.put(rowIndex + "-" + k + rowKeyArray[k], 1);
				}
			}
			rowIndex++;
		}

		// result转换
		rowIndex = 0;
		List<Json> finalResultList = new LinkedList<Json>();
		for (String rowKey : rowDiemsionkeys) {
			Json resultJson = new Json();
			String[] rowKeyArray = rowKey.split(";");
			for (int k = 0; k < rowKeyArray.length; k++) {
				resultJson.put(String.valueOf(k),
						rowKeyArray[k].equals("null") ? "未知" : rowKeyArray[k]);
				if (cellNumMap
						.containsKey((rowIndex + "-" + k + rowKeyArray[k]))) {
					int realCellNum = cellNumMap
							.get((rowIndex + "-" + k + rowKeyArray[k]));
					resultJson.put("num" + k, realCellNum);
				}
			}
			int rowNum = rowKeyArray.length;

			Map<String, LinkedList<Json>> _columnMap = resultMap.get(rowKey);
			if (CollectionUtils.isNotEmpty(columnDiemsionkeys)) {
				for (String columnKey : columnDiemsionkeys) {
					LinkedList<Json> indexValues = _columnMap.get(columnKey);
					if (CollectionUtils.isNotEmpty(indexValues)) {
						for (Json indexValue : indexValues) {
							resultJson.put(String.valueOf(rowNum++),
									indexValue.get("value"));
						}
					} else {
						for (Json indexValue : indexList) {
							resultJson.put(String.valueOf(rowNum++), 0);
						}
					}

				}
			} else {
				LinkedList<Json> indexValues = _columnMap.get(rowKey);
				for (Json indexValue : indexValues) {
					resultJson.put(String.valueOf(rowNum++),
							indexValue.get("value"));
				}
			}

			finalResultList.add(resultJson);
			rowIndex++;
		}

		totalColumnSize = totalColumnSize + rowDimensionList.size();

		JSONObject obj = new JSONObject();
		obj.put("data", finalResultList);
		obj.put("head", head.toString().replace("null", "未知"));
		obj.put("totalColumnSize", totalColumnSize);
		obj.put("dimRowSize", rowDimensionList.size());
		return obj.toJSONString();
	}

	// 递归获取所有的组合列维度
	private static String multiRound(List<Json> columns,
			Map<String, LinkedList<String>> datasMap,
			LinkedList<String> result, String temp, int index) {
		if (index >= columns.size()) {
			return "";
		}

		String tmp = "";
		Json column = columns.get(index);
		LinkedList<String> datas = datasMap.get(column.get("key"));
		if (datas == null) {
			return "";
		}
		for (int i = 0; i < datas.size(); i++) {
			StringBuffer out = new StringBuffer();
			tmp = datas.get(i);
			if (index == columns.size() - 1) {
				result.add(out.append(temp).append(tmp).toString());
			}
			if (index < columns.size() - 1) {
				multiRound(columns, datasMap, result, temp + tmp, index + 1);
			}
		}
		return "";
	}
}
