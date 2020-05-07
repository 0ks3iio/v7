package net.zdsoft.gkelective.data.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.remote.service.GradeRemoteService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.LoginInfo;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.gkelective.data.constant.GkElectveConstants;
import net.zdsoft.gkelective.data.dto.GkSubjectArrangeDto;
import net.zdsoft.gkelective.data.entity.GkSubjectArrange;
import net.zdsoft.gkelective.data.service.GkSubjectArrangeService;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/gkelective")
public class GkElectiveArrangeAction extends BaseAction{
	@Autowired
	private GradeRemoteService gradeService;
	@Autowired
	private GkSubjectArrangeService gkSubjectArrangeService;
	
	@RequestMapping("/arrange/index/page")
    @ControllerInfo(value = "7选3index")
    public String showIndex(ModelMap map, HttpSession httpSession) {
		return "/gkelective/arrange/arrangeIndex.ftl";
	}
	
	@RequestMapping("/arrange/list/page")
    @ControllerInfo(value = "显示list")
	public String showList(ModelMap map){
		LoginInfo info = getLoginInfo();
		List<GkSubjectArrangeDto> dtos = gkSubjectArrangeService.findByUnitIdIsUsing(info.getUnitId(),null);
		map.put("dtos", dtos);
		return "/gkelective/arrange/arrangeList.ftl";
	}
	
	@RequestMapping("/arrange/show/delete/page")
    @ControllerInfo(value = "新增排课")
	public String showDelete(HttpServletRequest request,ModelMap map){
		String deleteId = request.getParameter("deleteId");
		map.put("deleteId", deleteId);
		return "/gkelective/arrange/deleteArrange.ftl";
	}
	
	@RequestMapping("/arrange/edit/page")
    @ControllerInfo(value = "新增排课")
	public String addArrange(ModelMap map){
		//年级
		List<Grade> gradeList = SUtils.dt(gradeService.findBySchoolId(getLoginInfo().getUnitId(),new Integer[]{BaseConstants.SECTION_HIGH_SCHOOL}),new TR<List<Grade>>(){});
		map.put("gradeList", gradeList);
		return "/gkelective/arrange/arrangeEdit.ftl";
	}
	@ResponseBody
	@RequestMapping("/arrange/save")
    @ControllerInfo(value = "保存")
	public String saveArrange(GkSubjectArrange gkSubjectArrange,ModelMap map){
		try {
			LoginInfo info = getLoginInfo();
			if(gkSubjectArrange==null){
				return error("保存失败！");
			}
			//根据学年学期年级是不是已经存在
			GkSubjectArrangeDto dto = gkSubjectArrangeService.findByGradeId(gkSubjectArrange.getGradeId());
			if(dto.getGsaEnt()!=null){
				return error("该年级已经有对应选课项目！");
			}
			if(StringUtils.isBlank(gkSubjectArrange.getId())){
				gkSubjectArrange.setSubjectNum(3);//默认选3门
			}
			gkSubjectArrange.setUnitId(info.getUnitId());
			gkSubjectArrange.setIsUsing(GkElectveConstants.USE_FALSE);
			gkSubjectArrange.setIsLock(GkElectveConstants.USE_FALSE);
			gkSubjectArrange.setIsDeleted(GkElectveConstants.USE_FALSE);
			dto.setGsaEnt(gkSubjectArrange);
			gkSubjectArrangeService.saveDto(dto);
		} catch (Exception e) {
			e.printStackTrace();
			return returnError("保存失败！", e.getMessage());
		}

		return success("新增成功！");
	}
	
	@ResponseBody
	@RequestMapping("/arrange/delete")
	@ControllerInfo("删除")
	public String doDelete(String id,String verifyCode,String examId, ModelMap map, HttpSession httpSession) {
		try{
            String verifyCodeKey = "xgk_verify_code_key" + httpSession.getId();
            String sessionVerifyCode = StringUtils.trim(RedisUtils.get(verifyCodeKey));
            if (StringUtils.isBlank(sessionVerifyCode)) {
                return error("验证码已失效");
            }
            if (StringUtils.equalsIgnoreCase(sessionVerifyCode, verifyCode)) {
                RedisUtils.del(verifyCodeKey);
            } else {
                return error("验证码错误");
            }
			gkSubjectArrangeService.deletedById(id);
		}catch(Exception e){
			e.printStackTrace();
			return returnError();
		}
		return returnSuccess();
	}
	
}
