package net.zdsoft.gkelective.data.action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;

import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.basedata.entity.Course;
import net.zdsoft.basedata.remote.service.CourseRemoteService;
import net.zdsoft.basedata.remote.service.TeacherRemoteService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.RedisInterface;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.gkelective.data.constant.GkElectveConstants;
import net.zdsoft.gkelective.data.dto.GkRoundsDto;
import net.zdsoft.gkelective.data.dto.GkSubjectArrangeDto;
import net.zdsoft.gkelective.data.entity.GkLimitSubject;
import net.zdsoft.gkelective.data.entity.GkRelationship;
import net.zdsoft.gkelective.data.entity.GkResult;
import net.zdsoft.gkelective.data.entity.GkRounds;
import net.zdsoft.gkelective.data.entity.GkSubject;
import net.zdsoft.gkelective.data.entity.GkSubjectArrange;
import net.zdsoft.gkelective.data.service.GkLimitSubjectService;
import net.zdsoft.gkelective.data.service.GkRelationshipService;
import net.zdsoft.gkelective.data.service.GkResultService;
import net.zdsoft.gkelective.data.service.GkRoundsService;
import net.zdsoft.gkelective.data.service.GkSubjectArrangeService;
import net.zdsoft.gkelective.data.service.GkSubjectService;
import net.zdsoft.gkelective.data.service.GkTimetableLimitArrangService;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.TypeReference;

@Controller
@RequestMapping("/gkelective/{arrangeId}")
public class GkElectiveGoClassAction extends BaseAction {

    @Autowired
    private CourseRemoteService courseRemoteService;
    @Autowired
    private GkSubjectArrangeService gkSubjectArrangeService;
    @Autowired
    private GkRelationshipService gkRelationshipService;
    @Autowired
	private GkSubjectService gkSubjectService;
    @Autowired
    private TeacherRemoteService teacherRemoteService;
    @Autowired
    private GkResultService gkResultService;
    @Autowired
    private GkTimetableLimitArrangService gkTimetableLimitArrangService;
    @Autowired
    private GkRoundsService gkRoundsService;
    @Autowired
    private GkLimitSubjectService gkLimitSubjectService;
    @RequestMapping("/goClass/index/page")
    @ControllerInfo(value = "走班安排index")
    public String showIndex(@PathVariable String arrangeId, ModelMap map, HttpSession httpSession) {
        map.put("arrangeId", arrangeId);
        return "/gkelective2/goClass/goClassIndex.ftl";
    }
    

    @RequestMapping("/goClass/list/page")
    @ControllerInfo(value = "走班安排list")
    public String showList(@PathVariable final String arrangeId, ModelMap map, HttpSession httpSession) {
        GkSubjectArrange gkent = gkSubjectArrangeService.findArrangeById(arrangeId);
        if (gkent == null) {
            addErrorFtlOperation(map, "返回", "/gkelective/arrange/list/page");
            return errorFtl(map, "未找到对应7选3系统！");
        }
        GkSubjectArrangeDto dto = new GkSubjectArrangeDto();
        if(gkent.getStartTime()==null){
        	gkent.setStartTime(new Date());
        }
        
        dto.setGsaEnt(gkent);
        //列出要总的课程信息
        List<Course> courseList = SUtils.dt(courseRemoteService.findByCodes73(getLoginInfo().getUnitId()),new TR<List<Course>>() {});
        //这次开的课程信息
        List<GkRelationship>  gkrsent =gkRelationshipService.findByTypePrimaryIdIn(GkElectveConstants.RELATIONSHIP_TYPE_03, arrangeId);
        Map<String,List<String>>gkxkMap =EntityUtils.getListMap(gkrsent, "relationshipTargetId", "primaryId");
        List<GkResult> gklist = gkResultService.findByArrangeId(arrangeId);
        if(CollectionUtils.isNotEmpty(gklist)){
        	dto.setStuXuanKe(true);
        }
        map.put("dto", dto);
        map.put("courseList", courseList);
        map.put("gkxkMap", gkxkMap);
        map.put("arrangeId", arrangeId);
        return "/gkelective2/goClass/goClassList.ftl";
    }
    
    @ResponseBody
    @RequestMapping("/goClass/save")
    @ControllerInfo(value = "走班安排save")
    public String saveArrange(@PathVariable String arrangeId, String roundsId,GkRoundsDto dto, ModelMap map) {
        try {
        	//修改页面维护的字段，没有则不修改
        	GkRounds rounds = gkRoundsService.findOne(roundsId);
        	if(rounds!=null){
	        	EntityUtils.copyProperties(dto.getGkRounds(), rounds, true);
	        	if(rounds.getStep()==GkElectveConstants.STEP_0){
	        		rounds.setStep(GkElectveConstants.STEP_1);
	        	}
	        	dto.setGkRounds(rounds);
        	}
        	gkRoundsService.saveDto(dto);
        }
        catch (Exception e) {
            e.printStackTrace();
            return returnError();
        }
        return returnSuccess();
    }
    
    @ResponseBody
    @RequestMapping("/notice/save")
    @ControllerInfo(value = "公告save")
    public String saveArrange(@PathVariable String arrangeId, GkSubjectArrangeDto dto, ModelMap map) {
        try {
        	//修改页面维护的字段，没有则不修改
        	GkSubjectArrange ent =gkSubjectArrangeService.findOne(arrangeId);
        	EntityUtils.copyProperties(dto.getGsaEnt(), ent, true);
        	dto.setGsaEnt(ent);
            gkSubjectArrangeService.saveDto(dto);
        }
        catch (Exception e) {
            e.printStackTrace();
            return returnError();
        }
        return returnSuccess();
    }
    
    
    @RequestMapping("/openClasSub/list/page")
    @ControllerInfo(value = "课程走班设置list")
	public String openClassSubList(@PathVariable final String arrangeId,String roundsId, ModelMap map){
    	GkSubjectArrange  gkent =gkSubjectArrangeService.findArrangeById(arrangeId);
    	List<GkSubject> subs = gkSubjectService.findByRoundsId(roundsId,null);
		if(CollectionUtils.isEmpty(subs)){
			List<Course> courses = RedisUtils.getObject(GkElectveConstants.GK_OPENSUBJECT_KEY+arrangeId, RedisUtils.TIME_ONE_MINUTE, new TypeReference<List<Course>>(){}, new RedisInterface<List<Course>>(){
				@Override
				public List<Course> queryData() {
					List<GkRelationship> findByTypePrimaryIdIn = gkRelationshipService.findByTypePrimaryIdIn(GkElectveConstants.RELATIONSHIP_TYPE_03,arrangeId);
					Set<String> subjectIds = EntityUtils.getSet(findByTypePrimaryIdIn, "relationshipTargetId");
					return SUtils.dt(courseRemoteService.findListByIds(subjectIds.toArray(new String[0])),new TR<List<Course>>() {});
				}
			});
			subs=new ArrayList<GkSubject>();
			if(CollectionUtils.isNotEmpty(courses)){
				for(Course c:courses){
					GkSubject ent= new GkSubject();
					ent.setSubjectId(c.getId());
					ent.setSubjectName(c.getSubjectName());
					ent.setTeachModel(GkSubject.TEACH_TYPE1); //默认全部走班
					ent.setRoundsId(roundsId);
					subs.add(ent);
				}
			}
			map.put("isAddorUp", "0");
		}else{
			Set<String> subjectIds = EntityUtils.getSet(subs, "subjectId");
			List<Course> courses =SUtils.dt(courseRemoteService.findListByIds(subjectIds.toArray(new String[0])),new TR<List<Course>>() {});
			Map<String,List<String>> cmap= EntityUtils.getListMap(courses, "id", "subjectName");      
	        for(GkSubject ent:subs){
				List<String> list =cmap.get(ent.getSubjectId());
				if(CollectionUtils.isNotEmpty(list)){
					ent.setSubjectName(list.get(0));
				}
			}
	        map.put("isAddorUp", "1");
		}
		//后面一步操作了前面就不能调整
		GkRounds rent =gkRoundsService.findRoundById(roundsId);
		GkRoundsDto dto =new GkRoundsDto();
		dto.setGsaEnt(gkent);
		dto.setGksubList(subs);
		dto.setGkRounds(rent);
		
		map.put("dto", dto);
		map.put("arrangeId", arrangeId);
		map.put("roundsId", roundsId);
		return "/gkelective2/arrangeRounds/openClassSubListNew.ftl";
	}
    
    @RequestMapping("/selectSet/List/page")
    @ControllerInfo(value = "限选设置List")
    public String selectSetList(@PathVariable final String arrangeId, ModelMap map, HttpSession httpSession) {
    	GkSubjectArrange gkSubArr = gkSubjectArrangeService.findArrangeById(arrangeId);
		if(gkSubArr==null){
			return errorFtl(map,"对应选课项目已经不存在");
		}
		Map<String,String> courseMap = new HashMap<String,String>();
		List<Course> coursesList = RedisUtils.getObject(GkElectveConstants.GK_OPENSUBJECT_KEY+arrangeId, RedisUtils.TIME_ONE_MINUTE, new TypeReference<List<Course>>(){}, new RedisInterface<List<Course>>(){
			@Override
			public List<Course> queryData() {
				List<GkRelationship> findByTypePrimaryIdIn = gkRelationshipService.findByTypePrimaryIdIn(GkElectveConstants.RELATIONSHIP_TYPE_03,arrangeId);
				Set<String> subjectIds = EntityUtils.getSet(findByTypePrimaryIdIn, "relationshipTargetId");
				return SUtils.dt(courseRemoteService.findListByIds(subjectIds.toArray(new String[0])),new TR<List<Course>>() {});
			}
		});
		for (Course course : coursesList) {
			courseMap.put(course.getId(), course.getSubjectName());
		}
		List<GkLimitSubject> GkLimitSubjectList = gkLimitSubjectService.findBySubjectArrangeId(arrangeId);
		String[] subjectIds;
		for (GkLimitSubject gkLimitSubject : GkLimitSubjectList) {
			subjectIds = gkLimitSubject.getSubjectVal().split(",");
			String subjectName = "";
			for (String str : subjectIds) {
				subjectName += courseMap.get(str) + "、";
			}
			subjectName = subjectName.substring(0, subjectName.length()-1);
			gkLimitSubject.setSubjectNames(subjectName);
		}
		map.put("gkSubArr", gkSubArr);
		map.put("arrangeId", arrangeId);
		map.put("GkLimitSubjectList", GkLimitSubjectList);
    	return "/gkelective2/goClass/selectSetList.ftl";
    }
    
    @ResponseBody
	@RequestMapping("/selectSet/delete")
    @ControllerInfo(value = "删除限选")
    public String selectSetDelete(@PathVariable String arrangeId, String id, ModelMap map, HttpSession httpSession) {
    	try {
    		gkLimitSubjectService.delete(id);
		} catch (Exception e) {
			e.printStackTrace();
			return returnError("删除失败！", e.getMessage());
		}
    	return success("删除成功！");
    }
    
    @RequestMapping("/selectSet/edit")
    @ControllerInfo(value = "新增限选")
    public String selectSetAdd(@PathVariable final String arrangeId, ModelMap map, HttpSession httpSession) {
    	List<Course> coursesList = RedisUtils.getObject(GkElectveConstants.GK_OPENSUBJECT_KEY+arrangeId, RedisUtils.TIME_ONE_MINUTE, new TypeReference<List<Course>>(){}, new RedisInterface<List<Course>>(){
			@Override
			public List<Course> queryData() {
				List<GkRelationship> findByTypePrimaryIdIn = gkRelationshipService.findByTypePrimaryIdIn(GkElectveConstants.RELATIONSHIP_TYPE_03,arrangeId);
				Set<String> subjectIds = EntityUtils.getSet(findByTypePrimaryIdIn, "relationshipTargetId");
				return SUtils.dt(courseRemoteService.findListByIds(subjectIds.toArray(new String[0])),new TR<List<Course>>() {});
			}
		});
    	map.put("coursesList", coursesList);
    	map.put("arrangeId", arrangeId);
    	return "/gkelective2/goClass/selectSetEdit.ftl";
    }
    
    @ResponseBody
	@RequestMapping("/selectSet/save")
    @ControllerInfo(value = "保存限选")
    public String selectSetSave(@PathVariable String arrangeId, String subjectId, ModelMap map, HttpSession httpSession) {
    	List<GkLimitSubject> GkLimitSubjectList = gkLimitSubjectService.findBySubjectArrangeId(arrangeId);
    	List<String> subjectIds =  Arrays.asList(subjectId.split(","));
    	List<String> subjectIds2;
    	for (GkLimitSubject gkLimitSubject : GkLimitSubjectList) {
    		subjectIds2 = Arrays.asList(gkLimitSubject.getSubjectVal().split(","));
    		if (subjectIds2.containsAll(subjectIds)) {
    			return returnError("保存失败！", "该限选种类已存在！");
    		}
    	}
    	try {
    		GkLimitSubject t = new GkLimitSubject();
    		t.setSubjectArrangeId(arrangeId);
    		t.setSubjectVal(subjectId);
    		gkLimitSubjectService.saveLimitSubject(t);
		} catch (Exception e) {
			e.printStackTrace();
			return returnError("保存失败！", e.getMessage());
		}
    	return success("保存成功！");
    }
}
