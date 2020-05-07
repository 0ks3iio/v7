package net.zdsoft.studevelop.data.action;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.remote.service.GradeRemoteService;
import net.zdsoft.basedata.remote.service.SemesterRemoteService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.StringUtils;
import net.zdsoft.studevelop.data.constant.StuDevelopConstant;
import net.zdsoft.studevelop.data.entity.*;
import net.zdsoft.studevelop.data.service.*;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/studevelop") 
public class StuDevelopProjectAction extends BaseAction{
    @Autowired
	private SemesterRemoteService semesterRemoteService;
    @Autowired
    private StuDevelopProjectService stuDevelopProjectService;
    @Autowired
    private GradeRemoteService gradeRemoteService;
    @Autowired
    private StuDevelopScoreRecordService stuDevelopScoreRecordService;
	@Autowired
    private StudevelopTemplateService studevelopTemplateService;
	@Autowired
	private StudevelopTemplateOptionsService studevelopTemplateOptionsService;
	@Autowired
	private StudevelopTemplateItemService studevelopTemplateItemService;
	@RequestMapping("/project/head")
	@ControllerInfo(value = "")
	public String projectHead(ModelMap map){
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
		return "/studevelop/project/projectHead.ftl";
	}
	
	@RequestMapping("/project/list")
	@ControllerInfo(value = "")
	public String projectList(String acadyear, String semester, String gradeId,String section,String code, ModelMap map){
		String unitId = getLoginInfo().getUnitId();
		List<StudevelopTemplate> templates = studevelopTemplateService.getTemplateByCode(acadyear,semester,gradeId,section,code,unitId);
		StudevelopTemplate tem = null;
		if(CollectionUtils.isNotEmpty(templates)){
			tem = templates.get(0);
		}
		if( tem != null){
			List<StudevelopTemplateItem> templateItems = studevelopTemplateItemService.getTemplateItemListByObjectType(tem.getId(),null, StuDevelopConstant.HEALTH_IS_NOT_CLOSED);
			//获取option 单选内容
				
			if(CollectionUtils.isNotEmpty(templateItems)){
				List<StudevelopTemplateOptions> optionList=studevelopTemplateOptionsService.getOptionsListByTemplateItemId
											(EntityUtils.getSet(templateItems, StudevelopTemplateItem::getId).toArray(new String[]{}));
				if(CollectionUtils.isNotEmpty(optionList)){
					//置入值
					Map<String, List<StudevelopTemplateOptions>> optionMap=optionList.stream().collect(Collectors.groupingBy(StudevelopTemplateOptions::getTemplateItemId));
					for (StudevelopTemplateItem item : templateItems) {
							if(optionMap.containsKey(item.getId())){
								List<StudevelopTemplateOptions> inOptionList=optionMap.get(item.getId());
								StringBuilder sb= new StringBuilder();
								int i=0;
								for(StudevelopTemplateOptions option:inOptionList){
									if(i!=0){
										sb.append("、");
									}
									i++;
									sb.append(option.getOptionName()+" ");
								}
								item.setOptionNames(sb.toString());
							}
					}
				}
			}
			map.put("itemList", templateItems);
		}

    	return "/studevelop/project/projectItemList.ftl";
	}
	
	@RequestMapping("/project/edit")
	@ControllerInfo(value = "")
	public String projectedit(String id, String acadyear, String semester, String gradeId,String code ,String objectType, ModelMap map ){
		StudevelopTemplateItem studevelopTemplateItem;
		if(StringUtils.isNotEmpty(id)){
			studevelopTemplateItem = studevelopTemplateItemService.findOne(id);
			List<StudevelopTemplateOptions> list =  studevelopTemplateOptionsService.findListBy("templateItemId" ,studevelopTemplateItem.getId());
			map.put("optionsList" ,list);
		}else{
			studevelopTemplateItem =  new StudevelopTemplateItem();
		}

		map.put("studevelopTemplateItem" ,studevelopTemplateItem);
    	map.put("acadyear", acadyear);
    	map.put("semester", semester);
    	map.put("gradeId", gradeId);
    	map.put("code", code);
    	map.put("objectType", objectType);
    	map.put("unitId", getLoginInfo().getUnitId());
		return "/studevelop/project/projectItemEdit.ftl";
	}
	
	@ResponseBody
	@RequestMapping("/project/save")
    @ControllerInfo(value = "保存")
	public String projectSave(StuDevelopProject stuDevelopProject){
		try {
			String acadyear = stuDevelopProject.getAcadyear();
			String semester = stuDevelopProject.getSemester();
			String gradeId = stuDevelopProject.getGradeId();
			List<StuDevelopProject> stuDevelopProjectList = stuDevelopProjectService.stuDevelopProjectList(getLoginInfo().getUnitId(), acadyear, semester, gradeId);
			Set<String> nameSet = new HashSet<String>();
			for(StuDevelopProject pro : stuDevelopProjectList){
				nameSet.add(pro.getProjectName());
			}
			if(StringUtils.isBlank(stuDevelopProject.getId())){
				if(nameSet.contains(stuDevelopProject.getProjectName())){
					return error("该项目名称已存在，请重新输入！");
				}
			}else{
				String yName = "";
				for(StuDevelopProject pro : stuDevelopProjectList){
					if(stuDevelopProject.getId().equals(pro.getId())){
						yName = pro.getProjectName();
					}
				}
				nameSet.remove(yName);
				if(nameSet.contains(stuDevelopProject.getProjectName())){
					return error("该项目名称已存在，请重新输入！");
				}
				StuDevelopProject pro = stuDevelopProjectService.findOne(stuDevelopProject.getId());
				List<StuDevelopScoreRecord> stuDevelopScoreRecordList = stuDevelopScoreRecordService.findByProjectId(acadyear, semester, stuDevelopProject.getId());
			    if(CollectionUtils.isNotEmpty(stuDevelopScoreRecordList) && !pro.getState().equals(stuDevelopProject.getState())){
			    	return error("该项目已有录入成绩，不修改显示对象！");
			    }
			}
			stuDevelopProjectService.saveStuDevelopProject(stuDevelopProject);
		} catch (Exception e) {
			e.printStackTrace();
			return returnError();
		}
		return returnSuccess();
	}
	@ResponseBody
	@RequestMapping("/projectItem/save")
	@ControllerInfo(value = "成绩项目保存")
	public String projectItemSave(StudevelopTemplateItem studevelopTemplateItem ){
		try{

			studevelopTemplateItemService.saveOrUpdateStudevelopTemplateItem(studevelopTemplateItem);
		}catch(Exception e){
			e.printStackTrace();
			return returnError();
		}

		return returnSuccess();
	}
	@ResponseBody
	@RequestMapping("/project/delete")
    @ControllerInfo(value = "删")
	public String projectDelete(String projectIds){
		try {
			String[] projectIdArr = projectIds.split(",");
			stuDevelopProjectService.deleteStuDevelopProject(projectIdArr);
		} catch (Exception e) {
			e.printStackTrace();
			return returnError();
		}
		return returnSuccess();
	}
	
	@RequestMapping("/project/copy")
	@ControllerInfo(value = "")
	public String projectCopy(String acadyear, String semester, String gradeId, ModelMap map){
		List<String> acadyearList = SUtils.dt(semesterRemoteService.findAcadeyearList(), new TR<List<String>>() {});
		if(CollectionUtils.isEmpty(acadyearList)){
			return errorFtl(map,"学年学期不存在");
		}
		map.put("acadyearList", acadyearList);
		map.put("acadyear", acadyear);
		map.put("semester", semester);
		map.put("gradeId", gradeId);
		return "/studevelop/project/projectCopy.ftl";
	}
	
	@ResponseBody
	@RequestMapping("/project/doCopy")
    @ControllerInfo(value = "保存")
	public String doCopy(String acadyear, String semester, String[] gradeIds, String yAcadyear, String ySemester){
		try {
			/*List<StuDevelopProject> stuDevelopProjectList = stuDevelopProjectService.stuDevelopProjectList(getLoginInfo().getUnitId(), acadyear, semester, gradeId);
			if(CollectionUtils.isEmpty(stuDevelopProjectList)){
				return error("该年级在该学年学期下没有项目数据！");
			}*/
			stuDevelopProjectService.copyProject(acadyear, semester, gradeIds, yAcadyear, ySemester, getLoginInfo().getUnitId());
		} catch (Exception e) {
			e.printStackTrace();
			return returnError();
		}
		return returnSuccess();
	}
}
