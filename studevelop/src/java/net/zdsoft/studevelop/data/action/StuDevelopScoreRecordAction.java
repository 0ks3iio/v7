package net.zdsoft.studevelop.data.action;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.SemesterRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.studevelop.data.dto.StuDevelopScoreRecordDto;
import net.zdsoft.studevelop.data.entity.StuDevelopCateGory;
import net.zdsoft.studevelop.data.entity.StuDevelopProject;
import net.zdsoft.studevelop.data.entity.StuDevelopScoreRecord;
import net.zdsoft.studevelop.data.entity.StuDevelopSubject;
import net.zdsoft.studevelop.data.service.StuDevelopCateGoryService;
import net.zdsoft.studevelop.data.service.StuDevelopProjectService;
import net.zdsoft.studevelop.data.service.StuDevelopScoreRecordService;
import net.zdsoft.studevelop.data.service.StuDevelopSubjectService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
@Controller
@RequestMapping("/studevelop")
public class StuDevelopScoreRecordAction extends BaseAction{
    @Autowired
	private SemesterRemoteService semesterRemoteService;
    @Autowired
    private ClassRemoteService classRemoteService;
    @Autowired
    private StudentRemoteService studentRemoteService;
    @Autowired
    private StuDevelopSubjectService stuDevelopSubjectService;
    @Autowired
    private StuDevelopCateGoryService stuDevelopCateGoryService;
    @Autowired
    private StuDevelopProjectService stuDevelopProjectService;
    @Autowired
    private StuDevelopScoreRecordService stuDevelopScoreRecordService;
	@RequestMapping("/scoreRecord/index/page")
	@ControllerInfo(value = "")
	public String scoreRecordHead(ModelMap map){		
		List<String> acadyearList = SUtils.dt(semesterRemoteService.findAcadeyearList(), new TR<List<String>>() {});
		if(CollectionUtils.isEmpty(acadyearList)){
			return errorFtl(map,"学年学期不存在");
		}
        map.put("acadyearList", acadyearList);
		Semester semesterObj = SUtils.dc(semesterRemoteService.getCurrentSemester(1,getLoginInfo().getUnitId()), Semester.class);
		if(semesterObj!=null){
			String acadyear=semesterObj.getAcadyear();
			String semester=semesterObj.getSemester()+"";
			map.put("acadyear", acadyear);
		    map.put("semester", semester);
		}else{
			map.put("acadyear", "");
		    map.put("semester", "");
		}
		return "/studevelop/scoreRecord/scoreRecordHead.ftl";
	}

	@RequestMapping("/scoreRecord/list")
	@ControllerInfo(value = "")
	public String scoreList(String acadyear, String semester, String studentId, ModelMap map){		
		String classId = SUtils.dc(studentRemoteService.findOneById(studentId), Student.class).getClassId();
		String studentName = SUtils.dc(studentRemoteService.findOneById(studentId), Student.class).getStudentName();
		String gradeId = SUtils.dc(classRemoteService.findOneById(classId), Clazz.class).getGradeId();
		List<StuDevelopSubject> subjectList = stuDevelopSubjectService.stuDevelopSubjectList(getLoginInfo().getUnitId(), acadyear, semester, gradeId);
		Set<String> subIdSet = new HashSet<String>();
		for(StuDevelopSubject sub : subjectList){
			subIdSet.add(sub.getId());
		}
		List<StuDevelopCateGory> stuDevelopCateGoryList;
		Set<String> valiSubIdSet = new HashSet<String>();
		if(CollectionUtils.isNotEmpty(subIdSet)){
			stuDevelopCateGoryList = stuDevelopCateGoryService.findListBySubjectIdIn(subIdSet.toArray(new String[0]));
		    for(StuDevelopCateGory gory : stuDevelopCateGoryList){
		    	valiSubIdSet.add(gory.getSubjectId());
		    } 
		}else{
			stuDevelopCateGoryList = new ArrayList<StuDevelopCateGory>();
		}
		for(StuDevelopSubject sub : subjectList){
			if(!valiSubIdSet.contains(sub.getId())){
				sub.setState("0");
			}
		}
		List<StuDevelopProject> stuDevelopProjectListTemp = stuDevelopProjectService.stuDevelopProjectList(getLoginInfo().getUnitId(), acadyear, semester, gradeId);
		List<StuDevelopProject> stuDevelopProjectList = new ArrayList<StuDevelopProject>();
		List<StuDevelopProject> stuDevelopProjectList2 = new ArrayList<StuDevelopProject>();
		for(StuDevelopProject item : stuDevelopProjectListTemp){
			if("1".equals(item.getState())){
				stuDevelopProjectList.add(item);
			}else{
				stuDevelopProjectList2.add(item);
			}
		}
		for(StuDevelopProject item : stuDevelopProjectList2){
			stuDevelopProjectList.add(item);
		}
		List<StuDevelopScoreRecord> stuDevelopScoreRecordList = stuDevelopScoreRecordService.stuDevelopScoreRecordList(acadyear, semester, studentId);
		map.put("stuDevelopScoreRecordList", stuDevelopScoreRecordList);
		map.put("studentName", studentName);
		map.put("subjectList", subjectList);
		map.put("stuDevelopCateGoryList", stuDevelopCateGoryList);
		map.put("stuDevelopProjectList", stuDevelopProjectList);
		map.put("acadyear", acadyear);
		map.put("semester", semester);
		map.put("studentId", studentId);
		return "/studevelop/scoreRecord/scoreRecordList.ftl";
	}
	
	@ResponseBody
	@RequestMapping("/scoreRecord/save")
    @ControllerInfo(value = "保存")
	public String scoreRecordSave(String acadyear, String semester, String studentId, StuDevelopScoreRecordDto stuDevelopScoreRecordDto){
		try {
			List<StuDevelopScoreRecord> stuDevelopScoreRecordListTemp = stuDevelopScoreRecordDto.getStuDevelopScoreRecordList();
			List<StuDevelopScoreRecord> stuDevelopScoreRecordList = new ArrayList<StuDevelopScoreRecord>();
			if(CollectionUtils.isNotEmpty(stuDevelopScoreRecordListTemp)){
            	for(StuDevelopScoreRecord item : stuDevelopScoreRecordListTemp){
            		if(null!=item && StringUtils.isNotBlank(item.getSubjectId())){
            			item.setAcadyear(acadyear);
            			item.setSemester(semester);
            			item.setStudentId(studentId);
            			if(StringUtils.isBlank(item.getId())){
            				item.setId(UuidUtils.generateUuid());
            			}
            			stuDevelopScoreRecordList.add(item);
            		}
            	}  
            	stuDevelopScoreRecordService.saveScore(stuDevelopScoreRecordList);
            }
		} catch (Exception e) {
			e.printStackTrace();
			return returnError();
		}
		return returnSuccess();
	}
}
