package net.zdsoft.newgkelective.data.action;

import java.util.Arrays;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.newgkelective.data.entity.NewGkSubjectGroupColor;
import net.zdsoft.newgkelective.data.service.NewGkSubjectGroupColorService;

@Controller
@RequestMapping("/newgkelective/subjectGroupColor")
public class NewGkSubjectGroupColorAction extends BaseAction {
	@Autowired
	private NewGkSubjectGroupColorService newGkSubjectGroupColorService;
	
	@RequestMapping("/changeColor")
	@ResponseBody
	public String saveGroupColor(@RequestParam(name="subjectIds[]") String[] subjectIds, 
			NewGkSubjectGroupColor groupColor) {
		if(StringUtils.isBlank(groupColor.getColor())) {
			return error("请指定要设置的颜色！");
		}
		
		Date now = new Date();
		if(StringUtils.isNotBlank(groupColor.getId())) {
			NewGkSubjectGroupColor gpColor = newGkSubjectGroupColorService.findOne(groupColor.getId());
			gpColor.setColor(groupColor.getColor());
			gpColor.setModifyTime(now);
			groupColor = gpColor;
		}else {
			if(StringUtils.isBlank(groupColor.getGroupType())) {
				return error("请指定要设置的组合类型！");
			}
			if(subjectIds == null || subjectIds.length == 0) {
				return error("请指定要设置的科目组合！");
			}
			
			groupColor.setId(UuidUtils.generateUuid());
			groupColor.setUnitId(getLoginInfo().getUnitId());
			Arrays.sort(subjectIds);
			groupColor.setSubjectGroup(String.join(",", subjectIds));
			groupColor.setCreationTime(now);
			groupColor.setModifyTime(now);
		}
		
		try {
			newGkSubjectGroupColorService.save(groupColor);
		} catch (Exception e) {
			e.printStackTrace();
			return error("保存失败，"+e.getMessage());
		}
		
		return returnSuccess();
	}
}
