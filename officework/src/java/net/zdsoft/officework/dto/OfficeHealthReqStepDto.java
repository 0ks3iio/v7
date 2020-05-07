package net.zdsoft.officework.dto;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 获取步数、里程、卡路里等信息
 * 如只是获取当日数据，只需传参数"beginDate"，无需携带"endDate"，若传当日时间，则代表获取的当日到获取时刻的步数累计值。
 * @author Administrator
 */
public class OfficeHealthReqStepDto {
	private static final DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
	
	public OfficeHealthReqStepDto(String devSn, String[] studentCodes, Date beginDate, Date endDate){
		this.devSn = devSn;
		this.studentCodes = studentCodes;
		this.beginDate = beginDate;
		this.endDate = endDate;
	}
	
	private String Param;
	
	private String devSn; 
	private String[] studentCodes; 
	private Date beginDate; 
	private Date endDate;
	
	public String getParam() {
		return Param;
	}

	public void setParam(String param) {
		Param = param;
	}

	public String getDevSn() {
		return devSn;
	}

	public void setDevSn(String devSn) {
		this.devSn = devSn;
	}

	public String[] getStudentCodes() {
		return studentCodes;
	}

	public void setStudentCodes(String[] studentCodes) {
		this.studentCodes = studentCodes;
	}

	public Date getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	/**
	 * 请求参数json格式后tostring
	 */
	public JSONObject toJson(){
		
		if(studentCodes==null || studentCodes.length==0)
			return new JSONObject();
		
		JSONArray stuArray = new JSONArray();
		for(String stuCode : studentCodes){
			JSONObject s = new JSONObject();
			s.put("studentId", stuCode);
			stuArray.add(s);
		}
		
		JSONObject parm = new JSONObject();
		parm.put("devSn", devSn);
		parm.put("studentIdList", stuArray);
		if(beginDate!=null)
			parm.put("beginDate", df.format(beginDate));
		if(endDate!=null)
			parm.put("endDate", df.format(endDate));
		
		JSONObject json = new JSONObject();
		json.put("Method", "getStudentStepInfo");
		json.put("Param", parm);
		
		return json;
	}
}
