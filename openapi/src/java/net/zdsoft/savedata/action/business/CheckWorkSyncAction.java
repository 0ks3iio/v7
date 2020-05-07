package net.zdsoft.savedata.action.business;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.zdsoft.basedata.constant.BaseSaveConstant;
import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.framework.utils.StringUtils;
import net.zdsoft.savedata.action.BaseSyncAction;
import net.zdsoft.savedata.entity.CheckWorkData;
import net.zdsoft.savedata.service.BusinessSyncSaveService;
import net.zdsoft.officework.remote.service.OfficeHealthDoinoutInfoRemoteService;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 考勤的业务接口
 * @author yangsj
 *
 */
@Controller
@RequestMapping(value = { "/remote/openapi/sync", "/openapi/sync" })
public class CheckWorkSyncAction extends BaseSyncAction{

	@Autowired
	@Lazy
	private OfficeHealthDoinoutInfoRemoteService officeHealthDoinoutInfoRemoteService;
	
	@Autowired
	private BusinessSyncSaveService businessSyncSaveService;
	/**
	 * 保存考勤数据 （）
	 * @param jsonStr
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "checkWork", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String addCheckWork(@RequestBody String jsonStr) {
		try {
			if(StringUtils.isNotBlank(jsonStr)) {
				String dataList = doExcuteDate(jsonStr);
				if(StringUtils.isNotBlank(dataList)) {
					List<CheckWorkData> checkWorkDatas =  JSONArray.parseArray(dataList, CheckWorkData.class);
					if(CollectionUtils.isNotEmpty(checkWorkDatas)) {
						//截取数据
						checkWorkDatas = getLimitData(checkWorkDatas);
						Map<String,String> returnMsg = new HashMap<>();
						Semester semester = getCurrentSemester();
						if (semester == null) {
							return returnMsg(BaseSaveConstant.BASE_ERROR_CODE, "获取不到学年学期数据！");
						}
						if(CollectionUtils.isNotEmpty(checkWorkDatas)) {
							for (CheckWorkData checkWorkData : checkWorkDatas) {
								String unitIdent = checkWorkData.getUnitIdent();
								String userIdent = checkWorkData.getUserIdent();
								String placeIdent = checkWorkData.getPlaceIdent();
								Date attendanceDate = checkWorkData.getAttendanceDate();
								String areaSource = checkWorkData.getAreaSource();
								//主要的字段非空
							    if(StringUtils.isBlank(unitIdent) || StringUtils.isBlank(userIdent) || StringUtils.isBlank(placeIdent)
							    		|| attendanceDate == null || StringUtils.isBlank(areaSource)){
							    	return error("数据格式不对,重新检查");
							    }
							}
							businessSyncSaveService.saveCheckWork(checkWorkDatas.toArray(new CheckWorkData[checkWorkDatas.size()]),returnMsg);
						}
						StringBuilder detailError = new StringBuilder().append("详细错误:");
						int errorField = getErrorFieldNum(returnMsg,detailError);
						if(errorField >0 ){
							return returnMsg(BaseSaveConstant.BASE_HALF_SUCCESS_CODE, detailError.toString(), detailError.toString(), returnMsg.get(BaseSaveConstant.PROVING_BASE_SAVE_ERROR_KEY));
						}else{
							return returnMsg(BaseSaveConstant.BASE_SUCCESS_CODE,"数据保存成功");
						}
					}
				}
			}
			return returnSuccess();
		} catch (Exception e) {
			log.error("保存第三方考勤数据失败,错误信息是：-------"+e.getMessage());
			return returnMsg(e);
		}
	}
	
	/**
	 * 保存考勤数据流水 
	 * @param jsonStr
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "checkWorkSerial", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String addCheckSerial(@RequestBody String jsonStr) {
		try {
			if(StringUtils.isNotBlank(jsonStr)) {
				String dataList = doExcuteDate(jsonStr);
				if(StringUtils.isNotBlank(dataList)) {
					 String returnMsg =  officeHealthDoinoutInfoRemoteService.doTBInOut(dataList);
					 JSONObject retJson = JSONObject.parseObject(returnMsg);
					 return returnMsg(retJson.getString("code"),retJson.getString("message"));
				}
			}
			return returnSuccess();
		} catch (Exception e) {
			log.error("保存考勤流水数据失败,错误信息是：-------"+e.getMessage());
			return returnMsg(e);
		}
	}
}
