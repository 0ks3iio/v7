package net.zdsoft.datareport.data.service.impl;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.datareport.data.constants.ReportConstants;
import net.zdsoft.datareport.data.dao.DataReportResultsDao;
import net.zdsoft.datareport.data.dto.SaveTaskResultDto;
import net.zdsoft.datareport.data.entity.DataReportColumn;
import net.zdsoft.datareport.data.entity.DataReportResults;
import net.zdsoft.datareport.data.entity.DataReportTitle;
import net.zdsoft.datareport.data.service.DataReportColumnService;
import net.zdsoft.datareport.data.service.DataReportResultsService;
import net.zdsoft.datareport.data.service.DataReportTitleService;
import net.zdsoft.datareport.data.utils.ExcelUtils;
import net.zdsoft.framework.config.Evn;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.Constant;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.MathUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.system.remote.service.SysOptionRemoteService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.curator.shaded.com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

@Service("dataReportResultsService")
public class DataReportResultsServiceImpl extends BaseServiceImpl<DataReportResults,String> implements DataReportResultsService{

	@Autowired
	private DataReportResultsDao dataReportResultsDao;
	@Autowired
	private DataReportColumnService dataReportColumnService;
	@Autowired
	private DataReportTitleService dataReportTitleService;
	
	Pattern pattern = Pattern.compile("^(\\-|\\+)?\\d+(\\.\\d+)?$");
	
	@Override
	public List<DataReportResults> findByTaskId(String taskId) {
		return dataReportResultsDao.findByTaskId(taskId);
	}
	
	@Override
	public List<DataReportResults> findByInfoIdAndType(String infoId, Integer type) {
		return dataReportResultsDao.findByInfoIdAndType(infoId,type);
	}
	
	@Override
	public void deleteByReportId(String infoId) {
		dataReportResultsDao.deleteByReportId(infoId);
	}
	
	@Override
	public String saveDataResult(String path, String taskId, String reportId,
			Integer tableType, String unitId, String ownerId, boolean coverage) throws Exception {
		//获取Excel文件
		String fileSystemPath = Evn.<SysOptionRemoteService> getBean("sysOptionRemoteService").findValue(Constant.FILE_PATH);// 文件系统地址
		File files = new File(fileSystemPath + File.separator + "upload" + File.separator + path);
		File[] filelist = files.listFiles();
		File file = null;
		if (filelist == null || filelist.length == 0) {
			return "emptyFile";
		} else {
			file = filelist[0];
		}
		
		//行列数据
		List<DataReportColumn> rowColumns = dataReportColumnService.findByIdAndType(reportId,ReportConstants.REPORT_COLUMN_TYPE_1);
		List<DataReportColumn> rankColumns = dataReportColumnService.findByIdAndType(reportId,ReportConstants.REPORT_COLUMN_TYPE_2);
		//表头行数
		List<DataReportTitle> titles = dataReportTitleService.findByReportId(reportId);
		int headerSize = (int) titles.stream().filter(tit->Objects.equals(ReportConstants.TITLE_TYPE_1,tit.getType())).count();
		//读取Excel文件上的数据
		List<String[]> datalist = null;
		boolean needStats = false;
		int colSize = 0;
		List<DataReportColumn> columns = null;
		if (Objects.equals(ReportConstants.REPORT_TABLE_TYPE_2, tableType)) {
			datalist = ExcelUtils.readExcel(file.getPath(), headerSize+1, headerSize+rankColumns.size(), 1, -1, tableType);
			colSize = rankColumns.size();
			columns = rankColumns;
			needStats = rankColumns.stream().anyMatch(data -> data.getMethodType() != null);
		} else {
			if (Objects.equals(ReportConstants.REPORT_TABLE_TYPE_1, tableType)) {
				datalist = ExcelUtils.readExcel(file.getPath(), headerSize+1, -1, 1, rowColumns.size(), tableType);
			} else {
				datalist = ExcelUtils.readExcel(file.getPath(), headerSize+1, headerSize+rankColumns.size()+1, 1, rowColumns.size()+1, tableType);
			}
			colSize = rowColumns.size();
			columns = rowColumns;
			needStats = rowColumns.stream().anyMatch(data -> data.getMethodType() != null);
		}
		String msg = analyzeExcelData(headerSize,datalist,rowColumns,rankColumns,tableType);
		if (!Objects.equals(msg, "success")) {
			//删除上传的文件
			file.delete();
			return msg;
		}
		//是否覆盖数据
		List<String[]> oldResults = null;
		int orderIndex = 1;
		if (!coverage) {
			dataReportResultsDao.deleteByTaskIdAndType(taskId, ReportConstants.RESULT_TYPE_2);
			List<DataReportResults> oldResultList = dataReportResultsDao.findByTaskIdAndType(taskId,ReportConstants.RESULT_TYPE_1);
			orderIndex = oldResultList.size() + 1;
			oldResults = Lists.newArrayList();
			String[] oldResult = null;
 			for (DataReportResults result: oldResultList) {
 				oldResult = JSONArray.parseArray(result.getData()).toArray(new String[colSize]);
 				oldResults.add(oldResult);
			}
		} else {
			dataReportResultsDao.deleteByTaskId(taskId);
		}
		
		Date date = new Date();
		DataReportResults reportResults = null;
		List<DataReportResults> newResultList = Lists.newArrayList();
		Double[] stats = new Double[colSize];
		for (int i=0;i<stats.length;i++) {
			stats[i] = 0.0;
		}
		String[] datas = null;
		JSONArray array = null;
		for (int i=0;i<datalist.size();i++) {
			reportResults = new DataReportResults();
			reportResults.setId(UuidUtils.generateUuid());
			reportResults.setReportId(reportId);
			reportResults.setUnitId(unitId);
			reportResults.setOwnerId(ownerId);
			reportResults.setTaskId(taskId);
			reportResults.setRowIndex(orderIndex + i);
			reportResults.setCreationTime(date);
			reportResults.setModifyTime(date);
			reportResults.setType(ReportConstants.RESULT_TYPE_1);
			array = new JSONArray();
			if (Objects.equals(ReportConstants.REPORT_TABLE_TYPE_3, tableType)) {
				datas = new String[colSize];
				System.arraycopy(datalist.get(i), 1, datas, 0, colSize);
			} else {
				datas = datalist.get(i);
			}
			for (int j=0;j<datas.length;j++) {
				if (Objects.equals(columns.get(j).getColumnType(), 3)) {
					array.add(orderIndex + i + "");
				} else {
					if (StringUtils.isNotEmpty(datas[j])) {
						array.add(datas[j]);
						if (columns.get(j).getMethodType() != null) {
							stats[j] = MathUtils.add(stats[j], Double.valueOf(datas[j]));
						}
					} else {
						array.add("null");
					}
				}
			}
			reportResults.setData(array.toJSONString());
			newResultList.add(reportResults);
		}
		if (!coverage) {
			for (String[] oldResult : oldResults) {
				for (int i=0;i<oldResult.length;i++) {
					if (columns.get(i).getMethodType() != null && !Objects.equals(oldResult[i], "null")) {
						stats[i] = MathUtils.add(stats[i], Double.valueOf(oldResult[i]));
					}
				}
			}
		}
		if (needStats) {
			reportResults = new DataReportResults();
			reportResults.setId(UuidUtils.generateUuid());
			reportResults.setReportId(reportId);
			reportResults.setUnitId(unitId);
			reportResults.setOwnerId(ownerId);
			reportResults.setTaskId(taskId);
			if (coverage) {
				reportResults.setRowIndex(datalist.size()+1);
			} else {
				reportResults.setRowIndex(datalist.size() + oldResults.size() + 1);
			}
			reportResults.setCreationTime(date);
			reportResults.setModifyTime(date);
			reportResults.setType(ReportConstants.RESULT_TYPE_2);
			array = new JSONArray();
			for (int i=0;i<colSize;i++) {
				if (Objects.equals(columns.get(i).getMethodType(), 1)) {
					if (coverage) {
						array.add(MathUtils.divString(stats[i], datalist.size(),2));
					} else {
						array.add(MathUtils.divString(stats[i], datalist.size() + oldResults.size(),2));
					}
				} else if (Objects.equals(columns.get(i).getMethodType(), 2)) {
					array.add(MathUtils.roundString(stats[i], 2));
				} else {
					array.add("null"); 
				}
			}
			reportResults.setData(array.toJSONString());
			newResultList.add(reportResults);
		}
		if (CollectionUtils.isNotEmpty(newResultList)) {
			dataReportResultsDao.saveAll(newResultList);
		}
		//删除上传的文件
		file.delete();      
		return "success";
	}
	
	private String analyzeExcelData(int headerSize, List<String[]> dataList, List<DataReportColumn> rowColumns, List<DataReportColumn> rankColumns, Integer tableType){
		if (CollectionUtils.isEmpty(dataList)) {
			return "emptyContent";
		}
		String[] rowNames = null;
		String[] rankNames = null;
		if (CollectionUtils.isNotEmpty(rowColumns)) {
			rowNames = EntityUtils.getArray(rowColumns, DataReportColumn::getColumnName, String[]::new);
		}
		if (CollectionUtils.isNotEmpty(rankColumns)) {
			rankNames = EntityUtils.getArray(rankColumns, DataReportColumn::getColumnName, String[]::new);
		}
		if (Objects.equals(ReportConstants.REPORT_TABLE_TYPE_1, tableType)) {
			if (!sameStrs(rowNames,dataList.get(0))) {
				return "unlike";
			}
		}
		if (Objects.equals(ReportConstants.REPORT_TABLE_TYPE_2, tableType)) {
			if (!sameStrs(rankNames,dataList.get(0))) {
				return "unlike";
			}
		}
		if (Objects.equals(ReportConstants.REPORT_TABLE_TYPE_3, tableType)) {
			String[] rowsList = new String[rowColumns.size()];
			System.arraycopy(dataList.get(0), 1, rowsList, 0, rowsList.length);
			String[] rankList = new String[rankColumns.size()];
			System.arraycopy(dataList.stream().map(str -> str[0]).toArray(String[]::new), 1, rankList, 0, rankList.length);
			if (!sameStrs(rowNames,rowsList) || !sameStrs(rankNames,rankList)) {
				return "unlike";
			}
		}
		dataList.remove(0);
		if (Objects.equals(ReportConstants.REPORT_TABLE_TYPE_3, tableType)) {
			boolean haveData = false;
			outer : for (String[] strs : dataList) {
				for (int i=1;i<strs.length;i++) {
					if (StringUtils.isNotBlank(strs[i])) {
						haveData = true;
						break outer;
					}
				}
			}
			if (!haveData) {
				return "emptyContent";
			}
		} else {
			if (CollectionUtils.isEmpty(dataList)) {
				return "emptyContent";
			}
		}
		
		int rowIndex = 0;
		int rankIndex = 0;
		DataReportColumn column = null;
		List<String[]> errorlist = Lists.newArrayList();
		String[] errStr = null;
		if (Objects.equals(ReportConstants.REPORT_TABLE_TYPE_2, tableType)) {
			rankIndex = 2;
			for (String[] strs : dataList) {
				rowIndex = 0;
				for (String str : strs) {
					column = rankColumns.get(rowIndex);
					if (Objects.equals(column.getIsNotnull(), 1)&&StringUtils.isEmpty(str)) {
						errStr = new String[3];
						errStr[0] = rowIndex + headerSize + 1 + "";
						errStr[1] = "第" + rankIndex + "列";
						errStr[2] = "不能为空！";
						errorlist.add(errStr);
						rowIndex++;
						continue;
					}
					if (Objects.equals(column.getColumnType(), 2)&&StringUtils.isNotEmpty(str)) {
						if (!pattern.matcher(str).matches()) {
							errStr = new String[3];
							errStr[0] = rowIndex + headerSize + 1 + "";
							errStr[1] = "第" + rankIndex + "列";
							errStr[2] = "不是数字！"; 
							errorlist.add(errStr);
							rowIndex++;
							continue;
						} else {
							if (str.length()>13) {
								errStr = new String[3];
								errStr[0] = rowIndex + headerSize + 1 + "";
								errStr[1] = "第" + rankIndex + "列";
								errStr[2] = "数字长度不能大于13位！"; 
								errorlist.add(errStr);
								rowIndex++;
								continue;
							}
						}
					}
					rowIndex++;
				}
				rankIndex++;
			}
		} else {
			for (String[] strs : dataList) {
				rankIndex = 0;
				if (Objects.equals(ReportConstants.REPORT_TABLE_TYPE_3, tableType)) {
					rankIndex = 1;
				}
				for (DataReportColumn col : rowColumns) {
					if (Objects.equals(col.getIsNotnull(), 1)&&StringUtils.isEmpty(strs[rankIndex])) {
						errStr = new String[3];
						errStr[0] = rowIndex + headerSize + 2 + "";
						errStr[1] = col.getColumnName();
						errStr[2] = "不能为空！"; 
						errorlist.add(errStr);
						rankIndex++;
						continue;
					}
					if (Objects.equals(col.getColumnType(), 2)&&StringUtils.isNotEmpty(strs[rankIndex])) {
						if (!pattern.matcher(strs[rankIndex]).matches()) {
							errStr = new String[3];
							errStr[0] = rowIndex + headerSize + 2 + "";
							errStr[1] = col.getColumnName();
							errStr[2] = "不是数字！"; 
							errorlist.add(errStr);
							rankIndex++;
							continue;
						} else {
							if (strs[rankIndex].length()>13) {
								errStr = new String[3];
								errStr[0] = rowIndex + headerSize + 2 + "";
								errStr[1] = col.getColumnName();
								errStr[2] = "数字长度不能大于13位！"; 
								errorlist.add(errStr);
								rankIndex++;
								continue;
							}
						}
					}
					rankIndex++;
				}
				rowIndex++;
			}
		}
		if (CollectionUtils.isNotEmpty(errorlist)) {
			return JSON.toJSONString(errorlist);
		} else {
			return "success";
		}
	}
	
	private boolean sameStrs(String[] strs1,String[] strs2) {
		if (strs1==null||strs2==null||strs1.length==0||strs2.length==0) {
			return false;
		}
		if (strs1.length != strs2.length) {
			return false;
		}
		for (int i=0;i<strs1.length;i++) {
			if (!Objects.equals(strs1[i], strs2[i].replaceAll("\\*", "").trim())) {
				return false;
			}
		}
		return true;
	}
	
	@Override
	public void saveTaskResults(SaveTaskResultDto saveResultDto) throws Exception {
		//删除旧数据
		dataReportResultsDao.deleteByTaskId(saveResultDto.getTaskId());
		Date date = new Date();
		List<DataReportColumn> columns = null;
		if (Objects.equals(saveResultDto.getTableType(), ReportConstants.REPORT_TABLE_TYPE_2)) {
			columns = dataReportColumnService.findByIdAndType(saveResultDto.getReportId(), ReportConstants.REPORT_COLUMN_TYPE_2);
		} else {
			columns = dataReportColumnService.findByIdAndType(saveResultDto.getReportId(), ReportConstants.REPORT_COLUMN_TYPE_1);
		}
		List<DataReportResults> newResultsList = saveResultDto.getDataReportResults().stream().filter(rel -> rel.getRowIndex()!=null).collect(Collectors.toList());
		//判断是否需要统计数据
		boolean needStats = columns.stream().anyMatch(data -> data.getMethodType() != null);
		Double[] numResult = null;
		if (needStats) {
			numResult = new Double[columns.size()];
			for (int i=0;i<columns.size();i++) {
				numResult[i] = 0.0;
			}
		}
		String[] dataArray = null;
		JSONArray array = null;
		for (DataReportResults result : newResultsList) {
			result.setId(UuidUtils.generateUuid());
			result.setReportId(saveResultDto.getReportId());
			result.setUnitId(saveResultDto.getUnitId());
			result.setOwnerId(saveResultDto.getOwnerId());
			result.setTaskId(saveResultDto.getTaskId());
			result.setType(ReportConstants.RESULT_TYPE_1);
			result.setCreationTime(date);
			result.setModifyTime(date);
			dataArray = result.getDataArray();
			array = new JSONArray();
			for (int i=0;i<dataArray.length;i++) {
				if (StringUtils.isNotBlank(dataArray[i])) {
					array.add(dataArray[i]);
					if (columns.get(i).getMethodType() != null) {
						numResult[i] = MathUtils.add(numResult[i], Double.valueOf(dataArray[i]));
					}
				} else {
					array.add("null");
				}
			}
			result.setData(array.toJSONString());
		}
		if (needStats) {
			DataReportResults sumResult = new DataReportResults();
			sumResult.setId(UuidUtils.generateUuid());
			sumResult.setReportId(saveResultDto.getReportId());
			sumResult.setUnitId(saveResultDto.getUnitId());
			sumResult.setOwnerId(saveResultDto.getOwnerId());
			sumResult.setTaskId(saveResultDto.getTaskId());
			sumResult.setType(ReportConstants.RESULT_TYPE_2);
			sumResult.setCreationTime(date);
			sumResult.setModifyTime(date);
			array = new JSONArray();
			for (int i=0;i<columns.size();i++) {
				if (Objects.equals(columns.get(i).getMethodType(), 1)) {
					array.add(MathUtils.divString(numResult[i], newResultsList.size(),2));
				} else if (Objects.equals(columns.get(i).getMethodType(), 2)) {
					array.add(MathUtils.roundString(numResult[i], 2));
				} else {
					array.add("null");
				}
			}
			sumResult.setData(array.toJSONString());
			newResultsList.add(sumResult);
		}
		if (CollectionUtils.isNotEmpty(newResultsList)) {
			dataReportResultsDao.saveAll(newResultsList);
		}
	}
	
	@Override
	protected BaseJpaRepositoryDao<DataReportResults, String> getJpaDao() {
		return dataReportResultsDao;
	}

	@Override
	protected Class<DataReportResults> getEntityClass() {
		return DataReportResults.class;
	}

}
