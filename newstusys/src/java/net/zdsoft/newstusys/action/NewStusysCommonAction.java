package net.zdsoft.newstusys.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Family;
import net.zdsoft.basedata.entity.Region;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.FamilyRemoteService;
import net.zdsoft.basedata.remote.service.RegionRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.config.Evn;
import net.zdsoft.framework.entity.Constant;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.newstusys.constants.BaseStudentConstants;
import net.zdsoft.newstusys.entity.BaseStudent;
import net.zdsoft.newstusys.entity.StudentResume;
import net.zdsoft.newstusys.service.StudentResumeService;
import net.zdsoft.system.remote.service.SysOptionRemoteService;

/**
 * 
 * @author weixh
 * 2019年10月14日	
 */
@Controller
@RequestMapping("/newstusys/common")
public class NewStusysCommonAction extends BaseAction{
	@Autowired
	private ClassRemoteService classRemoteService;
	@Autowired
	private StudentRemoteService studentRemoteService;
	@Autowired
    private StudentResumeService studentResumeService;
    @Autowired
    private FamilyRemoteService familyRemoteService;
    @Autowired
    private RegionRemoteService regionRemoteService;
	
	@RequestMapping("/pdfHtml")
	public String pdfExport(HttpServletRequest req, ModelMap map) {
		String clsId = req.getParameter("clsId");
		Clazz clazz = SUtils.dc(classRemoteService.findOneById(clsId), Clazz.class);
		String stuId = req.getParameter("stuId");
		List<Student> stuList ;
		if(StringUtils.isNotEmpty(stuId)) {
			stuList = new ArrayList<>();
			stuList.add(Student.dc(studentRemoteService.findOneById(stuId)));
		} else {
			stuList = Student.dt(studentRemoteService.findByClassIds(new String[] {clsId}));
		}
//		System.out.println("stuId="+stuId+"&====stuList=="+stuList.size());
		if(CollectionUtils.isNotEmpty(stuList)) {
			List<String> stuIds = EntityUtils.getList(stuList, Student::getId);
			List<Region> regionList = Region.dt(regionRemoteService.findByType(Region.TYPE_1));
			Map<String, String> regionMap = EntityUtils.getMap(regionList, Region::getFullCode, Region::getFullName);
			List<Family> clsFams = SUtils.dt(familyRemoteService.findByStudentIds(stuIds.toArray(new String[0])), Family.class);
			Map<String, List<Family>> sfMap = EntityUtils.getListMap(clsFams, Family::getStudentId, Function.identity());
			List<StudentResume> clsRes = studentResumeService.findByStuids(stuIds.toArray(new String[0]));
			Map<String, List<StudentResume>> srMap = EntityUtils.getListMap(clsRes, StudentResume::getStuid, Function.identity());
			List<BaseStudent> studentList = new ArrayList<>();
			Family family = new Family();
			String furl = Evn.<SysOptionRemoteService> getBean("sysOptionRemoteService").findValue(Constant.FILE_URL) + "/store/";// 文件系统地址
			for(Student stu : stuList) {
				BaseStudent bs = new BaseStudent();
				EntityUtils.copyProperties(stu, bs);
				
				if (StringUtils.isNotEmpty(bs.getFilePath())) {
	                bs.setHasPic(true);
	                bs.setOldFilePath(furl+bs.getFilePath());
//	                bs.setOldFilePath("http://depot.nipic.com/file/20150423/20829447_16153273773.jpg");
	            }
				
				bs.setClassName(clazz.getClassNameDynamic());
				List<Family> familyList = sfMap.get(stu.getId());
				if (CollectionUtils.isNotEmpty(familyList)) {
	                for (Family f : familyList) {
	                    if (f.getIsGuardian() == BaseStudentConstants.RELATION_IS_GUARDIAN) {
	                        bs.setFamily1(f);
	                    }
	                    if (BaseStudentConstants.RELATION_FATHER.equals(f.getRelation())) {
	                    	bs.setFamily2(f);
	                    } else if (BaseStudentConstants.RELATION_MOTHER.equals(f.getRelation())) {
	                    	bs.setFamily3(f);
	                    }
	                }
	            }
				if(bs.getFamily1() == null) {
					bs.setFamily1(family);
				}
				if(bs.getFamily2() == null) {
					bs.setFamily2(family);
				}
				if(bs.getFamily3() == null) {
					bs.setFamily3(family);
				}
				
				List<StudentResume> resus = srMap.get(stu.getId());
				if(resus == null) {
					resus = new ArrayList<>();
				}
//				else {
//					resus.addAll(resus);
//					resus.addAll(resus);
//				}
				bs.setStudentResumeList(resus);
				
				if(StringUtils.isNotEmpty(bs.getRegisterPlace())) {
					bs.setRegisterPlace(regionMap.get(bs.getRegisterPlace()));
				}
				if(StringUtils.isNotEmpty(bs.getNativePlace())) {
					bs.setNativePlace(regionMap.get(bs.getNativePlace()));
				}
				
				studentList.add(bs);
			}
			
			map.put("studentList", studentList);
		}
		return "/newstusys/sch/studentShow/htmlStudentDetail.ftl";
	}

}
