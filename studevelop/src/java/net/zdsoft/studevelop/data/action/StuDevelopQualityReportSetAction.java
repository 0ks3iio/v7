package net.zdsoft.studevelop.data.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.remote.service.SchoolRemoteService;
import net.zdsoft.basedata.remote.service.SemesterRemoteService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.studevelop.data.constant.StuDevelopConstant;
import net.zdsoft.studevelop.data.entity.StuDevelopQualityReportSet;
import net.zdsoft.studevelop.data.service.StuDevelopQualityReportSetService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 模板选择设置
 */
@Controller
@RequestMapping("/studevelop")
public class StuDevelopQualityReportSetAction extends BaseAction{
	
	@Autowired
	private SemesterRemoteService semesterRemoteService;
	@Autowired
	private SchoolRemoteService schoolRemoteService;
	@Autowired
	private StuDevelopQualityReportSetService stuDevelopQualityReportSetService;
	
	@RequestMapping("/stuQualityReportSet/index/page")
    @ControllerInfo(value = "奖惩登记tab")
	public String qualityReportSetHead(ModelMap map){
		List<String> acadyearList = SUtils.dt(semesterRemoteService.findAcadeyearList(), new TR<List<String>>(){});
        if(CollectionUtils.isEmpty(acadyearList)){
			return errorFtl(map,"学年学期不存在");
		}
        String unitId = getLoginInfo().getUnitId();
        Semester semester = SUtils.dc(semesterRemoteService.getCurrentSemester(1,unitId), Semester.class);
        map.put("acadyearList", acadyearList);
        map.put("semester", semester);
        //获取学段
        String sections = schoolRemoteService.findSectionsById(unitId);		
		String[] sectionArray = sections.split(",");
		Map<String, String> sectionMap = new HashMap<String, String>();
		if(sectionArray.length==0){
			map.put("section", sections);
		}else{
		    for(String str : sectionArray){
		    	if("1".equals(str)){
		    		sectionMap.put("1", "小学");
		    	}else if("2".equals(str)){
		    		sectionMap.put("2", "初中");
		    	}/*else if("3".equals(str)){
		    		sectionMap.put("3", "高中");
		    	}*/
		    }
		    map.put("sectionMap", sectionMap);
		}
		if(sectionMap.size() == 0 && StringUtils.isNotBlank(sections)){
			return errorFtl(map,"暂不支持小初学段之外的报告单设置");
		}else if(StringUtils.isBlank(sections)){
			return errorFtl(map,"学段不存在");
		}
		return "/studevelop/query/qualityReportSetHead.ftl";
	}
	
	@RequestMapping("/stuQualityReportSet/list")
    @ControllerInfo(value = "奖惩登记tab")
	public String qualityReportSetList(String acadyear, String semester, int section, ModelMap map){
		StuDevelopQualityReportSet stuDevelopQualityReportSet;
		stuDevelopQualityReportSet = stuDevelopQualityReportSetService.findByAll(getLoginInfo().getUnitId(), acadyear, semester, section);
		if(null == stuDevelopQualityReportSet){
			stuDevelopQualityReportSet = stuDevelopQualityReportSetService.findByAll(StuDevelopConstant.DEFAULT_UNIT_ID, StuDevelopConstant.DEFAULT_ACADYEAR, StuDevelopConstant.DEFAULT_SEMESTER, section);
		}
		if(null == stuDevelopQualityReportSet) {
			stuDevelopQualityReportSet = new StuDevelopQualityReportSet();
			stuDevelopQualityReportSet.setSection(section);
		}
		map.put("stuDevelopQualityReportSet", stuDevelopQualityReportSet);
		return "/studevelop/query/qualityReportSetList.ftl";
	}
	
	@ResponseBody
	@RequestMapping("/stuQualityReportSet/templateSave")
    @ControllerInfo(value = "保存")
	public String templateSave(String acadyear, String semester, int section, String template){
		try {
			StuDevelopQualityReportSet stuDevelopQualityReportSetTemp = stuDevelopQualityReportSetService.findByAll(getLoginInfo().getUnitId(), acadyear, semester, section);
			if(null!=stuDevelopQualityReportSetTemp){
				if("1".equals(template)){
					stuDevelopQualityReportSetTemp.setTemplate(StuDevelopConstant.REPORT_TEMPLATE_1);
				}else if("2".equals(template)){
					stuDevelopQualityReportSetTemp.setTemplate(StuDevelopConstant.REPORT_TEMPLATE_2);
				}
				stuDevelopQualityReportSetService.save(stuDevelopQualityReportSetTemp);
			}else{
				StuDevelopQualityReportSet stuDevelopQualityReportSet = new StuDevelopQualityReportSet();
				if(StringUtils.isBlank(stuDevelopQualityReportSet.getId())){
					stuDevelopQualityReportSet.setId(UuidUtils.generateUuid());
				}
				stuDevelopQualityReportSet.setAcadyear(acadyear);
				stuDevelopQualityReportSet.setSemester(semester);
				stuDevelopQualityReportSet.setSection(section);
				if("1".equals(template)){
					stuDevelopQualityReportSet.setTemplate(StuDevelopConstant.REPORT_TEMPLATE_1);
				}else if("2".equals(template)){
					stuDevelopQualityReportSet.setTemplate(StuDevelopConstant.REPORT_TEMPLATE_2);
				}
				stuDevelopQualityReportSet.setUnitId(getLoginInfo().getUnitId());
				stuDevelopQualityReportSetService.save(stuDevelopQualityReportSet);
			} 
		} catch (Exception e) {
			e.printStackTrace();
			return returnError();
		}
		return returnSuccess();
	}

}
