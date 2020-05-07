package net.zdsoft.datacollection.action;

import java.util.Date;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import net.zdsoft.basedata.remote.service.SemesterRemoteService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;

@Controller
@RequestMapping("/dc/semester")
public class DcSemesterAction extends BaseAction {

	@Autowired
	private SemesterRemoteService sermesterRemoteService;

	@ResponseBody
	@ControllerInfo(ignoreLog=1)
	@RequestMapping("/currentSemester")
	/**
	 * 当前学年学期
	 * @return
	 */
	public String currentSemester() {
		String s = sermesterRemoteService.findByCurrentDay(DateFormatUtils.format(new Date(), "yyyy-MM-dd"));
		return s;
	}
}
