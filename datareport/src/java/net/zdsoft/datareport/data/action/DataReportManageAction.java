package net.zdsoft.datareport.data.action;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import net.zdsoft.basedata.entity.Attachment;
import net.zdsoft.basedata.remote.service.AttachmentRemoteService;
import net.zdsoft.datareport.data.constants.ReportConstants;
import net.zdsoft.datareport.data.dto.DataReportSaveDto;
import net.zdsoft.datareport.data.entity.DataReportColumn;
import net.zdsoft.datareport.data.entity.DataReportInfo;
import net.zdsoft.datareport.data.entity.DataReportObj;
import net.zdsoft.datareport.data.entity.DataReportResults;
import net.zdsoft.datareport.data.entity.DataReportTask;
import net.zdsoft.datareport.data.entity.DataReportTitle;
import net.zdsoft.datareport.data.service.DataReportColumnService;
import net.zdsoft.datareport.data.service.DataReportInfoService;
import net.zdsoft.datareport.data.service.DataReportObjService;
import net.zdsoft.datareport.data.service.DataReportResultsService;
import net.zdsoft.datareport.data.service.DataReportTaskService;
import net.zdsoft.datareport.data.service.DataReportTitleService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.MathUtils;
import net.zdsoft.framework.utils.SUtils;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.curator.shaded.com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


import com.alibaba.fastjson.JSONArray;

@Controller
@RequestMapping("/datareport/infomanage")
public class DataReportManageAction extends BaseAction{
	
	@Autowired
	private DataReportInfoService dataReportInfoService;
	@Autowired
	private DataReportObjService dataReportObjService;		
	@Autowired
	private DataReportTaskService dataReportTaskService;
	@Autowired
	private DataReportResultsService dataReportResultsService;
	@Autowired
	private DataReportColumnService dataReportColumnService;
	@Autowired
	private DataReportTitleService dataReportTitleService;
	@Autowired
	private AttachmentRemoteService attachmentRemoteService;
	
	@RequestMapping("/showpage")
	@ControllerInfo("发布报表list")
	public String showPage() {
		return "/datareport/infomanage/showpage.ftl";
	}
	
	@RequestMapping("/showinfolisthead")
	@ControllerInfo("发布信息列表筛选")
	public String showInfosHead(ModelMap map) {
        return "/datareport/infomanage/dataInfoHead.ftl";
	}
	
	@RequestMapping("/showinfolist")
	@ControllerInfo("发布报表list")
	public String showReportInfoList(String title,ModelMap map, HttpServletRequest request) {
		String unitId = getLoginInfo().getUnitId();
		Pagination page = createPagination();
		try {
			title = URLDecoder.decode(title,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		List<DataReportInfo> dataReportInfos = dataReportInfoService.findInfoList(title,unitId,page);
		sendPagination(request, map, page);
		map.put("dataReportInfos", dataReportInfos);
		return "/datareport/infomanage/dataInfoList.ftl";
	}
	
	@ResponseBody
	@RequestMapping("/deleteorrevoke")
	@ControllerInfo("删除或撤销报表任务")
	public String deleteOrRevoke(String infoId, Integer type) {
		String unitId = getLoginInfo().getUnitId();
		try {
			dataReportInfoService.deleteOrRevokeInfo(infoId,unitId,type);
		} catch (Exception e) {
			e.printStackTrace();
			return error("失败");
		}
		return success("成功");
	}
	
	@ResponseBody
	@RequestMapping("/copyinfosave")
	@ControllerInfo("复制文件")
	public String copyInfoSave(String infoId) {
		try{
			dataReportInfoService.saveCopyInfo(infoId);
		}catch (Exception e) {
			e.printStackTrace();
			return error(e.getMessage());
		}
		return success("复制成功");
	}
	
	@ResponseBody
	@RequestMapping("/updatetime")
	@ControllerInfo("修改结束时间")
	public String updateEndTime(String infoId,String endTime,Integer state) {
		String unitId = getLoginInfo().getUnitId();
		try {
			dataReportInfoService.updateTime(infoId,endTime,state,unitId);
		} catch (Exception e) {
			e.printStackTrace();
			return error("修改失败!");
		}
		return success("修改成功!");
	}
	
	@RequestMapping("/editinfo")
	@ControllerInfo("新增或修改发布报表")
	public String reportInfoAdd(String infoId, ModelMap map) {
		String unitId = getLoginInfo().getUnitId();
		String ownerId = getLoginInfo().getOwnerId();
		Integer unitClass = getLoginInfo().getUnitClass();
		map.put("unitId", unitId);
		map.put("ownerId", ownerId);
		map.put("unitClass", unitClass);
		StringBuilder title = new StringBuilder();
		String remark = "";
		DataReportInfo dataReportInfo = new DataReportInfo();
		List<DataReportColumn> rowColumns = Lists.newArrayList();
		List<DataReportColumn> rankColumns = Lists.newArrayList();
		String objectIds = "";
		String objectNames = "";
		if (StringUtils.isNotEmpty(infoId)) {
			dataReportInfo = dataReportInfoService.findOne(infoId);
			List<DataReportTitle> titles = dataReportTitleService.findByReportId(infoId);
			List<DataReportTitle> headers = titles.stream().filter(tit-> Objects.equals(ReportConstants.TITLE_TYPE_1, tit.getType())).
					sorted((t1,t2) -> t1.getOrderIndex().compareTo(t2.getOrderIndex())).collect(Collectors.toList());
			for (DataReportTitle header : headers) {
				title.append(header.getContent());
			}	
			Optional<DataReportTitle> remarkTitle = titles.stream().filter(tit->Objects.equals(ReportConstants.TITLE_TYPE_3, tit.getType())).findFirst();
			if (remarkTitle.isPresent()) {
				remark = remarkTitle.get().getContent();
			}
			rowColumns = dataReportColumnService.findByIdAndType(infoId, ReportConstants.REPORT_COLUMN_TYPE_1);
			rankColumns = dataReportColumnService.findByIdAndType(infoId, ReportConstants.REPORT_COLUMN_TYPE_2);
			List<DataReportObj> objs = dataReportObjService.findByReportIds(new String[]{infoId}, true);
			for (DataReportObj dataReportObj : objs) {
				objectIds += "," + dataReportObj.getObjectId();
				objectNames += "," + dataReportObj.getObjectName();
			}
			objectIds = objectIds.substring(1);
			objectNames = objectNames.substring(1);
		}
		map.put("dataReportInfo", dataReportInfo);
		map.put("title", title.toString());
		map.put("remark", remark);
		map.put("rowColumns", rowColumns);
		map.put("rankColumns", rankColumns);
		map.put("objectIds", objectIds);
		map.put("objectNames", objectNames);
		return "/datareport/infomanage/dataInfoEdit.ftl";
	}

	@ResponseBody
	@RequestMapping("/sametitle")
	@ControllerInfo("查找问卷名称是否重复")
	public String sameTitle(String infoId, String infoTitle) {
		String unitId = getLoginInfo().getUnitId();
		DataReportInfo reportInfo = dataReportInfoService.findSameTitle(infoTitle,unitId);
		if (StringUtils.isEmpty(infoId)&&reportInfo!=null ||
				StringUtils.isNotEmpty(infoId)&&reportInfo!=null&&!reportInfo.getId().equals(infoId)) {
			return error("重复");
		} else {
			return success("不重复");
		}
	}
	
	@ResponseBody
	@RequestMapping("/saveinfo")
	@ControllerInfo("保存一条新的问卷")
	public String reportInfoSave(DataReportSaveDto saveDto, boolean isSub) {
		DataReportInfo info = null;
		try{
			info = dataReportInfoService.saveOneInfo(saveDto,isSub);
		}catch (Exception e) {
			e.printStackTrace();
			return error(e.getMessage());
		}
		return success(info.getId());
	}
	
	@RequestMapping("/showinfohead")
	@ControllerInfo("展示问卷详情")
	public String shwoInfoHead(String infoId,ModelMap map) {
		map.put("infoId", infoId);
		return "/datareport/infomanage/dataTaskHead.ftl";
	}
	
	@RequestMapping("/showinfo")
	@ControllerInfo("展示问卷详情")
	public String showInfoCont(String infoId, ModelMap map) {
		DataReportInfo dataReportInfo = dataReportInfoService.findOne(infoId);
		List<DataReportObj> dataReportObjs = dataReportObjService.findByReportIds(new String[]{infoId}, true);
		StringBuilder title = new StringBuilder();
		List<DataReportTitle> titles = dataReportTitleService.findByReportId(infoId);
		titles.stream().filter(tit->Objects.equals(ReportConstants.TITLE_TYPE_1, tit.getType())).
				sorted((t1,t2) -> t1.getOrderIndex().compareTo(t2.getOrderIndex())).forEach(tit->{title.append(tit.getContent());});
		map.put("title", title.toString());
		Optional<DataReportTitle> remarkTitle = titles.stream().filter(tit->Objects.equals(ReportConstants.TITLE_TYPE_3, tit.getType())).findFirst();
		if (remarkTitle.isPresent()) {
			map.put("remark", remarkTitle.get().getContent());
		}
		List<DataReportColumn> rowColumns = dataReportColumnService.findByIdAndType(infoId, ReportConstants.REPORT_COLUMN_TYPE_1);
		List<DataReportColumn> rankColumns = dataReportColumnService.findByIdAndType(infoId, ReportConstants.REPORT_COLUMN_TYPE_2);
		map.put("dataReportInfo", dataReportInfo);
		map.put("dataReportObjs", dataReportObjs);
		map.put("rowColumns", rowColumns);
		map.put("rankColumns", rankColumns);
		return "/datareport/infomanage/dataInfoDetails.ftl";
	}
	
	@RequestMapping("/showtasklist")
	@ControllerInfo("展示发布报表对象")
	public String showReportTask(String infoId, ModelMap map, HttpServletRequest request) {
		DataReportInfo info = dataReportInfoService.findOne(infoId);
		Pagination page = createPagination();
		List<DataReportTask> dataReportTasks = dataReportTaskService.findByReportId(infoId,page);    
		sendPagination(request, map, page);
		List<Attachment> attachments = null;
		if (CollectionUtils.isNotEmpty(dataReportTasks)) {
			String[] taskIds = EntityUtils.getArray(dataReportTasks, DataReportTask::getId, String[]::new); 
			if (Objects.equals(info.getIsAttachment(), ReportConstants.IS_ATTACHMENT_1)) {
				attachments = SUtils.dt(attachmentRemoteService.findAttachmentDirPathByObjTypeAndId(ReportConstants.REPORT_TASK_ACCESSORY,taskIds),new TR<List<Attachment>>(){});
				Map<String,Long> attNumMap = attachments.stream().collect(Collectors.groupingBy(Attachment::getObjId,Collectors.counting()));
				for (DataReportTask task : dataReportTasks) {
					if (attNumMap.get(task.getId()) != null) {
						task.setFileNum(attNumMap.get(task.getId()).intValue());
					} else {
						task.setFileNum(0);
					}
				}
			}
			attachments = SUtils.dt(attachmentRemoteService.findAttachmentDirPathByObjTypeAndId(ReportConstants.REPORT_TASK_ATT,taskIds),new TR<List<Attachment>>(){});
			Map<String,Attachment> excelAttMap = EntityUtils.getMap(attachments,Attachment::getObjId);
			for (DataReportTask task : dataReportTasks) {
				if (excelAttMap.get(task.getId()) != null) {
					task.setExistExcel(true);
				} else {
					task.setExistExcel(false);
				}
			}
		}
		Boolean statistics = false;
		if (ReportConstants.REPORT_INFO_STATE_4.equals(info.getState())) {
			statistics = true;
		}
		attachments = SUtils.dt(attachmentRemoteService.findAttachmentDirPathByObjTypeAndId(ReportConstants.REPORT_INFO_STATS, infoId),new TR<List<Attachment>>(){});
		if (CollectionUtils.isNotEmpty(attachments)) {
			map.put("haveStats", true);
		} else {
			map.put("haveStats", false);
		}
		map.put("dataReportTasks", dataReportTasks);
		map.put("statistics", statistics);
		map.put("infoId", infoId);
		map.put("tableType", info.getTableType());
		map.put("isAttachment", info.getIsAttachment());
		return "/datareport/infomanage/dataTaskList.ftl";
	}
	
	@RequestMapping("/showresulthead")
	@ControllerInfo("展示数据Head")
	public String showResultHead(String infoId, String taskId, boolean existExcel, Integer fileNum, ModelMap map) {
		DataReportInfo dataReportInfo = dataReportInfoService.findOne(infoId);
		map.put("taskId", taskId);
		map.put("infoId", infoId);
		map.put("existExcel", existExcel);
		map.put("fileNum", fileNum);
		map.put("tableType", dataReportInfo.getTableType());
		map.put("isAttachment", dataReportInfo.getIsAttachment());
		return "/datareport/infomanage/dataResultHead.ftl";
	}
	
	@RequestMapping("/showresults")
	@ControllerInfo("展示数据情况")
	public String showResults(String infoId, String taskId, Integer tableType, ModelMap map) {
		//获取标题备注
		StringBuilder title = new StringBuilder();
		List<DataReportTitle> titles = dataReportTitleService.findByReportId(infoId);
		titles.stream().filter(tit->Objects.equals(ReportConstants.TITLE_TYPE_1, tit.getType())).
				sorted((t1,t2) -> t1.getOrderIndex().compareTo(t2.getOrderIndex())).forEach(tit->{title.append(tit.getContent());});
		map.put("titleName", title.toString());
		Optional<DataReportTitle> remarkTitle = titles.stream().filter(tit->Objects.equals(ReportConstants.TITLE_TYPE_3, tit.getType())).findFirst();
		if (remarkTitle.isPresent()) {
			map.put("remark", remarkTitle.get().getContent());
		}
		List<DataReportColumn> rowColumns = null;
		List<DataReportColumn> rankColumns = null;
		int colNums = 0;
		if (Objects.equals(ReportConstants.REPORT_TABLE_TYPE_1, tableType)) {
			rowColumns = dataReportColumnService.findByIdAndType(infoId, ReportConstants.REPORT_COLUMN_TYPE_1);
			colNums = rowColumns.size();
			map.put("rowColumns", rowColumns);
		} else if (Objects.equals(ReportConstants.REPORT_TABLE_TYPE_2, tableType)) {
			rankColumns = dataReportColumnService.findByIdAndType(infoId, ReportConstants.REPORT_COLUMN_TYPE_2);
			colNums = rankColumns.size();
			map.put("rankColumns", rankColumns);
		} else {
			rowColumns = dataReportColumnService.findByIdAndType(infoId, ReportConstants.REPORT_COLUMN_TYPE_1);
			rankColumns = dataReportColumnService.findByIdAndType(infoId, ReportConstants.REPORT_COLUMN_TYPE_2);
			colNums = rowColumns.size();
			map.put("rowColumns", rowColumns);
			map.put("rankColumns", rankColumns);
		}
		List<DataReportResults> dataReportResults = dataReportResultsService.findByTaskId(taskId);
		List<DataReportResults> sumResults = dataReportResults.stream().filter(data->Objects.equals(ReportConstants.RESULT_TYPE_2, data.getType())).collect(Collectors.toList());
		if (CollectionUtils.isNotEmpty(sumResults)) {
			dataReportResults.removeAll(sumResults);
			map.put("sumResult", JSONArray.parseArray(sumResults.get(0).getData()).toArray(new String[colNums]));
		}
		List<String[]> resultsList = Lists.newArrayList();
		for (DataReportResults result : dataReportResults) {
			resultsList.add(JSONArray.parseArray(result.getData()).toArray(new String[colNums]));
		}
		map.put("resultsList", resultsList);
		if (Objects.equals(ReportConstants.REPORT_TABLE_TYPE_1, tableType)) {
			return "/datareport/infomanage/dataResultOne.ftl";
		} else if (Objects.equals(ReportConstants.REPORT_TABLE_TYPE_2, tableType)) {
			return "/datareport/infomanage/dataResultTwo.ftl";
		} else {
			return "/datareport/infomanage/dataResultThree.ftl";
		}
	} 
	
	@RequestMapping("/common/showtaskPDF")
	@ControllerInfo("展示数据PDF")
	public String showTaskPDF(String infoId, String taskId, Integer tableType, Integer type,ModelMap map) {
		map.put("tableType", tableType);
		StringBuilder title = new StringBuilder();
		List<DataReportTitle> titles = dataReportTitleService.findByReportId(infoId);
		List<DataReportTitle> headers = titles.stream().filter(tit->Objects.equals(ReportConstants.TITLE_TYPE_1, tit.getType())).
				sorted((t1,t2) -> t1.getOrderIndex().compareTo(t2.getOrderIndex())).collect(Collectors.toList());
		for (DataReportTitle header : headers) {
			title.append(header.getContent());
		}	
		map.put("titleName", title.toString());
		List<DataReportColumn> columns = null;
		List<DataReportColumn> rankColumns = null;
		int colNums = 0;
		boolean needStat = false;
		if (Objects.equals(ReportConstants.REPORT_TABLE_TYPE_1, tableType)) {
			columns = dataReportColumnService.findByIdAndType(infoId, ReportConstants.REPORT_COLUMN_TYPE_1);
			colNums = columns.size();
			needStat = columns.stream().anyMatch(data -> data.getMethodType() != null);
			map.put("rowColumns", columns);
		} else if (Objects.equals(ReportConstants.REPORT_TABLE_TYPE_2, tableType)) {
			columns = dataReportColumnService.findByIdAndType(infoId, ReportConstants.REPORT_COLUMN_TYPE_2);
			colNums = columns.size();
			needStat = columns.stream().anyMatch(data -> data.getMethodType() != null);
			map.put("rankColumns", columns);
		} else {
			columns = dataReportColumnService.findByIdAndType(infoId, ReportConstants.REPORT_COLUMN_TYPE_1);
			rankColumns = dataReportColumnService.findByIdAndType(infoId, ReportConstants.REPORT_COLUMN_TYPE_2);
			colNums = columns.size();
			needStat = columns.stream().anyMatch(data -> data.getMethodType() != null);
			map.put("rowColumns", columns);
			map.put("rankColumns", rankColumns);
		}
		List<DataReportResults> dataReportResults = null;
		if (Objects.equals(type, 1)) {
			dataReportResults = dataReportResultsService.findByTaskId(taskId);
		} else {
			dataReportResults = dataReportResultsService.findByInfoIdAndType(infoId,ReportConstants.RESULT_TYPE_1);
			Map<String,List<DataReportResults>> dataMap = dataReportResults.stream().collect(Collectors.groupingBy(DataReportResults::getTaskId));
			dataReportResults = Lists.newArrayList();
			for (Map.Entry<String,List<DataReportResults>> entry : dataMap.entrySet()) {
				dataReportResults.addAll(entry.getValue());
			}
		}
		List<String[]> resultsList = Lists.newArrayList();
		String[] sumResults = null;
		if (Objects.equals(type, 1)) {
			if (needStat) {
				Optional<DataReportResults> sumResult = dataReportResults.stream().filter(data->Objects.equals(data.getType(), ReportConstants.RESULT_TYPE_2)).findFirst();
				if (sumResult.isPresent()) {
					sumResults = JSONArray.parseArray(sumResult.get().getData()).toArray(new String[colNums]);
					dataReportResults.remove(sumResult.get());
				}
			}
			for (DataReportResults result : dataReportResults) {
				resultsList.add(JSONArray.parseArray(result.getData()).toArray(new String[colNums]));
			}
		} else {
			Double[] stats = null;
			if (needStat) {
				stats = new Double[colNums];
				for (int i=0;i<columns.size();i++) {
					stats[i] = 0.0;
				}
			}
			String[] resultStr = null;
			for (DataReportResults result : dataReportResults) {
				resultStr = JSONArray.parseArray(result.getData()).toArray(new String[colNums]);
				if (needStat) {
					for (int i=0;i<columns.size();i++) {
						if (columns.get(i).getMethodType()!=null && StringUtils.isNotBlank(resultStr[i])) {
							stats[i] = MathUtils.add(stats[i], Double.valueOf(resultStr[i]));
						}
					}
				}
				resultsList.add(resultStr);
			}
			if (needStat) {
				sumResults = new String[colNums];
				for (int i=0;i<columns.size();i++) {
					if (Objects.equals(columns.get(i).getMethodType(), 1)) {
						sumResults[i] = MathUtils.divString(stats[i], dataReportResults.size(),2);
					} else if (Objects.equals(columns.get(i).getMethodType(), 2)) {
						sumResults[i] = MathUtils.roundString(stats[i], 2);
					} else {
						sumResults[i] = "null";
					}
				}
			}
		}
		map.put("sumResults", sumResults);
		map.put("resultsList", resultsList);
		return "/datareport/infomanage/dataResultPDF.ftl";
	}
	
}
