package net.zdsoft.officework.action;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.entity.User;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.basedata.remote.service.UserRemoteService;
import net.zdsoft.eclasscard.data.constant.EccConstants;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.DateUtils;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.officework.constant.OfficeConstants;
import net.zdsoft.officework.entity.OfficeHealthCount;
import net.zdsoft.officework.service.OfficeHealthCountService;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;

@Controller
@RequestMapping("/eccShow/eclasscard")
public class OfficeHealthAction extends BaseAction{

	@Autowired 
	private OfficeHealthCountService officeHealthCountService;
	@Autowired 
	private UserRemoteService userRemoteService;
	@Autowired 
	private StudentRemoteService studentRemoteService;
	
	@RequestMapping("/stuHealthIndex/page")
	@ControllerInfo("电子班牌学生健康信息Index")
	public String stuHealth(String userId,String view,ModelMap map){
		if(StringUtils.isBlank(userId)){
			userId=getLoginInfo().getUserId();
		}
		map.put("userId", userId);
		map.put("view", view);
		if (EccConstants.ECC_VIEW_1.equals(view)) {
			return "/eclasscard/show/studentspace/eccStudentHealthIndex.ftl";
		} else {
			return "/eclasscard/verticalshow/studentspace/eccStudentHealthIndex.ftl";
		}
	}
	
	@RequestMapping("/stuHealthData/page")
	@ControllerInfo("电子班牌学生健康信息Data")
	public String stuHealth(String view,String userId,String dateType,String startTime,String endTime,ModelMap map){
		User user = SUtils.dc(userRemoteService.findOneById(userId), User.class);
		String studentId = user.getOwnerId();
		Student student = SUtils.dc(studentRemoteService.findOneById(studentId), Student.class);
		List<Student> studentList = SUtils.dt(studentRemoteService.findByClassIds(student.getClassId()), new TR<List<Student>>(){});
		List<String> studentIds = EntityUtils.getList(studentList, "id");
		Calendar calendar =Calendar.getInstance();
		calendar.setTime(DateUtils.string2Date(startTime));
		String stepList = "";
		String TimeList = "";
		Integer step = 0;
		Double distance = 0.0;
		Double calorie = 0.0;
		Integer rank = 0;
		if (OfficeConstants.HEALTH_DATE_DAY.equals(dateType)) {
			Map<Integer,OfficeHealthCount> healthList=officeHealthCountService.getOfficeHealthCountByStudentId(studentId,dateType,startTime,endTime);
			for (int i=1;i<=24;i++) {
				OfficeHealthCount count = healthList.get(i);
				if (count !=null) {
					step += count.getStep();
					distance += count.getDistance();
					calorie += count.getCalorie();
					stepList += ",'" +  count.getStep() + "'";
				} else {
					stepList += ",'" +  0 + "'";
				}
				TimeList += ",'" + i + "时'";
			}
			stepList = stepList.substring(1);
			TimeList = TimeList.substring(1);
			map.put("stepList", "["+stepList+"]");
			map.put("TimeList","["+TimeList+"]");
		} else if (OfficeConstants.HEALTH_DATE_WEEK.equals(dateType)) {
			Map<Integer,OfficeHealthCount> healthList=officeHealthCountService.getOfficeHealthCountByStudentId(studentId,dateType,startTime,endTime);
			for (int i=1;i<=7;i++) {
				OfficeHealthCount count = healthList.get(i);
				if (count !=null) {
					step += count.getStep();
					distance += count.getDistance();
					calorie += count.getCalorie();
					stepList += ",'" +  count.getStep() + "'";
				} else {
					stepList += ",'" +  0 + "'";
				}
			}
			stepList = stepList.substring(1);
			map.put("stepList", "["+stepList+"]");
			map.put("TimeList","['周日','周一','周二','周三','周四','周五','周六']");
		} else {
			Map<Integer,OfficeHealthCount> healthList=officeHealthCountService.getOfficeHealthCountByStudentId(studentId,dateType,startTime,endTime);
			for (int i=1;i<=calendar.getActualMaximum(Calendar.DAY_OF_MONTH);i++) {
				OfficeHealthCount count = healthList.get(i);
				if (count != null) {
					step += count.getStep();
					distance += count.getDistance();
					calorie += count.getCalorie();
					stepList += ",'" +  count.getStep() + "'";
				} else {
					stepList += ",'" +  0 + "'";
				}
				TimeList += ",'" + i + "号'";
			}
			stepList = stepList.substring(1);
			TimeList = TimeList.substring(1);
			map.put("stepList", "["+stepList+"]");
			map.put("TimeList","["+TimeList+"]");
		}
		
		rank = officeHealthCountService.getRankByStudentId(student,dateType,startTime,endTime);
		
		if (distance != 0.0) {
			distance = formatDouble2(distance/1000,1);
		}
		if (calorie != 0.0) {
			calorie = formatDouble2(calorie/1000,0);
		}
		map.put("step", step);
		map.put("distance", distance);
		map.put("calorie", calorie);
		map.put("rank", rank);
		if (EccConstants.ECC_VIEW_1.equals(view)) {
			return "/eclasscard/show/studentspace/eccStudentHealthData.ftl";
		} else {
			return "/eclasscard/verticalshow/studentspace/eccStudentHealthData.ftl";
		}
	}
	
	@ResponseBody
	@RequestMapping("/dateOfWeek")
	@ControllerInfo("时间切换")
	public List<String> dateOfWeek(String dateType,String numberOfTime,String num) {
		Calendar calendar =Calendar.getInstance();
		Calendar nowcalendar =Calendar.getInstance();
		List<String> dataList = Lists.newArrayList();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd");
		if (StringUtils.isNotBlank(numberOfTime)) {
			calendar.setTime(DateUtils.string2Date(numberOfTime));
		} else {
			calendar.setTime(new Date());
		}
		Date startTime = new Date();
		Date endTime = new Date();
		if (OfficeConstants.HEALTH_DATE_DAY.equals(dateType)) {
			calendar.add(Calendar.DATE, Integer.parseInt(num));
			if (calendar.getTimeInMillis() > nowcalendar.getTimeInMillis()) {
				calendar = nowcalendar;
			}
			dataList.add(DateUtils.date2StringByDay(calendar.getTime()));
			dataList.add(simpleDateFormat.format(calendar.getTime()));
			dataList.add(DateUtils.date2StringByDay(calendar.getTime()));
		} else if (OfficeConstants.HEALTH_DATE_WEEK.equals(dateType)) {
			calendar.add(Calendar. WEEK_OF_YEAR, Integer.parseInt(num));
			if (calendar.getTimeInMillis() > nowcalendar.getTimeInMillis()) {
				calendar = nowcalendar;
			}
			calendar.set(Calendar. DAY_OF_WEEK, Calendar.SUNDAY);
			startTime = calendar.getTime();
			calendar.set(Calendar. DAY_OF_WEEK, Calendar.SATURDAY); 
			endTime = calendar.getTime();
		} else {
			calendar.add(Calendar.MONTH, Integer.parseInt(num));
			if (calendar.getTimeInMillis() > nowcalendar.getTimeInMillis()) {
				calendar = nowcalendar;
			}
			calendar.set(Calendar. DAY_OF_MONTH,1); 
			startTime = calendar.getTime();
			calendar.set(Calendar. DAY_OF_MONTH,calendar.getActualMaximum(Calendar.DAY_OF_MONTH)); 
			endTime = calendar.getTime();
		}
		if (OfficeConstants.HEALTH_DATE_WEEK.equals(dateType) || OfficeConstants.HEALTH_DATE_MONTH.equals(dateType)) {
			dataList.add(DateUtils.date2StringByDay(startTime));
			dataList.add(simpleDateFormat.format(startTime)+"-"+simpleDateFormat.format(endTime));
			dataList.add(DateUtils.date2StringByDay(endTime));
		}
		return dataList;
	}
	
	/**
	 * 四舍五入
	 * @param d
	 * @param scale 保留几位小数
	 * @return
	 */
	public double formatDouble2(double d, int scale) {
        // 新方法，如果不需要四舍五入，可以使用RoundingMode.DOWN
        BigDecimal bg = new BigDecimal(d).setScale(scale, BigDecimal.ROUND_HALF_UP);
        return bg.doubleValue();
    }
	
}
