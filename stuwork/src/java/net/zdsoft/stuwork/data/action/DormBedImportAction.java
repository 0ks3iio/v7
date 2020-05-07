package net.zdsoft.stuwork.data.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.dataimport.DataImportAction;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.ExcelUtils;
import net.zdsoft.framework.utils.ExportUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.stuwork.data.entity.DyDormBed;
import net.zdsoft.stuwork.data.entity.DyDormBuilding;
import net.zdsoft.stuwork.data.entity.DyDormRoom;
import net.zdsoft.stuwork.data.service.DyDormBedService;
import net.zdsoft.stuwork.data.service.DyDormBuildingService;
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
@RequestMapping("/stuwork/bedImport")
public class DormBedImportAction extends DataImportAction{
private Logger logger = Logger.getLogger(DormBedImportAction.class);
	
	@Autowired
	private DyDormRoomService dyDormRoomService;
	@Autowired
	private DyDormBuildingService dyDormBuildingService;
	@Autowired
	private DyDormBedService dyDormBedService;
	
	@ControllerInfo("进入导入首页")
	@RequestMapping("/main")
	public String execute(ModelMap map,String acadyear,String semester,String buildingId,String roomProperty) {
		// 业务名称
		String str="1".equals(roomProperty)?"学生":"教师";
		map.put("businessName", str+"住宿安排");
		// 导入URL 
		map.put("businessUrl", "/stuwork/bedImport/import");
		// 导入模板
		map.put("templateDownloadUrl", "/stuwork/bedImport/template?acadyear="+acadyear+"&semester="+semester+"&buildingId="+buildingId+"&roomProperty="+roomProperty);
		// 导入对象
		map.put("objectName", getObjectName());
		// 导入说明
		map.put("description", getDescription());
		
		DyDormBuilding building=dyDormBuildingService.findOne(buildingId);
		
		map.put("acadyear", acadyear);
		map.put("semester", semester);
		map.put("buildingName", building!=null?building.getName():"");
		map.put("buildingId", buildingId);
		map.put("roomProperty", roomProperty);
		
		map.put("businessKey", UuidUtils.generateUuid());

		return "/stuwork/dorm/dormBedImport.ftl";
	}

	@Override
	public String getObjectName() {
		return "bedImport";
	};

	@Override
	public String getDescription() {
		return "<h4 class='box-graybg-title'>说明</h4>"
				+ "<p>1、导入文件中请确认数据是否正确，且根据列表页选择的学年导入对应的数据</p>"
				+ "<p>2、导入学生信息不能重复</p>"
				+ "<p>3、导入班级名称为年级名称+班级名称</p>";
	};

	@Override
	public List<String> getRowTitleList() {
		List<String> tis = new ArrayList<String>();
		tis.add("寝室楼");
		//tis.add("寝室类别");
		tis.add("寝室号");
		//tis.add("楼层");
		//tis.add("容纳人数");
		tis.add("床位号");
		tis.add("班级");
		tis.add("学号");
		tis.add("姓名");
		tis.add("备注");
		return tis;
	}
	public List<String> getRowTitleList1(String roomProperty) {
		List<String> tis = new ArrayList<String>();
		tis.add("寝室楼");
		tis.add("寝室号");
		tis.add("床位号");
		if("1".equals(roomProperty)){
			tis.add("班级");
			tis.add("学号");
		}else{
			tis.add("教师号");
		}
		tis.add("姓名");
		tis.add("备注");
		return tis;
	}
	
	@Override
	@RequestMapping("/template")
	public void downloadTemplate(HttpServletRequest request,
			HttpServletResponse response) {
		String semester=request.getParameter("semester");
		String roomProperty=request.getParameter("roomProperty");

		List<String> tis = getRowTitleList1(roomProperty);
		Map<String, List<Map<String, String>>> sheetName2RecordListMap = new HashMap<String, List<Map<String, String>>>();
		List<Map<String,String>> recordList = new ArrayList<Map<String, String>>();
		String ss="1".equals(semester)?"第一学期":"第二学期";

		String str="1".equals(roomProperty)?"学生":"教师";
		//加入模板数据
		getRecordList(recordList,request.getParameter("acadyear"),semester,request.getParameter("buildingId"),roomProperty);
		sheetName2RecordListMap.put(getObjectName(),recordList);
		Map<String, List<String>> titleMap = new HashMap<String, List<String>>();
		titleMap.put(getObjectName(), tis);
		ExportUtils ex = ExportUtils.newInstance();
		ex.exportXLSFile(request.getParameter("acadyear")+"学年"+ss+str+"住宿安排导入", titleMap, sheetName2RecordListMap, response);
	}
	
	private void getRecordList(List<Map<String, String>> recordList,String acadyear,String semester,
			String buildingId,String roomProperty){
		String unitId = getLoginInfo().getUnitId();
		
		Map<String,String> buildingMap=EntityUtils.getMap(dyDormBuildingService.findByUnitId(unitId),"id", "name");
		boolean isStu="1".equals(roomProperty);
		List<DyDormRoom> roomList=dyDormRoomService.getDormRoomByUnitId(unitId, buildingId, null,acadyear,semester,null,null,null,roomProperty,null);
		if(CollectionUtils.isNotEmpty(roomList)){
			Map<String,String> valueMap = null;
			for(DyDormRoom room:roomList){
				List<DyDormBed> bedList=room.getBedList();
				if(CollectionUtils.isNotEmpty(bedList)){
					for(DyDormBed bed:bedList){
						valueMap=new HashMap<String, String>();
						valueMap.put("寝室楼", buildingMap.get(room.getBuildingId()));
						valueMap.put("寝室号",room.getRoomName());
						valueMap.put("床位号",String.valueOf(bed.getNo()));
						if(isStu){
							valueMap.put("班级",bed.getClassName());
							valueMap.put("学号",bed.getOwnerCode());
						}else{
							valueMap.put("教师号",bed.getOwnerCode());
						}
						valueMap.put("姓名",bed.getOwnerName());
						valueMap.put("备注",bed.getRemark());
						recordList.add(valueMap);
					}
				}
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
		// 业务处理模块　具体的业务在这里处理
		//自己的处理接口　数据范围格式如下
		//业务类中返回给我的一个json格式的数据
		JSONObject json=JSONObject.parseObject(params);
		String acadyear=json.getString("acadyear");
		String semester=json.getString("semester");
		String roomProperty=json.getString("roomProperty");
		String jsonMsg =dyDormBedService.doBedImport(getLoginInfo().getUnitId(), acadyear,semester,roomProperty, datas);
		logger.info("导入结束......");
		return jsonMsg;
	}

}
