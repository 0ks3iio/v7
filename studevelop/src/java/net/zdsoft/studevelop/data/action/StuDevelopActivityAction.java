package net.zdsoft.studevelop.data.action;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.SemesterRemoteService;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.studevelop.data.constant.StuDevelopConstant;
import net.zdsoft.studevelop.data.entity.StudevelopActivity;
import net.zdsoft.studevelop.data.entity.StudevelopAttachment;
import net.zdsoft.studevelop.data.service.StudevelopActivityService;
import net.zdsoft.studevelop.data.service.StudevelopAttachmentService;
import net.zdsoft.studevelop.mobile.service.StuIntroductionService;

/**
 * 学生成长活动action
 * 
 * @author weixh
 * @since 2017-7-19 上午10:11:04
 */
@Controller
@RequestMapping("/studevelop/activity/{actType}")
public class StuDevelopActivityAction extends CommonAuthAction {
	@Autowired
	private StudevelopActivityService studevelopActivityService;
	@Autowired
	private SemesterRemoteService semesterRemoteService;
	@Autowired
	private ClassRemoteService classRemoteService;
	@Autowired
	private StudevelopAttachmentService studevelopAttachmentService;
	@Autowired
	private StuIntroductionService stuIntroductionService;
	
	private String acadyear = "";
	private int semester = 0;
	private String rangeId = "";
	private String rangeType = "";
	
	@RequestMapping("/index/page")
	@ControllerInfo(value = "活动index")
	public String showIndex(@PathVariable String actType, HttpServletRequest request, ModelMap map){
		String unitId = getLoginInfo().getUnitId();
		if(StuDevelopConstant.ACTIVITY_TYPE_SCHOOL_INFO.equals(actType)){
			if(!isAdmin(StuDevelopConstant.PERMISSION_TYPE_GROWTH)){
				return promptFlt(map , "不是成长手册管理员不能维护和查询我的校园内容");
			}
			rangeId = unitId;
			rangeType = StuDevelopConstant.RANGETYPE_SCH;
			return schoolInfo(actType, map, unitId);
		}
		List<String> acadyears = SUtils.dt(semesterRemoteService.findAcadeyearList(), new TR<List<String>>(){});
		if(CollectionUtils.isEmpty(acadyears)){
			return errorFtl(map, "还没有维护学年学期信息！");
		}
		acadyear = request.getParameter("acadyear");
		semester = NumberUtils.toInt(request.getParameter("semester"));
		rangeId = request.getParameter("rangeId");
		if (StringUtils.isEmpty(acadyear)) {
			Semester cs = SUtils.dc(
					semesterRemoteService.getCurrentSemester(1,unitId),
					Semester.class);
			if (cs != null) {
				acadyear = cs.getAcadyear();
				semester = cs.getSemester();
			} else {
				acadyear = acadyears.get(0);
				semester = 1;
			}
		}
		map.put("acadyearList", acadyears);
		map.put("acadyear", acadyear);
		map.put("semester", semester);
		if(StuDevelopConstant.ACTIVITY_TYPE_CLASS_ACTIVITY.equals(actType)
				|| StuDevelopConstant.ACTIVITY_TYPE_THEME_ACTIVITY.equals(actType)){
			String title = "";
			if(StuDevelopConstant.ACTIVITY_TYPE_CLASS_ACTIVITY.equals(actType)){
				title = "班级活动";
			}else{
				title = "主题活动";
			}
			rangeType = StuDevelopConstant.RANGETYPE_CLASS;
			List<Clazz> classList = null;
			if(isAdmin(StuDevelopConstant.PERMISSION_TYPE_GROWTH)){
				classList = SUtils.dt(classRemoteService.findByIdCurAcadyear(unitId, acadyear), new TR<List<Clazz>>(){});
			}else {
				classList = headTeacherClass(acadyear);
			}
			if(CollectionUtils.isEmpty(classList)){
				return promptFlt(map , "不是（副）班主任和成长手册管理员不能维护和查询"+ title + "内容");
			}

			map.put("clsList",classList);
		} else {
			if(!isAdmin(StuDevelopConstant.PERMISSION_TYPE_GROWTH)){
				return promptFlt(map , "不是成长手册管理员不能维护和查询学校活动内容");
			}
			rangeId = unitId;
			rangeType = StuDevelopConstant.RANGETYPE_SCH;
		}
		map.put("rangeId", rangeId);
		map.put("rangeType", rangeType);
		map.put("actType", actType);
		return "/studevelop/activity/activityIndex.ftl";
	}
	
	// 我的校园
	private String schoolInfo(String actType, ModelMap map, String unitId){
		acadyear = StuDevelopConstant.DEFAULT_ACADYEAR;
		semester = NumberUtils.toInt(StuDevelopConstant.DEFAULT_SEMESTER);
		List<StudevelopActivity> acts = studevelopActivityService.findActBySemeRangeId(acadyear, semester, actType, rangeId, rangeType);
		StudevelopActivity act;
		if(CollectionUtils.isNotEmpty(acts)){
			act = acts.get(0);
			act.setNewEn(false);
			List<StudevelopAttachment> atts = studevelopAttachmentService.findListByObjIds(act.getId());
			map.put("picList", atts);
		} else {
			act = getNewAct(actType, unitId);
			act.setId(UuidUtils.generateUuid());
		}
		map.put("actType", actType);
		map.put("act", act);
		return "/studevelop/myschool/mySchool.ftl";
	}
	
	private StudevelopActivity getNewAct(String actType, String unitId){
		StudevelopActivity act = new StudevelopActivity();
		act.setAcadyear(acadyear);
		act.setSemester(semester);
		act.setUnitId(unitId);
		act.setRangeId(rangeId);
		act.setRangeType(rangeType);
		act.setActType(actType);
		act.setNewEn(true);
		return act;
	}
	
	@RequestMapping("/list/page")
	@ControllerInfo(value = "活动列表")
	public String showList(@PathVariable String actType, String acadyear, int semester, 
			String rangeId, String rangeType, ModelMap map){
		List<StudevelopActivity> acts = studevelopActivityService.findActBySemeRangeId(acadyear, semester, 
				actType, rangeId, rangeType);
		map.put("actList", acts);
		map.put("actType", actType);
		return "/studevelop/activity/activityList.ftl";
	}
	
	@RequestMapping("/edit")
	@ControllerInfo(value = "活动编辑")
	public String showEdit(@PathVariable String actType, String id, String acadyear, int semester, 
			String rangeId, String rangeType, ModelMap map){
		StudevelopActivity act = null;
		if (StringUtils.isNotEmpty(id)) {
			act = studevelopActivityService.findOne(id);
		}
		if(act == null){
			this.acadyear = acadyear;
			this.semester = semester;
			this.rangeId = rangeId;
			this.rangeType = rangeType;
			act = getNewAct(actType, getLoginInfo().getUnitId());
		}
		map.put("act", act);
		map.put("actType", actType);
		return "/studevelop/activity/activityEdit.ftl";
	}
	
	@ResponseBody
	@RequestMapping("/saveAct")
	@ControllerInfo(value = "保存活动")
	public String saveAct(@PathVariable String actType, StudevelopActivity act, ModelMap map){
		try {
			act.setModifyTime(new Date());
			if(StringUtils.isEmpty(act.getId())){
				act.setId(UuidUtils.generateUuid());
			}
			if(act.isNewEn()){
				act.setCreationTime(act.getModifyTime());
			}
			studevelopActivityService.save(act);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return error("保存失败！");
		}
		return success("保存成功！");
	}
	
	@ResponseBody
	@RequestMapping("/saveSchoolInfo")
	@ControllerInfo(value = "保存我的校园")
	public String saveSchoolInfo(@PathVariable String actType, StudevelopActivity act, ModelMap map){
		try {
			act.setModifyTime(new Date());
			if(StringUtils.isEmpty(act.getId())){
				act.setId(UuidUtils.generateUuid());
			}
			if(act.isNewEn()){
				act.setCreationTime(act.getModifyTime());
			}
			studevelopActivityService.saveInfo(act);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return error("保存失败！");
		}
		return success("保存成功！");
	}
	
	@ResponseBody
	@RequestMapping("/delAct")
	@ControllerInfo(value = "保存活动")
	public String delAct(@PathVariable String actType, String ids, ModelMap map){
		try {
			studevelopActivityService.delete(StringUtils.split(ids, ","));
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return error("删除失败！");
		}
		return success("删除成功！");
	}
	
	@RequestMapping("/detail")
	@ControllerInfo(value = "活动详情")
	public String showDetails(@PathVariable String actType, String id, ModelMap map){
		StudevelopActivity act = studevelopActivityService.findOne(id);
		if(act == null){
			map.put("msg", "活动不存在或已被删除！");
		} else {
			map.put("act", act);
		}
		map.put("actType", actType);
		return "/studevelop/activity/activityDetail.ftl";
	}
	
	@RequestMapping("/detailList")
	@ControllerInfo(value = "活动详情")
	public String showDetail(@PathVariable String actType, String id, ModelMap map){
		List<StudevelopAttachment> atts = studevelopAttachmentService.getAttachmentByObjId(id, actType);
		map.put("maxSize", StuDevelopConstant.MAX_SIZE_ACTIVITY);
		map.put("actDetails", atts);
		map.put("actType", actType);
		map.put("id", id);
		map.put("fileUrl",stuIntroductionService.getFileURL());
		return "/studevelop/activity/activityDetailList.ftl";
	}
}
