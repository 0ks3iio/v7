package net.zdsoft.studevelop.mobile.action;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.zdsoft.basedata.remote.service.SemesterRemoteService;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.SUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import net.zdsoft.framework.action.MobileAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.utils.StorageFileUtils;
import net.zdsoft.studevelop.data.service.StudevelopAttachmentService;
import net.zdsoft.studevelop.mobile.entity.StuHonor;
import net.zdsoft.studevelop.mobile.service.StuHonorService;
import net.zdsoft.studevelop.mobile.service.StuIntroductionService;

@Controller
@RequestMapping("/mobile/open/studevelop/honor")
public class StuHonorAction extends MobileAction {

	@Autowired
	private StuHonorService stuHonorService;
	@Autowired
	private StudevelopAttachmentService studevelopAttachmentService;
	@Autowired
	private StuIntroductionService stuIntroductionService;
	@Autowired
	private SemesterRemoteService semesterRemoteService;
	@RequestMapping("/index")
    @ControllerInfo(value = "我的荣誉")
	public String showMyHonor(String acadyear, String semester, String studentId, ModelMap map){
		List<String> acadyearList = SUtils.dt(semesterRemoteService.findAcadeyearList(), new TR<List<String>>() {});
		if(CollectionUtils.isEmpty(acadyearList)){
			return errorFtl(map, "找不到学年信息，请联系管理员");
		}
		map.put("acadyearList", acadyearList);
		StuHonor item = stuHonorService.findObj(acadyear, semester, studentId);
		
		map.put("acadyear", acadyear);
		map.put("semester", semester);
		map.put("studentId", studentId);
		map.put("item", item);
		map.put("fileUrl", stuIntroductionService.getFileURL());
		
		return "/studevelop/mobile/myHonor.ftl";
	}
	
	@RequestMapping("/upload")
	@ControllerInfo(value = "我的荣誉--上传图片")
	@ResponseBody
	public String save(String acadyear, String semester, String studentId, HttpServletRequest request) throws IOException{
		try {
			MultipartFile file = StorageFileUtils.getFile(request);
			//
			StuHonor item = stuHonorService.findObj(acadyear, semester, studentId);
			if(item==null){
				item = new StuHonor();
				item.setAcadyear(acadyear);
				item.setSemester(semester);
				item.setStudentId(studentId);
				stuHonorService.save(item, file);
			}else{
				stuHonorService.update(item, file);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return error("上传失败，请重新上传");
		}
		
		return success("上传成功");
	}
	
	@RequestMapping("delete")
	@ControllerInfo(value = "我的荣誉--删除图片")
	@ResponseBody
	public String delete(String attId, HttpServletRequest request) throws IOException{
		try {
			studevelopAttachmentService.delete(attId);
		} catch (Exception e) {
			e.printStackTrace();
			return error("删除失败");
		}
		return success("删除成功");
	}
}
