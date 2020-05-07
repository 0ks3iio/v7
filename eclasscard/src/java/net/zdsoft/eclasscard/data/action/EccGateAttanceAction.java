package net.zdsoft.eclasscard.data.action;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.entity.User;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.GradeRemoteService;
import net.zdsoft.basedata.remote.service.SemesterRemoteService;
import net.zdsoft.basedata.remote.service.UserRemoteService;
import net.zdsoft.eclasscard.data.dto.AttanceSearchDto;
import net.zdsoft.eclasscard.data.entity.EccGateAttance;
import net.zdsoft.eclasscard.data.service.EccGateAttanceService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.StringUtils;
import net.zdsoft.stuwork.remote.service.StuworkRemoteService;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/eclasscard")
public class EccGateAttanceAction extends BaseAction{
	@Autowired
	private ClassRemoteService classRemoteService;
	@Autowired
	private UserRemoteService userRemoteService;
	@Autowired
	private EccGateAttanceService eccGateAttanceService;
	@Autowired
	private SemesterRemoteService semesterRemoteService;
	@Autowired
	private GradeRemoteService gradeRemoteService;
	@Autowired
	private StuworkRemoteService stuworkRemoteService;
	
	@RequestMapping("/gate/attance/index/page")
	public String attanceIndex(ModelMap map){
		
		User user=SUtils.dc(userRemoteService.findOneById(getLoginInfo().getUserId()),User.class);
		if(user==null){
			return errorFtl(map, "用户信息有误");
		}
		List<Clazz> clazzList=SUtils.dt(classRemoteService.findBySchoolIdTeacherId(getLoginInfo().getUnitId(),user.getOwnerId()),new TR<List<Clazz>>(){});
		//String ss=getLoginInfo().getOwnerId();
		if(CollectionUtils.isNotEmpty(clazzList)) {
			Collections.sort(clazzList, new Comparator<Clazz>() {
				@Override
				public int compare(Clazz o1, Clazz o2) {
					return o1.getClassCode().compareTo(o2.getClassCode());
				}
			});
		}
		map.put("clazzList", clazzList);
		return "/eclasscard/gate/gateAttMyClassIndex.ftl";
	}
	@RequestMapping("/gate/attance/myClassList/page")
	public String myClassList(ModelMap map,AttanceSearchDto attDto){
		String unitId=getLoginInfo().getUnitId();
		
		Semester se=SUtils.dc(semesterRemoteService.getCurrentSemester(0, unitId),Semester.class);
		if(se==null){
			return errorFtl(map, "当前时间不在学年学期范围内");
		}
		User user=SUtils.dc(userRemoteService.findOneById(getLoginInfo().getUserId()),User.class);
		if(user==null){
			return errorFtl(map, "用户信息有误");
		}
		List<Clazz> clazzList=SUtils.dt(classRemoteService.findBySchoolIdTeacherId(unitId,user.getOwnerId()),new TR<List<Clazz>>(){});
		
		if(CollectionUtils.isNotEmpty(clazzList)){
			Set<String> gradeIds=new HashSet<String>();
			if(StringUtils.isBlank(attDto.getClassId())){
				attDto.setClassId(clazzList.get(0).getId());
			}
			for(Clazz clazz:clazzList){
				gradeIds.add(clazz.getGradeId());
			}
			Map<String,String> gradeNameMap=EntityUtils.getMap(SUtils.dt((gradeRemoteService.findListByIds(gradeIds.toArray(new String[0])))
					,new TR<List<Grade>>(){}),"id","gradeName");
			for(Clazz clazz:clazzList){
				clazz.setClassNameDynamic(gradeNameMap.get(clazz.getGradeId())+clazz.getClassName());
			}
		}
		if(attDto.getSearchDate()==null){
			attDto.setSearchDate(new Date());
		}
		
		List<EccGateAttance> gateList=eccGateAttanceService.getMyClassListByCon(unitId, attDto);
		
		map.put("attDto", attDto);
		map.put("gateList", gateList);
		if(CollectionUtils.isNotEmpty(clazzList)) {
			Collections.sort(clazzList, new Comparator<Clazz>() {
				@Override
				public int compare(Clazz o1, Clazz o2) {
					return o1.getClassCode().compareTo(o2.getClassCode());
				}
			});
		}
		map.put("clazzList", clazzList);
		map.put("teacherName", user.getRealName());
		//map.put("beginDate", se.getSemesterBegin());
		return "/eclasscard/gate/gateAttMyClassList.ftl";
	}
	
	@ResponseBody
	@RequestMapping("/gate/attance/saveStatus")
	public String saveStatus(ModelMap map,String[] ids,int status){
		try{
			eccGateAttanceService.updateStatus(ids, status);
		}catch(Exception e){
			e.printStackTrace();
			return returnError();
		}
		return returnSuccess();
	}
	
	//*******************一下全校学生数据**********分割线********
	@RequestMapping("/gate/attance/schoolList/page")
	public String schoolList(ModelMap map,AttanceSearchDto attDto,HttpServletRequest request){
		String unitId=getLoginInfo().getUnitId();
		
		Semester se=SUtils.dc(semesterRemoteService.getCurrentSemester(0, unitId),Semester.class);
		if(se==null){
			return errorFtl(map, "当前时间不在学年学期范围内");
		}
		Pagination page=createPagination();
		if(attDto.getSearchDate()==null){
			attDto.setSearchDate(new Date());//默认当前时间
		}
		List<Grade> gradeList=SUtils.dt(gradeRemoteService.findBySchoolId(unitId), new TR<List<Grade>>(){});
		List<Clazz> clazzList=null;
		if(StringUtils.isNotBlank(attDto.getGradeId())){
			clazzList=SUtils.dt(classRemoteService.findByGradeIdSortAll(attDto.getGradeId()), new TR<List<Clazz>>(){});
		}else{
			clazzList=SUtils.dt(classRemoteService.findByIdCurAcadyear(unitId,se.getAcadyear()), new TR<List<Clazz>>(){});
		}
		
		//班级权限
		Set<String> classPermission = stuworkRemoteService.findClassSetByUserId(getLoginInfo().getUserId());
				
		Iterator<Clazz> it = clazzList.iterator();
		while(it.hasNext()) {
			Clazz clazz = it.next();
			if (!classPermission.contains(clazz.getId())) {
				it.remove();
			}
		}
		
		List<EccGateAttance> gateList=eccGateAttanceService.getSchoolListByCon(unitId,clazzList, attDto,page);
		map.put("attDto", attDto);
		map.put("gradeList", gradeList);
		map.put("clazzList", clazzList);
		map.put("gateList", gateList);
		//map.put("beginDate", se.getSemesterBegin());
		sendPagination(request, map, page);
		return "/eclasscard/gate/gateAttSchoolList.ftl";
	}
}
