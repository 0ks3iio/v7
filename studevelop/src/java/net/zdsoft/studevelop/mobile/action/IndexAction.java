package net.zdsoft.studevelop.mobile.action;

import net.zdsoft.basedata.entity.Family;
import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.remote.service.FamilyRemoteService;
import net.zdsoft.basedata.remote.service.SemesterRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.framework.action.MobileAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.WeiKeyUtils;
import net.zdsoft.studevelop.mobile.entity.StuIntroduction;
import net.zdsoft.studevelop.mobile.service.StuIntroductionService;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/mobile/open/studevelop")
public class IndexAction extends MobileAction{
	
	@Autowired
	private SemesterRemoteService semesterRemoteService;
	@Autowired
	private StudentRemoteService studentRemoteService;
	@Autowired
	private StuIntroductionService stuIntroductionService;
	@Autowired
	private FamilyRemoteService familyRemoteService;
	
	/**
	 * 学生成长手册--微课跳转地址
	 * @param token 微课校验token信息
	 * @param ownerType 1:学生， 3: 家长
	 */
	@RequestMapping("/index")
    @ControllerInfo(value = "首页")
	public String showIndex(String token, ModelMap map){
		try {
			map.put("indexPage", true);
			if(StringUtils.isBlank(token)){
				return errorFtl(map, "token信息为空，请联系管理员");
			}
			String ownerId = WeiKeyUtils.decodeByDes(token);
			//判断是家长登陆还是学生登陆
			//String ownerId = "AD598485F903451396CC068A0F5D3D4A";
			/*String token1=WeiKeyUtils.encodeByDes("04FE0993F033477083262EF0DB4CA5B6", "abcd1234ABCD1234");
			System.out.println(token1);*/

//			ownerId = "11d4d0b4e2ce4959bfdbd497ccf7d8fd";
			return showHomepage(ownerId, map);
			
		} catch (Exception e) {
			e.printStackTrace();
			return errorFtl(map, "发送未知错误");
		}
	}
	
	/**
	 * 学生成长手册--首页
	 */
	@RequestMapping("/homepage")
    @ControllerInfo(value = "首页")
	public String showHomepage(String ownerId, ModelMap map){
		try {
			map.put("indexPage", true);
			boolean isFamily = true;
			Family family = SUtils.dc(familyRemoteService.findOneById(ownerId), Family.class);
			String studentId = null;
			if(family!=null){//家长登陆
				studentId = family.getStudentId();
				//isFamily = true;
			}else{//学生账号登陆
				studentId = ownerId;
			}
			
			Student student = SUtils.dc(studentRemoteService.findOneById(studentId), Student.class);
			
			if(student==null){
				return errorFtl(map, "找不到用户信息，请联系管理员");
			}
			
			
			Semester semesterObj = SUtils.dc(semesterRemoteService.getCurrentSemester(1,student.getSchoolId()), Semester.class);
			String acadyear = "";
			String semester = "";
			if(semesterObj!=null){
				acadyear=semesterObj.getAcadyear();
				semester=semesterObj.getSemester()+"";
				map.put("acadyear", acadyear);
			    map.put("semester", semester);
			}else{
				return errorFtl(map, "学年学期不存在，请联系管理员");
			}
			
			StuIntroduction item = stuIntroductionService.findObj(acadyear, semester, studentId);
			//判断当前学期成长手册是否可以查看
			boolean isBook = false;
			map.put("isFamily", isFamily);
			map.put("studentId", student.getId());
			map.put("stuName", student.getStudentName());
			map.put("unitId", student.getSchoolId());
			String fileUrl=stuIntroductionService.getFileURL();
			map.put("imgPath",(item==null?"":fileUrl+item.getFilePath()));
			//map.put("imgPath", item==null?"":item.getImgPath());
			
			map.put("isBook", isBook);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "/studevelop/mobile/index.ftl";
	}
}
