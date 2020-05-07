package net.zdsoft.studevelop.data.action;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.basedata.entity.School;
import net.zdsoft.basedata.remote.service.SchoolRemoteService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.config.Evn;
import net.zdsoft.framework.entity.Constant;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.NameFactory;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.StorageFileUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.studevelop.data.constant.StuDevelopConstant;
import net.zdsoft.studevelop.data.entity.StudevelopAttachment;
import net.zdsoft.studevelop.data.entity.StudevelopMasterWords;
import net.zdsoft.studevelop.data.service.StudevelopAttachmentService;
import net.zdsoft.studevelop.data.service.StudevelopMasterWordsService;
import net.zdsoft.system.entity.mcode.McodeDetail;
import net.zdsoft.system.remote.service.McodeRemoteService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

/**
 * 
 * @author weixh
 * @since 2017-7-24 下午2:22:45
 */
@Controller
@RequestMapping("/studevelop/words")
public class StuDevelopMasterWordsAction extends CommonAuthAction {
	@Autowired
	private StudevelopMasterWordsService studevelopMasterWordsService;
	@Autowired
	private SchoolRemoteService schoolRemoteService;
	@Autowired
	private McodeRemoteService mcodeRemoteService;
	@Autowired
	private StudevelopAttachmentService studevelopAttachmentService;
	
	@RequestMapping("/index/page")
	@ControllerInfo(value="校长寄语index")
	public String showIndex(String nowSection, ModelMap map){
		if(!isAdmin(StuDevelopConstant.PERMISSION_TYPE_GROWTH)){
			return promptFlt(map , "不是成长手册管理员不能维护和查询校长寄语内容");
		}
		String unitId = getLoginInfo().getUnitId();
		School sch = SUtils.dc(schoolRemoteService.findOneById(unitId), School.class);
		boolean hasPic = studevelopAttachmentService.findAttachmentNumByObjId(unitId, StuDevelopConstant.OBJTYPE_MASTER_PIC)>0;
		map.put("hasPic", hasPic);
		if(StringUtils.isBlank(sch.getSections())){
			map.put("msg", "学校没有有效的学校类别信息！");
		} else {
			if(StringUtils.isNotBlank(nowSection)){
				map.put("nowSection", nowSection);
			}
			Map<String, McodeDetail> mm = SUtils.dt(mcodeRemoteService.findMapByMcodeId("DM-RKXD"), new TR<Map<String, McodeDetail>>(){});
			String[] secs = StringUtils.split(sch.getSections(), ",");
			List<McodeDetail> mds = new ArrayList<McodeDetail>();
			Map<String, List<String[]>> secGrades = new HashMap<String, List<String[]>>();
			for(String sec : secs){
				int years = 0;
				int secInt = NumberUtils.toInt(sec);
				if(secInt==BaseConstants.SECTION_PRIMARY){
					years = sch.getGradeYear()==null?0:sch.getGradeYear();
				} else if(secInt == BaseConstants.SECTION_JUNIOR){
					years = sch.getJuniorYear();
				} else if(secInt == BaseConstants.SECTION_HIGH_SCHOOL){
					years = sch.getSeniorYear();
				}
				if (years > 0) {
					List<String[]> grades = new ArrayList<String[]>();
					for (int i = 1; i <= years; i++) {
						grades.add(new String[]{sec+i, NameFactory.sectionMap.get(NumberUtils.toInt(sec))+NameFactory.yearMap.get(i)});
					}
					secGrades.put(sec, grades);
					McodeDetail md = mm.get(sec);
					mds.add(md);
				}
			}
			map.put("sections", mds);
			map.put("gradesMap", secGrades);
			List<StudevelopMasterWords> wordsList = studevelopMasterWordsService.getStudevelopMasterWordsByUnitId(unitId);
			StudevelopMasterWords  words=null;
			if(CollectionUtils.isNotEmpty(wordsList)){
				words = wordsList.get(0);
			}
			if(words == null){
				words = new StudevelopMasterWords();
				words.setUnitId(unitId);
			}
			map.put("words", words);
			
			map.put("objId", unitId);
			map.put("objType", StuDevelopConstant.OBJTYPE_MASTER_PIC);
		}
		map.put("randomNum", System.currentTimeMillis());
		return "/studevelop/myschool/masterWords.ftl";
	}
	
	@ResponseBody
	@RequestMapping(value="/save")
	@ControllerInfo(value="保存校长寄语")
	public String saveWords(StudevelopMasterWords words){
		try {
			words.setModifyTime(new Date());
			if (StringUtils.isBlank(words.getId())) {
				words.setId(UuidUtils.generateUuid());
				words.setCreationTime(words.getModifyTime());
			}
			studevelopMasterWordsService.save(words);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return returnError();
		}
		return returnSuccess();
	}
	
	@ResponseBody
	@RequestMapping(value="/masterpic/show")
	@ControllerInfo(value="显示校长照片")
	public String showPic(String showOrigin, HttpServletResponse response){
		try {
			List<StudevelopAttachment> atts = studevelopAttachmentService.getAttachmentByObjId(getLoginInfo().getUnitId(), StuDevelopConstant.OBJTYPE_MASTER_PIC);
			File pic;
			if(CollectionUtils.isNotEmpty(atts)){
				StudevelopAttachment att = atts.get(0);
				if (Constant.IS_TRUE_Str.equals(showOrigin)) {
					pic = att.getOriginFile();// 原图
				} else {
					pic = att.getSmallFile();// 缩略图
				}
			} else {
				String defalutPath = Evn.getRequest().getRealPath("/static/jscrop/images/portrait_big.png");
				pic = new File(defalutPath);
			}
			if(pic != null && pic.exists()){
				response.getOutputStream().write(FileUtils.readFileToByteArray(pic));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@RequestMapping(value="/masterpic/save")
	@ControllerInfo(value="保存校长照片")
	public String savePic(ModelMap map, HttpServletRequest request){
		try {
			List<StudevelopAttachment> atts = studevelopAttachmentService.getAttachmentByObjId(getLoginInfo().getUnitId(), StuDevelopConstant.OBJTYPE_MASTER_PIC);
			StudevelopAttachment att;
			if(CollectionUtils.isNotEmpty(atts)){
				att = atts.get(0);
			} else {
				att = new StudevelopAttachment();
			}
			MultipartFile file = StorageFileUtils.getFile(request);
			att.setObjecttype(StuDevelopConstant.OBJTYPE_MASTER_PIC);
			att.setObjId(getLoginInfo().getUnitId());
			att.setUnitId(att.getObjId());
			studevelopAttachmentService.saveAttachment(att, file);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return "";
	}
	
	@ResponseBody
	@RequestMapping(value="/masterpic/del")
	@ControllerInfo(value="删除校长照片")
	public String delPic(ModelMap map, HttpServletRequest request){
		try {
			studevelopAttachmentService.deleteByObjIds(new String[]{getLoginInfo().getUnitId()});
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return error("删除失败！");
		}
		return success("删除成功！");
	}

}
