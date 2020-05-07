package net.zdsoft.savedata.action.business;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import net.zdsoft.basedata.constant.BaseSaveConstant;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.StringUtils;
import net.zdsoft.savedata.action.BaseSyncAction;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;


@Controller
@RequestMapping(value = { "/remote/openapi/sync", "/openapi/sync" })
public class UpdateStudentAction extends BaseSyncAction{
	
	
	/**
	 * 
	 * @param data
	 * @param type  1--学生id 2--身份证
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "update/stuCardNumber", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String addCheckWork(@RequestBody String data, String type) {
		try {
			if(StringUtils.isNotBlank(data)) {
				JSONArray array = Json.parseArray(data);
				List<String> studentParamList = new ArrayList<>();
				List<String> oneCardNumList = new ArrayList<>();
				List<Integer> errorList = new ArrayList<>(); 
				List<Student> updateStudents = new ArrayList<>();
				for (int i = 0; i < array.size(); i++) {
					JSONObject js = array.getJSONObject(i);
					String cardNumber  = js.getString("cardNumber");
					String param;
					if(StringUtils.isBlank(type) || "1".equals(type)){
						param = js.getString("id");
					}else{
						param = js.getString("identityCard");
					}
					if(StringUtils.isNotBlank(param) && StringUtils.isNotBlank(cardNumber) 
							&& cardNumber.length() <= 20){
						studentParamList.add(param);
						oneCardNumList.add(cardNumber);
					}else{
						errorList.add(i);
					}
				}
				if(CollectionUtils.isNotEmpty(errorList))
					return returnMsg(BaseSaveConstant.BASE_ERROR_CODE, "数据更新失败", null, errorList.toString());
				if(CollectionUtils.isNotEmpty(studentParamList)){
					List<Student> students = new ArrayList<>();
					Map<String,Student> paramMap = new HashMap<String, Student>();
					Map<String,List<Student>> idcardMap = new HashMap<String, List<Student>>();
					if(StringUtils.isBlank(type) || "1".equals(type)){
						students = SUtils.dt(studentRemoteService.findListByIds(studentParamList.toArray(new String[studentParamList.size()])), Student.class);
						paramMap = EntityUtils.getMap(students, Student::getId);
					}else{
						students = SUtils.dt(studentRemoteService.findByIdentityCards(studentParamList.toArray(new String[studentParamList.size()])), Student.class);
						idcardMap = EntityUtils.getListMap(students, Student::getIdentityCard, Function.identity());
					}
					Map<String, Student> haveCardNumMap = new HashMap<>();
					if(CollectionUtils.isNotEmpty(students)){
						Set<String> uidSet = EntityUtils.getSet(students, Student::getSchoolId);
						List<Student> allStudents = SUtils.dt(studentRemoteService.findBySchoolIdIn(null, uidSet.toArray(new String[uidSet.size()])), Student.class);
						allStudents.forEach(c->{
							String key = c.getSchoolId() + c.getCardNumber();
							haveCardNumMap.put(key, c);
						});
					}
					for (int i = 0; i < array.size(); i++) {
						JSONObject js = array.getJSONObject(i);
						String cardNumber = js.getString("cardNumber");
						String param;
						Student stu = null;
						if(StringUtils.isBlank(type) || "1".equals(type)){
							param = js.getString("id");
							if(MapUtils.isNotEmpty(paramMap) && paramMap.get(param) != null){
								stu = paramMap.get(param);
							}
						}else{
							param = js.getString("identityCard");
							if(MapUtils.isNotEmpty(idcardMap) && CollectionUtils.isNotEmpty(idcardMap.get(param))){
								if(idcardMap.get(param).size() > 1){
									errorList.add(i);
								}else{
									stu = idcardMap.get(param).get(0);
								}
							}
						}
						if(stu != null){
							//验证这个cardNumber 在同一个单位下面是否存在
							String key1 = stu.getSchoolId() + cardNumber;
							if(MapUtils.isNotEmpty(haveCardNumMap) && haveCardNumMap.get(key1) != null){
								errorList.add(i);
							}else{
								stu.setCardNumber(cardNumber);
								updateStudents.add(stu);
							}
						}
					}
				}
				if(CollectionUtils.isNotEmpty(errorList)){
					return returnMsg(BaseSaveConstant.BASE_ERROR_CODE, "数据更新失败", "身份证号或一卡通卡号有重复", errorList.toString());
				}
				if(CollectionUtils.isNotEmpty(updateStudents)){
					studentRemoteService.saveAll(updateStudents.toArray(new Student[0]));
				}
				return returnMsg(BaseSaveConstant.BASE_SUCCESS_CODE,"数据保存成功");
			}
			return returnSuccess();
		} catch (Exception e) {
			log.error("更新学生一卡通数据失败,错误信息是：-------"+e.getMessage());
			return returnMsg(e);
		}
	}

}
