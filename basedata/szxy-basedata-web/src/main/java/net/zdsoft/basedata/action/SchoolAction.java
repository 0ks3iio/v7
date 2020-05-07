package net.zdsoft.basedata.action;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import net.zdsoft.basedata.dto.SchoolDto;
import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.entity.Region;
import net.zdsoft.basedata.entity.School;
import net.zdsoft.basedata.entity.SchtypeSection;
import net.zdsoft.basedata.service.GradeService;
import net.zdsoft.basedata.service.RegionService;
import net.zdsoft.basedata.service.SchoolService;
import net.zdsoft.basedata.service.SchtypeSectionService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.LoginInfo;
import net.zdsoft.framework.utils.EntityUtils;

@Controller
@RequestMapping("/basedata")
public class SchoolAction extends BaseAction {
	
	@Autowired
	private SchoolService schoolService;
	
	@Autowired
	private RegionService regionService;
	
	@Autowired
	private SchtypeSectionService schtypeSectionService;
	
	@Autowired
	private GradeService gradeService;
	
	
	@RequestMapping("/school/index/page")
	public String schoolIndex(ModelMap map, HttpSession httpSession) {
		LoginInfo loginInfo = getLoginInfo(httpSession);
        String unitId = loginInfo.getUnitId();
		School findOne = schoolService.findOne(unitId);
		SchoolDto dto = new SchoolDto();
		Region findByFullCode = regionService.findByFullCode(findOne.getRegionCode());
		if(findByFullCode!=null){
			dto.setRegionName(findByFullCode.getFullName());
		}
		dto.setSchool(findOne);
		map.put("dto", dto);
		map.put("unitId", loginInfo.getUnitId());
		List<Grade> findByUnitId = gradeService.findByUnitId(loginInfo.getUnitId());
		map.put("gradeSize", findByUnitId.size());
		Map<String, String> findAllMap = schtypeSectionService.findAllMap();
		map.put("schtypeAllMap", findAllMap);
		return "/basedata/school/schoolIndex.ftl";
	}
	
	@ResponseBody
    @RequestMapping("/school/save")
    @ControllerInfo(value = "保存学校")
    public String doSaveSchool(@RequestBody SchoolDto dto) {
    	try{
	        School school = dto.getSchool();
	        if(StringUtils.isBlank(school.getId())){
	        }else{
	        	School schoolOld = schoolService.findOne(school.getId());
	        	EntityUtils.copyProperties(school, schoolOld, true);
	        	schoolOld.setInfantYear(school.getInfantYear());
	        	schoolOld.setGradeYear(school.getGradeYear());
	        	schoolOld.setJuniorYear(school.getJuniorYear());
	        	schoolOld.setSeniorYear(school.getSeniorYear());
	        	school = schoolOld;
	        }
	        SchtypeSection findBySchoolType = schtypeSectionService.findBySchoolType(school.getSchoolType());
	        if(findBySchoolType != null){
	        	school.setSections(findBySchoolType.getSection());
	        }else{
	        	school.setSections("");
	        }
	        schoolService.saveSchool(school);
    	}catch(Exception e){
			e.printStackTrace();
			return returnError();
		}
		return returnSuccess();
    }

}
