package net.zdsoft.datareport.data.action;

import java.io.File;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import net.zdsoft.basedata.entity.Attachment;
import net.zdsoft.basedata.entity.Unit;
import net.zdsoft.basedata.remote.service.AttachmentRemoteService;
import net.zdsoft.basedata.remote.service.UnitRemoteService;
import net.zdsoft.datareport.data.constants.ReportConstants;
import net.zdsoft.datareport.data.dto.SaveTaskResultDto;
import net.zdsoft.datareport.data.entity.DataReportColumn;
import net.zdsoft.datareport.data.entity.DataReportInfo;
import net.zdsoft.datareport.data.entity.DataReportResults;
import net.zdsoft.datareport.data.entity.DataReportTask;
import net.zdsoft.datareport.data.entity.DataReportTitle;
import net.zdsoft.datareport.data.service.DataReportColumnService;
import net.zdsoft.datareport.data.service.DataReportInfoService;
import net.zdsoft.datareport.data.service.DataReportResultsService;
import net.zdsoft.datareport.data.service.DataReportTaskService;
import net.zdsoft.datareport.data.service.DataReportTitleService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.DateUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;

import org.apache.commons.collections.CollectionUtils;
import org.apache.curator.shaded.com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


import com.alibaba.fastjson.JSONArray;

@Controller
@RequestMapping("/datareport/taskresult")
public class DataReportAction extends BaseAction{
	
	@Autowired
	private DataReportInfoService dataReportInfoService;
	@Autowired
	private DataReportTaskService dataReportTaskService;
	@Autowired
	private DataReportColumnService dataReportColumnService;
	@Autowired
	private DataReportTitleService dataReportTitleService;
	@Autowired
	private DataReportResultsService dataReportResultsService;
	@Autowired
	private UnitRemoteService unitRemoteService;
	@Autowired
	private AttachmentRemoteService attachmentRemoteService;
	
	@RequestMapping("/showpage")
	@ControllerInfo("单位填报或个人填报")
	public String showPage(String ownerType, ModelMap map) {
		map.put("ownerType", ownerType);
		return "/datareport/taskresult/showpage.ftl";
	}
	
	@RequestMapping("/tasklisthead")
	@ControllerInfo("问卷填报列表head")
	public String showTasksHead(ModelMap map) {
		return "/datareport/taskresult/dataTaskListHead.ftl";
	}
	
	@RequestMapping("/tasklist")
	@ControllerInfo("问卷填报list")
	public String showTaskList(String ownerType, Integer state, ModelMap map, HttpServletRequest request){
		String objectId = "";
		if ("unit".equals(ownerType)) {
			objectId = getLoginInfo().getUnitId();
		} else {
			objectId = getLoginInfo().getOwnerId();
		}
		Pagination page = createPagination();
		List<DataReportTask> dataReportTasks = dataReportTaskService.findTaskInfoList(objectId,state,page);
		map.put("dataReportTasks", dataReportTasks);
		sendPagination(request, map, page);
		return "/datareport/taskresult/dataTaskList.ftl";
	}
	
	@RequestMapping("/showtask")
	@ControllerInfo("提交问卷详情")
	public String showTask(String taskId,ModelMap map) {
		DataReportTask dataReportTask = dataReportTaskService.findByTaskInfo(taskId);
		DataReportInfo dataReportInfo = dataReportInfoService.findOne(dataReportTask.getReportId());
		Unit unit = SUtils.dc(unitRemoteService.findOneById(dataReportInfo.getUnitId()), Unit.class);
		map.put("unitName", unit.getUnitName());
		String ymd = DateUtils.date2String(new Date(), "yyyyMMdd");
		if (dataReportTask.getState() == ReportConstants.REPORT_TASK_STATE_1) {
			String fileDirId = UuidUtils.generateUuid();
			String filePath = ymd +"\\"+ File.separator+fileDirId;
			map.put("fileDirId", fileDirId);
			map.put("filePath", filePath);
		}
		if (dataReportInfo.getIsAttachment() == ReportConstants.IS_ATTACHMENT_1) {
			String accFileDitId = UuidUtils.generateUuid();
			String accFilePath = ymd +"\\"+ File.separator + accFileDitId;
			map.put("accFileDitId", accFileDitId);
			map.put("accFilePath", accFilePath);
			List<Attachment> attachments = SUtils.dt(attachmentRemoteService.findAttachmentDirPathByObjTypeAndId(ReportConstants.REPORT_TASK_ACCESSORY,taskId),new TR<List<Attachment>>() {});
			map.put("attachments", attachments);
			if (CollectionUtils.isNotEmpty(attachments)) {
				map.put("attSize", attachments.size());
			} else {
				map.put("attSize", 0);
			}
		}
		map.put("dataReportTask", dataReportTask);
		map.put("dataReportInfo", dataReportInfo);
		return "/datareport/taskresult/dataTask.ftl";
	}
	
	@RequestMapping("/showtaskresult")
	@ControllerInfo("查看报表详情数据")
	public String showTaskResult(String taskId,String reportId,ModelMap map) {
		//获取表格表头，表尾，备注信息
		StringBuilder title = new StringBuilder();
		List<DataReportTitle> titles = dataReportTitleService.findByReportId(reportId);
		titles.stream().filter(tit-> Objects.equals(ReportConstants.TITLE_TYPE_1, tit.getType())).
				sorted((t1,t2) -> t1.getOrderIndex().compareTo(t2.getOrderIndex())).forEach(tit->{title.append(tit.getContent());});
		map.put("titleName", title.toString());
		Optional<DataReportTitle> remarkTitle = titles.stream().filter(tit->Objects.equals(ReportConstants.TITLE_TYPE_3, tit.getType())).findFirst();
		if (remarkTitle.isPresent()) {
			map.put("remark", remarkTitle.get().getContent());
		}
		//获取不同类型的表格标题
		DataReportInfo info = dataReportInfoService.findOne(reportId);
		List<DataReportColumn> columns = null;
		List<DataReportColumn> rowColumns = null;
		List<DataReportColumn> rankColumns = null;
		int colNums = 0;
		if (Objects.equals(ReportConstants.REPORT_TABLE_TYPE_1, info.getTableType())) {
			columns = dataReportColumnService.findByIdAndType(reportId, ReportConstants.REPORT_COLUMN_TYPE_1);
			colNums = columns.size();
			map.put("columns", columns);
		} else if (Objects.equals(ReportConstants.REPORT_TABLE_TYPE_2, info.getTableType())) {
			columns = dataReportColumnService.findByIdAndType(reportId, ReportConstants.REPORT_COLUMN_TYPE_2);
			colNums = columns.size();
			map.put("columns", columns);
		} else {
			columns = dataReportColumnService.findByReportId(reportId);
			rowColumns = columns.stream().filter(row -> Objects.equals(ReportConstants.REPORT_COLUMN_TYPE_1, row.getType())).sorted(Comparator.comparing(DataReportColumn::getColumnIndex)).collect(Collectors.toList());
			colNums = rowColumns.size();
			rankColumns = columns.stream().filter(row -> Objects.equals(ReportConstants.REPORT_COLUMN_TYPE_2, row.getType())).sorted(Comparator.comparing(DataReportColumn::getColumnIndex)).collect(Collectors.toList());
			map.put("columns", rowColumns);
			map.put("rankColumns", rankColumns);
		}
		//获取表格内容数据
		List<DataReportResults> dataReportResults = dataReportResultsService.findByTaskId(taskId);
		//获取统计数据
		List<DataReportResults> sumDataResults = dataReportResults.stream().filter(data->Objects.equals(data.getType(), ReportConstants.RESULT_TYPE_2)).collect(Collectors.toList());
		String[] sumResult = null;
		if (CollectionUtils.isNotEmpty(sumDataResults)) {
			sumResult = JSONArray.parseArray(sumDataResults.get(0).getData()).toArray(new String[colNums]);
			map.put("sumResult", sumResult);
			dataReportResults.removeAll(sumDataResults);
		}
		//获取普通数据
		List<String[]> resultsList = Lists.newArrayList();
		for (DataReportResults reportResults : dataReportResults) {
			resultsList.add(JSONArray.parseArray(reportResults.getData()).toArray(new String[colNums]));
		}
		map.put("resultsList", resultsList);
		if (Objects.equals(ReportConstants.REPORT_TABLE_TYPE_1, info.getTableType())) {
			return "/datareport/taskresult/dataTaskResultOne.ftl";
		} else if (Objects.equals(ReportConstants.REPORT_TABLE_TYPE_2, info.getTableType())) {
			return "/datareport/taskresult/dataTaskResultTwo.ftl";
		} else {
			return "/datareport/taskresult/dataTaskResultThree.ftl";
		}
	}
	
	@ResponseBody
	@RequestMapping("/savetaskresults")
	@ControllerInfo("在线保存数据")
	public String saveTaskResults(SaveTaskResultDto saveResultDto) {
		saveResultDto.setUnitId(getLoginInfo().getUnitId());
		saveResultDto.setOwnerId(getLoginInfo().getOwnerId());
		try {
			dataReportResultsService.saveTaskResults(saveResultDto);
		} catch (Exception e) {
			e.printStackTrace();
			return error("保存失败！"+e.getMessage());
		}
		return success("保存成功");
	}
	
	@ResponseBody
	@RequestMapping("/saveexceldata")
	@ControllerInfo("校验和保存Excel文件数据")
	public String saveExcelData(String path, String taskId, String reportId, Integer tableType, boolean coverage) {
		String unitId = getLoginInfo().getUnitId();
		String ownerId = getLoginInfo().getOwnerId();
		String msg = "";
		try {
			msg = dataReportResultsService.saveDataResult(path, taskId,reportId,tableType,unitId,ownerId,coverage);
		} catch (Exception e) {
			e.printStackTrace();
			return error("error");
		}
		if (Objects.equals("success", msg)) {
			return success(msg);
		} else {
			return error(msg);
		}
	}
	
	@ResponseBody
	@RequestMapping("/submitdata")
	@ControllerInfo("修改任务报表状态-上报问卷")
	public String submitOneData(String taskId, Integer state,ModelMap map){
		try{
			dataReportTaskService.updateState(state,taskId);
		}catch (Exception e) {
			e.printStackTrace();
			return error("保存失败！"+e.getMessage());
		}
		return success("保存成功");
	}
	
	@RequestMapping("/refuploadfile")
	@ControllerInfo("刷新附件上传")
	public String refUploadFile(String accFileDitId, String fileNum, String canUse,ModelMap map) {
		map.put("accFileDitId", accFileDitId);
		map.put("fileNum", fileNum);
		map.put("canUse", canUse);
		return "/datareport/taskresult/uploadAcc.ftl";
	}
}
