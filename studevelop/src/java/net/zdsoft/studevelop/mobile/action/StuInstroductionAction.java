package net.zdsoft.studevelop.mobile.action;

import java.util.Date;
import javax.servlet.http.HttpServletRequest;
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
import net.zdsoft.studevelop.mobile.entity.StuIntroduction;
import net.zdsoft.studevelop.mobile.service.StuIntroductionService;

@Controller
@RequestMapping("/mobile/open/studevelop/instroduction")
public class StuInstroductionAction extends MobileAction {
	
	@Autowired
	private StuIntroductionService stuIntroductionService;

	@RequestMapping("/index")
    @ControllerInfo(value = "自我介绍")
	public String showSelfIntroduction(String acadyear, String semester, String studentId, String stuName, ModelMap map){
		
		StuIntroduction item = stuIntroductionService.findObj(acadyear, semester, studentId);
		if(item==null){
			item = new StuIntroduction();
			item.setAcadyear(acadyear);
			item.setSemester(semester);
			item.setStudentId(studentId);
			item.setHasRelease(0);
		}
		map.put("fileUrl", stuIntroductionService.getFileURL());
		map.put("stuName", stuName);
		map.put("item", item);
		
		return "/studevelop/mobile/selfInstroduction.ftl";
	}
	
	@ResponseBody
	@RequestMapping("/save")
	@ControllerInfo(value = "自我介绍")
	public String save(StuIntroduction item, HttpServletRequest request){
		try {
			MultipartFile file = StorageFileUtils.getFile(request);
			
			if(StringUtils.isEmpty(item.getId())) {
				StuIntroduction item2 = stuIntroductionService.findObj(item.getAcadyear(), item.getSemester(), item.getStudentId());
				if(item2 != null) {
					item.setId(item2.getId());
				}
			}
			if(StringUtils.isNotBlank(item.getId())){
				item.setModifyTime(new Date());
				stuIntroductionService.update(item, file);
			}else{
				item.setId(UuidUtils.generateUuid());
				item.setCreationTime(new Date());
				stuIntroductionService.save(item, file);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return success("操作成功");
	}
}
