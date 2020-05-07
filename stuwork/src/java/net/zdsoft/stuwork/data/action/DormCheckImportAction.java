package net.zdsoft.stuwork.data.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.remote.service.SemesterRemoteService;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.dataimport.DataImportAction;
import net.zdsoft.framework.utils.DateUtils;
import net.zdsoft.framework.utils.ExcelUtils;
import net.zdsoft.framework.utils.ExportUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.stuwork.data.constants.StuworkConstants;
import net.zdsoft.stuwork.data.entity.DyDormBuilding;
import net.zdsoft.stuwork.data.entity.DyDormRoom;
import net.zdsoft.stuwork.data.service.DyDormBuildingService;
import net.zdsoft.stuwork.data.service.DyDormCheckResultService;
import net.zdsoft.stuwork.data.service.DyDormRoomService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;


@Controller
@RequestMapping("/stuwork/check/checkImport")
public class DormCheckImportAction extends DataImportAction{
private Logger logger = Logger.getLogger(DormCheckImportAction.class);
	
	@Autowired
	private DyDormRoomService dyDormRoomService;
	@Autowired
	private DyDormBuildingService dyDormBuildingService;
	@Autowired
	private DyDormCheckResultService dyDormCheckResultService;
	@Autowired
	private SemesterRemoteService semesterRemoteService;
	
	@ControllerInfo("进入导入首页")
	@RequestMapping("/main")
	public String execute(ModelMap map,Date searchDate,String searchBuildId) {
		// 业务名称
		map.put("businessName", "考核录入");
		// 导入URL 
		map.put("businessUrl", "/stuwork/check/checkImport/import");
		// 导入模板
		String searchDateStr=DateUtils.date2String(searchDate,"yyyy-MM-dd");
		map.put("templateDownloadUrl", "/stuwork/check/checkImport/template?searchDate="+searchDateStr+"&buildingId="+searchBuildId);
		// 导入对象
		map.put("objectName", getObjectName());
		// 导入说明
		map.put("description", getDescription());
		
		DyDormBuilding building=dyDormBuildingService.findOne(searchBuildId);
		
		map.put("searchDate", searchDate);
		map.put("buildingName", building!=null?building.getName():"");
		map.put("buildingId", searchBuildId);
		
		map.put("businessKey", UuidUtils.generateUuid());

		return "/stuwork/dorm/check/dormCheckImport.ftl";
	}

	@Override
	public String getObjectName() {
		return "checkImport";
	};

	@Override
	public String getDescription() {
		return "<h4 class='box-graybg-title'>说明</h4>"
				+ "<p>1、导入文件中请确认数据是否正确</p>";
	};

	@Override
	public List<String> getRowTitleList() {
		List<String> tis = new ArrayList<String>();
		tis.add("寝室号");
		tis.add("卫生");
		tis.add("内务");
		tis.add("纪律");
		tis.add("扣分");
		return tis;
	}
	
	@Override
	@RequestMapping("/template")
	public void downloadTemplate(HttpServletRequest request,
			HttpServletResponse response) {
		String buildingId=request.getParameter("buildingId");
		String searchDate=request.getParameter("searchDate");
		List<String> tis = getRowTitleList();
		Map<String, List<Map<String, String>>> sheetName2RecordListMap = new HashMap<String, List<Map<String, String>>>();
		List<Map<String,String>> recordList = new ArrayList<Map<String, String>>();
		//加入模板数据
		getRecordList(recordList,searchDate,buildingId);
		DyDormBuilding building=dyDormBuildingService.findOne(buildingId);
		
		sheetName2RecordListMap.put(getObjectName(),recordList);
		Map<String, List<String>> titleMap = new HashMap<String, List<String>>();
		titleMap.put(getObjectName(), tis);
		ExportUtils ex = ExportUtils.newInstance();
		ex.exportXLSFile(searchDate+"日"+building.getName()+"寝室考核导入", titleMap, sheetName2RecordListMap, response);
	}
	
	private void getRecordList(List<Map<String, String>> recordList,String searchDate,String buildingId){
		String unitId = getLoginInfo().getUnitId();
		
		List<DyDormRoom> roomList=dyDormRoomService.findByName(unitId, null, buildingId,StuworkConstants.STU_TYPE);
		if(CollectionUtils.isNotEmpty(roomList)){
			Map<String,String> valueMap = null;
			for(DyDormRoom room:roomList){
				valueMap=new HashMap<String, String>();
				valueMap.put("寝室号",room.getRoomName());
				valueMap.put("卫生", "10");
				valueMap.put("内务","10");
				valueMap.put("纪律","10");
				recordList.add(valueMap);
			}
		}
	}
	@Override
	@RequestMapping("/import")
	@ResponseBody
	public String dataImport(String filePath,String params) {
		logger.info("业务数据处理中......");
		List<String[]> datas = ExcelUtils.readExcel(filePath,
				getRowTitleList().size());
		System.out.println(datas.size());
		Semester se = SUtils.dc(semesterRemoteService.getCurrentSemester(1, getLoginInfo().getUnitId()), Semester.class);
		String acadyear = se.getAcadyear();
		String semester = se.getSemester()+"";
		JSONObject json = JSONObject.parseObject(params);
		String searchDate=json.get("searchDate").toString();
		//String searchDate="2017-08-18";//TODO
		String buildingId=json.get("buildingId").toString();
		// 业务处理模块　具体的业务在这里处理
		//自己的处理接口　数据范围格式如下
		//业务类中返回给我的一个json格式的数据
		String jsonMsg =dyDormCheckResultService.doCheckImport(getLoginInfo().getUnitId(), acadyear,semester,getLoginInfo().getUserId(), buildingId,searchDate, datas);
		logger.info("导入结束......");
		return jsonMsg;
	}

}
