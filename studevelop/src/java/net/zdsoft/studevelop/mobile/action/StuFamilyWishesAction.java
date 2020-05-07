package net.zdsoft.studevelop.mobile.action;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.remote.service.SemesterRemoteService;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.SUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import net.zdsoft.framework.action.MobileAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.utils.StorageFileUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.studevelop.mobile.entity.StuFamilyWishes;
import net.zdsoft.studevelop.mobile.service.StuFamilyWishesService;
import net.zdsoft.studevelop.mobile.service.StuIntroductionService;

@Controller
@RequestMapping("/mobile/open/studevelop/family")
public class StuFamilyWishesAction extends MobileAction{
	
	@Autowired
	private StuFamilyWishesService stuFamilyWishesService;
	@Autowired
	private StuIntroductionService stuIntroductionService;
	@Autowired
	private SemesterRemoteService semesterRemoteService;

	@RequestMapping("/index")
    @ControllerInfo(value = "幸福的一家")
	public String showMyFamily(String acadyear, String semester, String studentId, String stuName, ModelMap map){
		List<String> acadyearList = SUtils.dt(semesterRemoteService.findAcadeyearList(), new TR<List<String>>() {});
		if(CollectionUtils.isEmpty(acadyearList)){
			return errorFtl(map, "找不到学年信息，请联系管理员");
		}
		map.put("acadyearList", acadyearList);

		StuFamilyWishes item = stuFamilyWishesService.findObj(acadyear, semester, studentId);
		if(item==null){
			item = new StuFamilyWishes();
			item.setAcadyear(acadyear);
			item.setSemester(semester);
			item.setStudentId(studentId);
		}
		map.put("fileUrl", stuIntroductionService.getFileURL());
		map.put("stuName", stuName);
		map.put("item", item);
		map.put("acadyear",acadyear);
		map.put("semester",semester);
		map.put("studentId",studentId);
		return "/studevelop/mobile/myFamily.ftl";
	}
	
	@ResponseBody
	@RequestMapping("/save")
	@ControllerInfo(value = "幸福的一家--保存")
	public String save(StuFamilyWishes item, HttpServletRequest request){
		try {
			MultipartFile file = StorageFileUtils.getFile(request);
			if (StringUtils.isEmpty(item.getId())) {
				StuFamilyWishes item2 = stuFamilyWishesService.findObj(item.getAcadyear(), item.getSemester(),
						item.getStudentId());
				if (item2 != null) {
					item.setId(item2.getId());
				} 
			}
			if(StringUtils.isNotBlank(item.getId())){
				item.setModifyTime(new Date());
				stuFamilyWishesService.update(item, file);
			}else{
				item.setId(UuidUtils.generateUuid());
				item.setCreationTime(new Date());
				stuFamilyWishesService.save(item, file);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return success("操作成功");
	}
	
}
