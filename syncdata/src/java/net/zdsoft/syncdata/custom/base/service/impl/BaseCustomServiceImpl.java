package net.zdsoft.syncdata.custom.base.service.impl;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.collect.Maps;

import net.zdsoft.basedata.constant.BaseSaveConstant;
import net.zdsoft.basedata.remote.service.BaseSyncSaveRemoteService;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.DeptRemoteService;
import net.zdsoft.basedata.remote.service.FamilyRemoteService;
import net.zdsoft.basedata.remote.service.GradeRemoteService;
import net.zdsoft.basedata.remote.service.SchoolRemoteService;
import net.zdsoft.basedata.remote.service.SemesterRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.basedata.remote.service.TeacherDutyRemoteService;
import net.zdsoft.basedata.remote.service.TeacherRemoteService;
import net.zdsoft.basedata.remote.service.UnitRemoteService;
import net.zdsoft.basedata.remote.service.UserRemoteService;
import net.zdsoft.framework.utils.PWD;
import net.zdsoft.syncdata.custom.base.service.BaseCustomService;
import net.zdsoft.system.remote.service.RoleRemoteService;

public abstract class BaseCustomServiceImpl implements BaseCustomService {
	@Autowired
	protected UnitRemoteService unitRemoteService;
	@Autowired
	protected SchoolRemoteService schoolRemoteService;
	@Autowired
	protected RoleRemoteService roleRemoteService;
	@Autowired
	protected TeacherRemoteService teacherRemoteService;
	@Autowired
	protected UserRemoteService userRemoteService;
	@Autowired
	protected DeptRemoteService deptRemoteService;
	@Autowired
	protected ClassRemoteService classRemoteService;
	@Autowired
	protected GradeRemoteService gradeRemoteService;
	@Autowired
	protected TeacherDutyRemoteService teacherDutyRemoteService;
	@Autowired
	protected SemesterRemoteService semesterRemoteService;
	@Autowired
	protected FamilyRemoteService familyRemoteService;
	@Autowired
	protected StudentRemoteService studentRemoteService;
	@Autowired
	protected BaseSyncSaveRemoteService baseSyncSaveService;
	
	@Override
	public void saveEdu() {}

	@Override
	public void saveUnit() {}

	@Override
	public void saveTeacher() {}

	@Override
	public void saveStudent() {}

	@Override
	public void saveFamily() {}

	@Override
	public void saveClass() {}

	@Override
	public void saveGrade() {}

	@Override
	public void saveDept() {}
	
	/**
	 * id不足32位的， 前面补为0
	 * @param id
	 * @return
	 */
	protected String doChangeId(String id) {
		if(StringUtils.isBlank(id)){
			return null;
		}
		return StringUtils.leftPad(id, 32, "0");
	}
	
	/**
	 * 得到默认加密密码
	 * @return
	 */
	protected String getDefaultPwd() {
		return new PWD(BaseSaveConstant.DEFAULT_PASS_WORD_VALUE).encode();
	}
	
	/**
     * 得到学校类型
     * @param name
     * @return
     */
	protected String doChangeSchoolType(String name) {
		String schoolType = "312";//九年一贯制学校;
		if(StringUtils.isNotBlank(name)){
			switch (name) {
			case "幼儿园":
				schoolType = "111"; 
				break;
			case "小学":
				schoolType = "211"; 
				break;
			case "普通初中":
				schoolType = "311"; 
				break;
			case "普通高中":
				schoolType = "342";
				break;
			case "特殊教育":
				schoolType = "514";
				break;
			case "培训学校":
				schoolType = "933";
				break;
			case "中等职业学校":
				schoolType = "361";
				break;
			case "中等职业培训机构":
				schoolType = "369";
				break;
			case "九年一贯制学校":
				schoolType = "312";
				break;
			case "十二年一贯制学校":
				schoolType = "345";
				break;
			default:
				break;
			}
		}
		return schoolType;
	}
	
	/**
	 * 根据学校类型 得到 学段
	 * @param schoolType
	 * @return
	 */
	protected String doChargeSection(String schoolType) {
		String section = "1,2,3";//其他培训机构（含社会培训机构）;
		if(StringUtils.isNotBlank(schoolType)){
			switch (schoolType) {
			case "111":
				section = "0"; 
				break;
			case "211":
				section = "1"; 
				break;
			case "311":
				section = "2"; 
				break;
			case "312":
				section = "1,2"; 
				break;
			case "342":
				section = "3";
				break;
			case "345":
				section = "1,2,3"; 
				break;
			case "351":
				section = "3";
				break;
			case "361":
				section = "3";
				break;
			case "369":
				section = "3";
				break;
			case "371":
				section = "1,2,3";
				break;
			default:
				break;
			}
		}
		return section;
	}
	
	/**
	 * 根据年级名称 来得到 学段
	 * @param
	 * @return
	 */
	protected Map<String, String> getMapByGradeName(String gradeName) {
		String section = null, gradeCode = null, num = null;
		if(StringUtils.isNotBlank(gradeName)){
			switch (gradeName) {
			case "一年级":
				section = "1"; 
				gradeCode = "11";
				num = "0";
				break;
			case "二年级":
				section = "1"; 
				gradeCode = "12";
				num = "1";
				break;
			case "三年级":
				section = "1"; 
				gradeCode = "13";
				num = "2";
				break;
			case "四年级":
				section = "1"; 
				gradeCode = "14";
				num = "3";
				break;
			case "五年级":
				section = "1";
				gradeCode = "15";
				num = "4";
				break;
			case "六年级":
				section = "1";
				gradeCode = "16";
				num = "5";
				break;
			case "七年级":
				section = "2";
				gradeCode = "21";
				num = "0";
				break;
			case "初一":
				section = "2";
				gradeCode = "21";
				num = "0";
				break;
			case "八年级":
				section = "2";
				gradeCode = "22";
				num = "1";
				break;
			case "初二":
				section = "2";
				gradeCode = "22";
				num = "1";
				break;
			case "九年级":
				section = "2";
				gradeCode = "23";
				num = "2";
				break;
			case "初三":
				section = "2";
				gradeCode = "23";
				num = "2";
				break;
			case "高一":
				section = "3";
				gradeCode = "31";
				num = "0";
				break;
			case "高二":
				section = "3";
				gradeCode = "32";
				num = "1";
				break;
			case "高三":
				section = "3";
				gradeCode = "33";
				num = "2";
				break;
			default:
				break;
			}
		}
		Map<String, String> keyMap = Maps.newHashMap();
		keyMap.put("section", section);
		keyMap.put("gradeCode", gradeCode);
		keyMap.put("num", num);
		keyMap.put("gradeName", gradeName);
		return keyMap;
	}
}
