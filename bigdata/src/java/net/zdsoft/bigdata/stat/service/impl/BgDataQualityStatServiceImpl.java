package net.zdsoft.bigdata.stat.service.impl;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.zdsoft.bigdata.data.exceptions.BigDataBusinessException;
import net.zdsoft.bigdata.frame.data.mysql.MysqlClientService;
import net.zdsoft.bigdata.frame.data.oracle.OracleClientService;
import net.zdsoft.bigdata.metadata.entity.Metadata;
import net.zdsoft.bigdata.metadata.entity.QualityDim;
import net.zdsoft.bigdata.metadata.entity.QualityDimResult;
import net.zdsoft.bigdata.metadata.entity.QualityResultDetail;
import net.zdsoft.bigdata.metadata.entity.QualityRuleRelation;
import net.zdsoft.bigdata.metadata.service.MetadataService;
import net.zdsoft.bigdata.metadata.service.QualityDimResultService;
import net.zdsoft.bigdata.metadata.service.QualityDimService;
import net.zdsoft.bigdata.metadata.service.QualityResultDetailService;
import net.zdsoft.bigdata.metadata.service.QualityRuleRelationService;
import net.zdsoft.bigdata.stat.service.BgDataQualityStatService;
import net.zdsoft.bigdata.stat.util.QualityStatUtils;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.utils.SerializationUtils;
import net.zdsoft.framework.utils.UuidUtils;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("bgDataQualityStatService")
public class BgDataQualityStatServiceImpl implements BgDataQualityStatService {

	private static final Logger logger = Logger
			.getLogger(BgDataQualityStatServiceImpl.class);

	@Autowired
	MysqlClientService mysqlClientService;

	@Autowired
	OracleClientService oracleClientService;

	@Autowired
	QualityDimService qualityDimService;

	@Autowired
	QualityRuleRelationService qualityRuleRelationService;

	@Autowired
	MetadataService metadataService;

	@Autowired
	QualityDimResultService qualityDimResultService;

	@Autowired
	QualityResultDetailService qualityResultDetailService;

	@Override
	public void dataQualityStat(boolean saveDetail)
			throws BigDataBusinessException {

		Map<String, QualityDim> dimMap = qualityDimService
				.findQualityDimMapByType(2);
		List<QualityDim> dimList = qualityDimService.findQualityDimsByType(1);
		Map<String, Json> dimensionStatMap = new HashMap<String, Json>();

		List<QualityRuleRelation> ruleRelationList = qualityRuleRelationService
				.findAll();
		List<QualityRuleRelation> metadataList = new ArrayList<QualityRuleRelation>();
		List<QualityRuleRelation> metadataColumnList = new ArrayList<QualityRuleRelation>();
		Map<String, List<QualityRuleRelation>> ruleRelationMap = new HashMap<String, List<QualityRuleRelation>>();
		Set<String> metadataIds = new HashSet<String>();
		Map<String, List<QualityRuleRelation>> columnsMap = new HashMap<String, List<QualityRuleRelation>>();
		for (QualityRuleRelation ruleRelation : ruleRelationList) {

			if (!metadataIds.contains(ruleRelation.getMetadataId())) {
				metadataIds.add(ruleRelation.getMetadataId());
				metadataList.add(ruleRelation);
			}

			List<QualityRuleRelation> columnIds = columnsMap.get(ruleRelation
					.getMetadataId());

			if (CollectionUtils.isEmpty(columnIds)) {
				columnIds = new ArrayList<QualityRuleRelation>();
			}
			columnIds.add(ruleRelation);
			columnsMap.put(ruleRelation.getMetadataId(), columnIds);

			List<QualityRuleRelation> tempRuleRelationList = ruleRelationMap
					.get(ruleRelation.getMetadataId()
							+ ruleRelation.getColumnId());
			if (CollectionUtils.isEmpty(tempRuleRelationList)) {
				tempRuleRelationList = new ArrayList<QualityRuleRelation>();
				metadataColumnList.add(ruleRelation);
			}
			tempRuleRelationList.add(ruleRelation);
			ruleRelationMap.put(
					ruleRelation.getMetadataId() + ruleRelation.getColumnId(),
					tempRuleRelationList);
		}
		List<QualityResultDetail> resultDetailList = new ArrayList<QualityResultDetail>();

		for (QualityRuleRelation metadata : metadataList) {
			// 暂时只支持mysql数据库
			if (!"mysql".equals(metadata.getDbType())) {
				continue;
			}
			List<QualityRuleRelation> columnList = columnsMap.get(metadata
					.getMetadataId());

			Metadata md = metadataService.findOne(metadata.getMetadataId());
			int startNo = 0;
			int pageSize = 1000;
			List<Json> resultList = new ArrayList<Json>();
			StringBuffer initSql = new StringBuffer();
			try {
				Set<String> columns = new HashSet<String>();
				if ("mysql".equals(metadata.getDbType())) {
					initSql.append("select ");
					// 组装需要查询的列
					int columnSize = 0;
					for (QualityRuleRelation column : columnList) {
						if (columns.contains(column.getColumnId())) {
							continue;
						} else {
							columns.add(column.getColumnId());
						}
						if (columnSize != 0)
							initSql.append(",");
						initSql.append(column.getColumnName());
						columnSize++;
					}
					initSql.append(" from ").append(metadata.getTableName());
					// 从数据库中获取数据 分页
					StringBuffer assembeldSql = new StringBuffer(
							initSql.toString());
					assembeldSql.append(" where ").append(" row_id >=")
							.append(startNo);
					assembeldSql.append(" limit ").append(pageSize);
					//系统默认的数据库
					resultList = mysqlClientService.getDataListFromMysql(
							null, null, assembeldSql.toString(), null,
							null);
				} else if ("oracle".equals(metadata.getDbType())) {
					initSql.append("select ");
					// 组装需要查询的列
					int columnSize = 0;
					for (QualityRuleRelation column : columnList) {
						if (columns.contains(column.getColumnId())) {
							continue;
						} else {
							columns.add(column.getColumnId());
						}
						if (columnSize != 0)
							initSql.append(",");
						initSql.append(column.getColumnName());
						columnSize++;
					}
					initSql.append(" from (");

					initSql.append("select ");
					// 组装需要查询的列
					columnSize = 0;
					columns = new HashSet<String>();
					for (QualityRuleRelation column : columnList) {
						if (columns.contains(column.getColumnId())) {
							continue;
						} else {
							columns.add(column.getColumnId());
						}
						if (columnSize != 0)
							initSql.append(",");
						initSql.append(column.getColumnName());
						columnSize++;
					}
					initSql.append(", rownum RNO from  ")
							.append(metadata.getTableName()).append(" e)");
					initSql.append(" where RNO between @StartNo and @endNo");
					String asembledSql = initSql.toString()
							.replace("@StartNo", String.valueOf(startNo))
							.replace("@endNo", String.valueOf(pageSize));
					resultList = oracleClientService.getDataListFromOracle(
							md.getDbId(), asembledSql);
				}
			} catch (Exception e) {
				logger.error("质量统计表[" + metadata.getName() + "]统计失败:"
						+ e.getMessage());
				throw new BigDataBusinessException("质量统计表["
						+ metadata.getName() + "]统计失败:" + e.getMessage());
			}
			// 作为唯一性判断
			Map<String, String> uniqueMap = new HashMap<String, String>();
			while (CollectionUtils.isNotEmpty(resultList)) {
				for (QualityRuleRelation column : columnList) {
					List<QualityRuleRelation> _ruleRelationList = ruleRelationMap
							.get(metadata.getMetadataId()
									+ column.getColumnId());
					if (_ruleRelationList == null)
						continue;
					for (Json resultJson : resultList) {
						String value = resultJson.getString(column
								.getColumnName());
						StringBuffer errorMsg = new StringBuffer();
						for (QualityRuleRelation ruleRelation : _ruleRelationList) {
							String computerType = ruleRelation
									.getComputerType();
							boolean result = false;
							boolean error = false;
							try {
								switch (computerType) {
								case "notnull":
									result = QualityStatUtils.isNotNull(value);
									if (!result) {
										errorMsg.append(ruleRelation.getName())
												.append(";");
										error = true;
									}
									break;
								case "between":
									if (StringUtils.isNotBlank(ruleRelation
											.getDetail())) {
										Json datas = SerializationUtils
												.deserialize(ruleRelation
														.getDetail(),
														Json.class);
										result = QualityStatUtils.between(
												value,
												datas.getIntValue("min"),
												datas.getIntValue("max"));
										if (!result) {
											errorMsg.append(
													ruleRelation.getName())
													.append(";");
											error = true;
										}
									}
									break;
								case "in":
									if (StringUtils.isNotBlank(ruleRelation
											.getDetail())) {
										result = QualityStatUtils.in(value,
												ruleRelation.getDetail());
										if (!result) {
											errorMsg.append(
													ruleRelation.getName())
													.append(";");
											error = true;
										}
									}
									break;
								case "notin":
									if (StringUtils.isNotBlank(ruleRelation
											.getDetail())) {
										result = QualityStatUtils.in(value,
												ruleRelation.getDetail());
										if (result) {
											errorMsg.append(
													ruleRelation.getName())
													.append(";");
											error = true;
										}
									}
									break;
								case "gt":
									if (StringUtils.isNotBlank(ruleRelation
											.getDetail())) {
										result = QualityStatUtils.gt(value,
												ruleRelation.getDetail());
										if (!result) {
											errorMsg.append(
													ruleRelation.getName())
													.append(";");
											error = true;
										}
									}
									break;
								case "gte":
									if (StringUtils.isNotBlank(ruleRelation
											.getDetail())) {
										result = QualityStatUtils.gte(value,
												ruleRelation.getDetail());
										if (!result) {
											errorMsg.append(
													ruleRelation.getName())
													.append(";");
											error = true;
										}
									}
									break;
								case "lt":
									if (StringUtils.isNotBlank(ruleRelation
											.getDetail())) {
										result = QualityStatUtils.lt(value,
												ruleRelation.getDetail());
										if (!result) {
											errorMsg.append(
													ruleRelation.getName())
													.append(";");
											error = true;
										}
									}
									break;
								case "lte":
									if (StringUtils.isNotBlank(ruleRelation
											.getDetail())) {
										result = QualityStatUtils.lte(value,
												ruleRelation.getDetail());
										if (!result) {
											errorMsg.append(
													ruleRelation.getName())
													.append(";");
											error = true;
										}
									}
									break;
								case "unique":
									if (StringUtils.isBlank(value))
										value = "NULL";
									result = uniqueMap.containsKey(ruleRelation
											.getColumnId() + value);
									if (result) {
										errorMsg.append(ruleRelation.getName())
												.append(";");
										error = true;
									} else {
										uniqueMap.put(
												ruleRelation.getColumnId()
														+ value, value);
									}
									break;
								case "regex":
									if (StringUtils.isNotBlank(ruleRelation
											.getDetail())) {
										result = QualityStatUtils
												.match(ruleRelation.getDetail(),
														value);
										if (!result) {
											errorMsg.append(
													ruleRelation.getName())
													.append(";");
											error = true;
										}
									}
									break;
								case "date":
									result = QualityStatUtils.isDate(value);
									if (!result) {
										errorMsg.append(ruleRelation.getName())
												.append(";");
										error = true;
									}
									break;
								case "digital":
									result = QualityStatUtils.IsNumber(value);
									if (!result) {
										errorMsg.append(ruleRelation.getName())
												.append(";");
										error = true;
									}
									break;
								default:
									break;
								}
							} catch (Exception e) {
								// 规则设置有误，不纳入统计
								logger.error("规则设置有误，不纳入统计-----"
										+ ruleRelation.getName());
								throw new BigDataBusinessException(
										"规则设置有误，不纳入统计-----"
												+ ruleRelation.getName());
							}
							Json dimStatData = dimensionStatMap
									.get(ruleRelation.getDimCode());
							if (dimStatData == null) {
								dimStatData = new Json();
							}
							int totalNum = dimStatData.getIntValue("total");
							int successNum = dimStatData.getIntValue("success");
							totalNum++;
							if (!error) {
								successNum++;
							}
							dimStatData.put("total", totalNum);
							dimStatData.put("success", successNum);
							dimensionStatMap.put(ruleRelation.getDimCode(),
									dimStatData);
						}
						if (saveDetail) {
							if (StringUtils.isNotBlank(errorMsg.toString())) {
								// 保存错误记录‚
								QualityResultDetail detail = new QualityResultDetail();
								detail.setId(UuidUtils.generateUuid());
								detail.setMetadataId(metadata.getMetadataId());
								detail.setDbType(metadata.getDbType());
								detail.setTableName(metadata.getTableName());
								detail.setColumnId(column.getColumnId());
								detail.setColumnName(column.getColumnName());
								detail.setResult(errorMsg.toString());
								detail.setStatTime(new Date());
								resultDetailList.add(detail);
							}
						}
					}
					ruleRelationMap.remove(metadata.getMetadataId()
							+ column.getColumnId());
				}

				startNo += pageSize;
				try {
					if ("mysql".equals(metadata.getDbType())) {
						StringBuffer assembeldSql = new StringBuffer(
								initSql.toString());
						assembeldSql.append(" where ").append(" row_id >=")
								.append(startNo + 1);
						assembeldSql.append(" limit ").append(pageSize);
						resultList = mysqlClientService.getDataListFromMysql(
								md.getDbId(), null, assembeldSql.toString(),
								null, null);
					} else if ("oracle".equals(metadata.getDbType())) {
						String asembledSql = initSql
								.toString()
								.replace("@StartNo",
										String.valueOf(startNo + 1))
								.replace("@endNo",
										String.valueOf(startNo + pageSize));
						resultList = oracleClientService.getDataListFromOracle(
								md.getDbId(), asembledSql);
					}
				} catch (Exception e) {
					logger.error("质量统计表[" + metadata.getName() + "]统计失败:"
							+ e.getMessage());
					throw new BigDataBusinessException("质量统计表["
							+ metadata.getName() + "]统计失败:" + e.getMessage());
				}
			}
		}
		int allTotalNum = 0;
		int allSuccessNum = 0;
		// 计算总的质量分，并且将所有需要的结果保存入库
		List<QualityDimResult> dimResultList = new ArrayList<QualityDimResult>();
		for (String dimCode : dimMap.keySet()) {
			QualityDim dim = dimMap.get(dimCode);
			Json dimStatData = dimensionStatMap.get(dimCode);
			if (dimStatData == null)
				dimStatData = new Json();
			int totalNum = dimStatData.getIntValue("total");
			int successNum = dimStatData.getIntValue("success");
			allTotalNum += totalNum * dim.getWeight();
			allSuccessNum += successNum * dim.getWeight();
			String dimQulityIndex = "100";
			if (totalNum > 0)
				dimQulityIndex = getPercent(successNum, totalNum);

			QualityDimResult dimResult = new QualityDimResult();
			dimResult.setId(UuidUtils.generateUuid());
			dimResult.setType(2);
			dimResult.setDimCode(dim.getCode());
			dimResult.setDimName(dim.getName());
			BigDecimal dimPercent = new BigDecimal(dimQulityIndex);
			dimResult.setResult(dimPercent);
			dimResult.setIsAlarm(dim.getIsAlarm());
			if (dim.getThreshold() != null)
				dimResult.setThreshold(BigDecimal.valueOf(dim.getThreshold()));
			dimResult.setStatTime(new Date());
			dimResult.setStatus(1);
			dimResultList.add(dimResult);
		}
		// 只有维度统计有结果才能有最后的结果
		if (dimensionStatMap.size() > 0) {
			List<QualityDimResult> _dimResultList = qualityDimResultService
					.findQualityDimResultsByTypeAndStatus(1, 1);
			QualityDimResult lastStatResult = null;
			if (CollectionUtils.isNotEmpty(_dimResultList)) {
				lastStatResult = _dimResultList.get(0);
			}
			String totalQulityIndex = "100";
			if (allTotalNum > 0)
				totalQulityIndex = getPercent(allSuccessNum, allTotalNum);
			QualityDimResult allResult = new QualityDimResult();
			allResult.setId(UuidUtils.generateUuid());
			allResult.setType(1);
			BigDecimal totalPercent = new BigDecimal(totalQulityIndex);
			if (totalPercent.compareTo(BigDecimal.valueOf(90)) >= 0) {
				allResult.setGrade("优秀");
			} else if (totalPercent.compareTo(BigDecimal.valueOf(80)) >= 0) {
				allResult.setGrade("良好");
			} else if (totalPercent.compareTo(BigDecimal.valueOf(70)) >= 0) {
				allResult.setGrade("中等");
			} else if (totalPercent.compareTo(BigDecimal.valueOf(60)) >= 0) {
				allResult.setGrade("及格");
			} else if (totalPercent.compareTo(BigDecimal.valueOf(30)) >= 0) {
				allResult.setGrade("差");
			} else {
				allResult.setGrade("极差");
			}
			allResult.setResult(totalPercent);
			allResult.setIsAlarm(dimList.get(0).getIsAlarm());
			if (dimList.get(0).getThreshold() != null)
				allResult.setThreshold(BigDecimal.valueOf(dimList.get(0)
						.getThreshold()));
			allResult.setStatTime(new Date());
			if (lastStatResult != null) {
				allResult.setTrend(totalPercent.subtract(lastStatResult
						.getResult()));
			} else {
				allResult.setTrend(totalPercent);
			}
			allResult.setStatus(1);
			allResult.setIsSaveDetail(saveDetail ? 1 : 0);
			dimResultList.add(allResult);
		}
		// 保存明细记录，永远是先删除后新增
		qualityResultDetailService.deleteAll();
		if (saveDetail) {
			qualityResultDetailService.saveAll(resultDetailList
					.toArray(new QualityResultDetail[0]));
		}
		// 保存统计结果 先将里面数据设置为历史状态
		qualityDimResultService.updateHistoryDataStatus();
		qualityDimResultService.saveAll(dimResultList
				.toArray(new QualityDimResult[0]));
	}

	private static String getPercent(int number1, int number2) {
		// 创建一个数值格式化对象
		NumberFormat numberFormat = NumberFormat.getInstance();
		// 设置精确到小数点后2位
		numberFormat.setMaximumFractionDigits(2);
		String result = numberFormat.format((float) number1 / (float) number2
				* 100);
		return result;
	}

}
