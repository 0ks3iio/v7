package net.zdsoft.remote.openapi.action;

import java.text.ParseException;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import net.zdsoft.basedata.remote.service.SemesterRemoteService;
import net.zdsoft.framework.action.JsonResult;

@Controller
@RequestMapping(value = { "/remote/openapi", "/openapi" })
public class OpenApiSemesterAction {
	
	@Autowired
	private SemesterRemoteService semesterRemoteService; 
	
	@RequestMapping("/semester")
	@ResponseBody
	public String getSemester(String acadyear, int semester, @RequestParam(required=false) String unitId) {
		if(StringUtils.isNotBlank(unitId))
			return semesterRemoteService.findByAcadyearAndSemester(acadyear, semester, unitId);
		return semesterRemoteService.findByAcadYearAndSemester(acadyear, semester);
	}
	
	@RequestMapping("/semesterByDate")
	@ResponseBody
	public String getSemester(String dateStr) {
		Date date = null;
		try {
			date = DateUtils.parseDate(dateStr, new String[] {"yyyy-MM-dd", "yyyy/MM/dd", "yyyyMMdd"});
		} catch (ParseException e) {
			e.printStackTrace();
		}
		if(date == null) {
			return JsonResult.error().toJSONString();
		}
		return semesterRemoteService.findSemestersByDate(date);
	}

}
