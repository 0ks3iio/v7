package net.zdsoft.studevelop.data.action;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.remote.service.GradeRemoteService;
import net.zdsoft.basedata.remote.service.SemesterRemoteService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.StringUtils;
import net.zdsoft.studevelop.data.dto.StuDevelopSubjectDto;
import net.zdsoft.studevelop.data.entity.StuDevelopCateGory;
import net.zdsoft.studevelop.data.entity.StuDevelopSubject;
import net.zdsoft.studevelop.data.service.StuDevelopCateGoryService;
import net.zdsoft.studevelop.data.service.StuDevelopSubjectService;

import net.zdsoft.system.remote.service.SystemIniRemoteService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/studevelop") 
public class StuDevelopSubjectAction extends BaseAction{
    @Autowired
	private StuDevelopSubjectService stuDevelopSubjectService;
    @Autowired
	private SemesterRemoteService semesterRemoteService;
    @Autowired
    private StuDevelopCateGoryService stuDevelopCateGoryService;
    @Autowired
    private GradeRemoteService gradeRemoteService;
	@Autowired
	private SystemIniRemoteService systemIniRemoteService;
    @RequestMapping("/subject/tab")
	@ControllerInfo(value = "")
	public String subjectTab(ModelMap map){
		String deployRegion = systemIniRemoteService.findValue(BaseConstants.SYS_OPTION_REGION);
		map.put("deployRegion" ,deployRegion);
    	return "/studevelop/subject/mainTab.ftl";
	}
    
    @RequestMapping("/subject/head")
	@ControllerInfo(value = "")
	public String subjectHead(ModelMap map){
    	List<String> acadyearList = SUtils.dt(semesterRemoteService.findAcadeyearList(), new TR<List<String>>() {});
		if(CollectionUtils.isEmpty(acadyearList)){
			return errorFtl(map,"学年学期不存在");
		}
		List<Grade> gradeList = SUtils.dt(gradeRemoteService.findBySchoolId(getLoginInfo().getUnitId()), new TR<List<Grade>>() {});
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
		map.put("acadyearList", acadyearList);
        map.put("gradeList", gradeList);
		return "/studevelop/subject/subjectHead.ftl";
	}
    
    @RequestMapping("/subject/list")
	@ControllerInfo(value = "")
	public String subjectList(String acadyear, String semester, String gradeId, ModelMap map){
    	List<StuDevelopSubject> stuDevelopSubjectList = stuDevelopSubjectService.stuDevelopSubjectList(getLoginInfo().getUnitId(), acadyear, semester, gradeId);
		map.put("stuDevelopSubjectList", stuDevelopSubjectList);
    	return "/studevelop/subject/subjectList.ftl";
	}
    
    @RequestMapping("/subject/edit")
	@ControllerInfo(value = "")
	public String subjectedit(String id, String acadyear, String semester, String gradeId, ModelMap map){
    	StuDevelopSubject stuDevelopSubject;
    	if(StringUtils.isNotBlank(id)){
    		stuDevelopSubject = stuDevelopSubjectService.findOne(id);
    		List<StuDevelopCateGory> stuDevelopCateGoryList = stuDevelopCateGoryService.findListBySubjectId(id);
    		map.put("stuDevelopCateGoryList", stuDevelopCateGoryList);
    	}else{
    		stuDevelopSubject = new StuDevelopSubject();
    	}
    	map.put("stuDevelopSubject", stuDevelopSubject);
    	map.put("acadyear", acadyear);
    	map.put("semester", semester);
    	map.put("gradeId", gradeId);
    	map.put("unitId", getLoginInfo().getUnitId());
		return "/studevelop/subject/subjectEdit.ftl";
	}
    
	@ResponseBody
	@RequestMapping("/subject/save")
    @ControllerInfo(value = "保存")
	public String subjectSave(StuDevelopSubjectDto stuDevelopSubjectDto){
		try {
			StuDevelopSubject stuDevelopSubject = stuDevelopSubjectDto.getStuDevelopSubject();
			String acadyear = stuDevelopSubject.getAcadyear();
			String semester = stuDevelopSubject.getSemester();
			String gradeId = stuDevelopSubject.getGradeId();
			List<StuDevelopSubject> stuDevelopSubjectList = stuDevelopSubjectService.stuDevelopSubjectList(getLoginInfo().getUnitId(), acadyear, semester, gradeId);
			Set<String> nameSet = new HashSet<String>();
			for(StuDevelopSubject sub : stuDevelopSubjectList){
				nameSet.add(sub.getName());
			}
			if(StringUtils.isBlank(stuDevelopSubject.getId())){
				if(nameSet.contains(stuDevelopSubject.getName())){
					return error("该学科名称已存在，请重新输入！");
				}
			}else{
				String yName = "";
				for(StuDevelopSubject sub : stuDevelopSubjectList){
					if(stuDevelopSubject.getId().equals(sub.getId())){
						yName = sub.getName();
					}
				}
				nameSet.remove(yName);
				if(nameSet.contains(stuDevelopSubject.getName())){
					return error("该学科名称已存在，请重新输入！");
				}
			}
			stuDevelopSubjectService.saveStuDevelopSubject(stuDevelopSubjectDto);
		} catch (Exception e) {
			e.printStackTrace();
			return returnError();
		}
		return returnSuccess();
	}
	
	@ResponseBody
	@RequestMapping("/subject/delete")
    @ControllerInfo(value = "删除")
	public String subjectDelete(String subjectIds){
		try {
			stuDevelopSubjectService.deleteByIds(subjectIds.split(","));
		} catch (Exception e) {
			e.printStackTrace();
			return returnError();
		}
		return returnSuccess();
	}
		
	@RequestMapping("/subject/copy")
	@ControllerInfo(value = "")
	public String subjectCopy(String acadyear, String semester, String gradeId, ModelMap map){
		List<String> acadyearList = SUtils.dt(semesterRemoteService.findAcadeyearList(), new TR<List<String>>() {});
		if(CollectionUtils.isEmpty(acadyearList)){
			return errorFtl(map,"学年学期不存在");
		}
		List<Grade> gradeList = SUtils.dt(gradeRemoteService.findBySchoolId(getLoginInfo().getUnitId()), new TR<List<Grade>>() {});
		map.put("acadyearList", acadyearList);
		map.put("acadyear", acadyear);
		map.put("semester", semester);
		map.put("gradeId", gradeId);
		map.put("gradeList", gradeList);
		return "/studevelop/subject/subjectCopy.ftl";
	}
	
	@ResponseBody
	@RequestMapping("/subject/doCopy")
    @ControllerInfo(value = "保存")
	public String doCopy(){
		try {
			List<Grade> gradeList = SUtils.dt(gradeRemoteService.findBySchoolId(getLoginInfo().getUnitId()), new TR<List<Grade>>() {});
			Set<String> gradeIdSet = new HashSet<String>();
			for(Grade gra : gradeList){
				gradeIdSet.add(gra.getId());
			}
			Semester semesterObj = SUtils.dc(semesterRemoteService.getCurrentSemester(1,getLoginInfo().getUnitId()), Semester.class);
			String acadyear;
			String semester;
			if(semesterObj!=null){
				acadyear=semesterObj.getAcadyear();
				semester=semesterObj.getSemester()+"";
			}else{
				return error("当前学年学期不存在");
			}
			//获取上一学年学期
			String oldAcadyear = "";
			String oldSemester = "";
			if("1".equals(semester)){
				oldSemester = "2";
				String[] arr = acadyear.split("-");
				oldAcadyear = String.valueOf(Integer.parseInt(arr[0])-1)+"-"+String.valueOf(Integer.parseInt(arr[1])-1);
			}else{
				oldSemester = "1";
				oldAcadyear = acadyear;
			}
			stuDevelopSubjectService.doCopy(oldAcadyear, oldSemester, gradeIdSet.toArray(new String[0]), acadyear, semester, getLoginInfo().getUnitId());
		} catch (Exception e) {
			e.printStackTrace();
			return returnError();
		}
		return returnSuccess();
	}
}
