package net.zdsoft.studevelop.mobile.action;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

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
import net.zdsoft.studevelop.mobile.entity.StuOutside;
import net.zdsoft.studevelop.mobile.service.StuIntroductionService;
import net.zdsoft.studevelop.mobile.service.StuOutsideService;

@Controller
@RequestMapping("/mobile/open/studevelop/outside")
public class StuOutsideAction extends MobileAction {
	
	@Autowired
	private StuOutsideService stuOutsideService;
	@Autowired
	private StuIntroductionService stuIntroductionService;
	@Autowired
	private SemesterRemoteService semesterRemoteService;
	@RequestMapping("/index")
    @ControllerInfo(value = "校外表现")
	public String index(String acadyear, String semester, String studentId, Integer type, ModelMap map){
		List<String> acadyearList = SUtils.dt(semesterRemoteService.findAcadeyearList(), new TR<List<String>>() {});
		if(CollectionUtils.isEmpty(acadyearList)){
			return errorFtl(map, "找不到学年信息，请联系管理员");
		}
		map.put("acadyearList", acadyearList);
		List<StuOutside> list = stuOutsideService.findList(acadyear, semester, studentId, type);
		map.put("acadyear", acadyear);
		map.put("semester", semester);
		map.put("studentId", studentId);
		map.put("type", type);

		map.put("list", list);
		return "/studevelop/mobile/outsideSchool.ftl";
	}
	
	@RequestMapping("/add")
	@ControllerInfo(value = "校外表现--新增")
	public String add(String acadyear, String semester, String studentId, String id, Integer type, ModelMap map){
		List<String> acadyearList = SUtils.dt(semesterRemoteService.findAcadeyearList(), new TR<List<String>>() {});
		if(CollectionUtils.isEmpty(acadyearList)){
			return errorFtl(map, "找不到学年信息，请联系管理员");
		}
		map.put("acadyearList", acadyearList);
		StuOutside item = null;
		if(StringUtils.isNotBlank(id)){
			item = stuOutsideService.findObj(id);
		}
		
		if(item==null){
			item = new StuOutside();
			item.setAcadyear(acadyear);
			item.setSemester(semester);
			item.setStudentId(studentId);
			item.setType(type);
		}
		map.put("fileUrl", stuIntroductionService.getFileURL());
		map.put("item", item);
		map.put("acadyear",acadyear);
		map.put("semester",semester);
		map.put("studentId",studentId);
		map.put("type",type);
		map.put("id",id);

		return "/studevelop/mobile/outsideSchoolEdit.ftl";
	}
	
	@ResponseBody
	@RequestMapping("/save")
	@ControllerInfo(value = "校外表现--保存")
	public String save(StuOutside item, HttpServletRequest request){
		
		try {
			List<MultipartFile> files = StorageFileUtils.getFiles(request);
			
			if(StringUtils.isNotBlank(item.getId())){
				item.setModifyTime(new Date());
				stuOutsideService.update(item, files);
			}else{
				item.setCreationTime(new Date());
				stuOutsideService.save(item, files);
			}
		} catch (Exception e) {
			e.printStackTrace();
			error("操作异常！");
		}
		return success();
	}
}
