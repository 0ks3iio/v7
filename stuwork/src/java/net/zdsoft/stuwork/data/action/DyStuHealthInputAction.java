package net.zdsoft.stuwork.data.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.GradeRemoteService;
import net.zdsoft.basedata.remote.service.SemesterRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.stuwork.data.dto.DyStuHealthInputDto;
import net.zdsoft.stuwork.data.entity.DyStuHealthProjectItem;
import net.zdsoft.stuwork.data.entity.DyStuHealthResult;
import net.zdsoft.stuwork.data.service.DyStuHealthProjectItemService;
import net.zdsoft.stuwork.data.service.DyStuHealthResultService;
import net.zdsoft.system.entity.mcode.McodeDetail;
import net.zdsoft.system.remote.service.McodeRemoteService;

@Controller
@RequestMapping("/stuwork")
public class DyStuHealthInputAction extends BaseAction {

	@Autowired
	private SemesterRemoteService semesterRemoteService;
	@Autowired
	private DyStuHealthProjectItemService dyStuHealthProjectItemService;
	@Autowired
	private McodeRemoteService mcodeRemoteService;
	@Autowired
	private ClassRemoteService classRemoteService;
	@Autowired
	private GradeRemoteService gradeRemoteService;
	@Autowired
	private StudentRemoteService studentRemoteService;
	@Autowired
	private DyStuHealthResultService dyStuHealthResultService;
	
	@RequestMapping("/health/input/index")
	@ControllerInfo("页面的第一次跳转")
	public String showIndex(){
		return "/stuwork/health/input/showIndex.ftl";
	}
	
	@RequestMapping("/health/inputIndex")
	@ControllerInfo("展示学生体检录入")
	public String inputIndex(ModelMap map){
		//当前的学年学期
    	Semester semester = SUtils.dc(semesterRemoteService.getCurrentSemester(0, getLoginInfo().getUnitId()), Semester.class);
        List<String> acadyearList = SUtils.dt(semesterRemoteService.findAcadeyearList(), new TR<List<String>>(){});
        map.put("semester", semester); 
		map.put("acadyearList", acadyearList);
		return "/stuwork/health/input/inputIndex.ftl";
	}
	
	@RequestMapping("/health/input/list")
	public String allTestResultList(String studentId,String acadyear,String semester,ModelMap map){
		DyStuHealthInputDto dshtDto = getHealthDto(studentId, acadyear, semester);
		
		map.put("dshtDto", dshtDto);
		return "/stuwork/health/input/inputResultList.ftl";
	}

	/**
	 * @param studentId
	 * @param acadyear
	 * @param semester
	 * @return
	 */
	protected DyStuHealthInputDto getHealthDto(String studentId, final String acadyear, final String semester) {
		DyStuHealthInputDto dshtDto = new DyStuHealthInputDto();
		List<McodeDetail> sexType = SUtils.dt(mcodeRemoteService.findByMcodeIds("DM-XB"), McodeDetail.class);
		Map<String, String> sexNameMap = EntityUtils.getMap(sexType, "thisId", "mcodeContent");
		
		List<McodeDetail> semesterType = SUtils.dt(mcodeRemoteService.findByMcodeIds("DM-XQ"), McodeDetail.class);
        Map<String, String> semesterMap = EntityUtils.getMap(semesterType, "thisId", "mcodeContent");
		
        //得到体检项的map
        List<DyStuHealthResult> listDSHR = dyStuHealthResultService.findByUnitIdAndStuId(getLoginInfo().getUnitId(),studentId);
		//过滤掉不是当前学年学期的
		listDSHR = EntityUtils.filter(listDSHR,new EntityUtils.Filter<DyStuHealthResult>(){
			@Override
			public boolean doFilter(DyStuHealthResult healthResult) {
				return ! (healthResult.getAcadyear().equals(acadyear) && healthResult.getSemester().equals(semester)) ;
			}
		});
		Map<String, String> dshrMap = EntityUtils.getMap(listDSHR, "itemId", "itemResult");
        
		if(StringUtils.isNotBlank(studentId)){
			List<DyStuHealthProjectItem> listDSHPI = getAcadyarProjectItem(acadyear, semester);
			if(CollectionUtils.isNotEmpty(listDSHPI)){
//				dshtDto.setStudentId(studentId);
				dshtDto.setListDSHPI(listDSHPI);
			}
			if(! dshrMap.isEmpty()) {
				dshtDto.setItemIdMap(dshrMap);
			}
			Student student =  SUtils.dc(studentRemoteService.findOneById(studentId), Student.class);
			Clazz clazz = SUtils.dc(classRemoteService.findOneById(student.getClassId()), Clazz.class);
			Grade grade = SUtils.dc(gradeRemoteService.findOneById(clazz.getGradeId()), Grade.class);
			dshtDto.setStudentName(student.getStudentName());
			dshtDto.setClassName(grade.getGradeName()+clazz.getClassName());
			dshtDto.setSex(sexNameMap.get(String.valueOf(student.getSex())));
			dshtDto.setSchoolYearTeam(acadyear+semesterMap.get(semester));
		}
		return dshtDto;
	}

	@ResponseBody
	@RequestMapping("/health/input/add")
	@ControllerInfo("添加学生的体检结果")
	public String addDyStuHealthResult(String studentId, final String acadyear, final String semester,@RequestBody String inputResult){
		try {
			List<DyStuHealthResult> listDSHR = dyStuHealthResultService.findByUnitIdAndStuId(getLoginInfo().getUnitId(),studentId);
			//过滤掉不是当前学年学期的
			listDSHR = EntityUtils.filter(listDSHR,new EntityUtils.Filter<DyStuHealthResult>(){
				@Override
				public boolean doFilter(DyStuHealthResult healthResult) {
					return ! (healthResult.getAcadyear().equals(acadyear) && healthResult.getSemester().equals(semester)) ;
				}
			});
			Map<String, DyStuHealthResult> dshrMap = EntityUtils.getMap(listDSHR, "itemId");
			//得到学生的性别和班级
			Student student =  SUtils.dc(studentRemoteService.findOneById(studentId), Student.class);
			Clazz clazz = SUtils.dc(classRemoteService.findOneById(student.getClassId()), Clazz.class);
			Grade grade = SUtils.dc(gradeRemoteService.findOneById(clazz.getGradeId()), Grade.class);
			List<DyStuHealthResult> listDSHR1 = new ArrayList<DyStuHealthResult>();
			JSONObject jsonObject = SUtils.dc(inputResult, JSONObject.class);
			List<DyStuHealthProjectItem> listDSHPI = getAcadyarProjectItem(acadyear, semester);
			if(CollectionUtils.isNotEmpty(listDSHPI)) {
				for (DyStuHealthProjectItem dyStuHealthProjectItem : listDSHPI) {
					String re = jsonObject.getString(dyStuHealthProjectItem.getItemName());
					if(CollectionUtils.isNotEmpty(listDSHR) && dshrMap.get(dyStuHealthProjectItem.getItemId()) != null) {
						DyStuHealthResult dyStuHealthResult = dshrMap.get(dyStuHealthProjectItem.getItemId());
						dyStuHealthResult.setItemResult(re);
						listDSHR1.add(dyStuHealthResult);
					}else {
						DyStuHealthResult dyStuHealthResult = new DyStuHealthResult();
						dyStuHealthResult.setId(UuidUtils.generateUuid());
						dyStuHealthResult.setAcadyear(acadyear);
						dyStuHealthResult.setSemester(semester);
						dyStuHealthResult.setUnitId(getLoginInfo().getUnitId());
						dyStuHealthResult.setStudentId(studentId);
						dyStuHealthResult.setItemId(dyStuHealthProjectItem.getItemId());
						dyStuHealthResult.setItemName(dyStuHealthProjectItem.getItemName());
						dyStuHealthResult.setItemUnit(dyStuHealthProjectItem.getItemUnit());
						dyStuHealthResult.setItemResult(re);
						dyStuHealthResult.setSex(String.valueOf(student.getSex()));
						dyStuHealthResult.setClassName(grade.getGradeName()+clazz.getClassName());
						dyStuHealthResult.setOrderId(dyStuHealthProjectItem.getOrderId());
						listDSHR1.add(dyStuHealthResult);
					}
				}
			}
			dyStuHealthResultService.saveAll(EntityUtils.toArray(listDSHR1,DyStuHealthResult.class));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return error("保存失败");
		}
		return success("保存成功");
	} 
	/**
	 * @param acadyear
	 * @param semester
	 * @return
	 */
	private List<DyStuHealthProjectItem> getAcadyarProjectItem(String acadyear, String semester) {
		List<DyStuHealthProjectItem> listDSHPI = dyStuHealthProjectItemService.findByUIdAndSemester(getLoginInfo().getUnitId(),acadyear,semester);
		//过滤掉不是当前unitId
		listDSHPI = EntityUtils.filter(listDSHPI,new EntityUtils.Filter<DyStuHealthProjectItem>(){
			@Override
			public boolean doFilter(DyStuHealthProjectItem projectItem) {
				return !projectItem.getUnitId().equals(getLoginInfo().getUnitId())  ;
			}
		});
		return listDSHPI;
	}
}
