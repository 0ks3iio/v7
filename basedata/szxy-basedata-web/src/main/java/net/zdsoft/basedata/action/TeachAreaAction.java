package net.zdsoft.basedata.action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;

import net.zdsoft.basedata.dto.TeachAreaDto;
import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.TeachArea;
import net.zdsoft.basedata.service.ClassService;
import net.zdsoft.basedata.service.TeachAreaService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.LoginInfo;
import net.zdsoft.framework.utils.ColumnInfoUtils;
import net.zdsoft.framework.utils.EntityUtils;

@Controller
@RequestMapping("/basedata")
public class TeachAreaAction extends BaseAction{
	
	@Autowired
	private TeachAreaService teachAreaService;
	@Autowired
	private ClassService classService;
	
	@RequestMapping("/teachArea/unit/{unitId}/list/page")
	@ControllerInfo("进入学校{unitId}下的校区页面")
	public String showTeachArea(@PathVariable String unitId, ModelMap map, HttpSession httpSession) {
        map.put("unitId", unitId);
		return "/basedata/teachArea/teachAreaList.ftl";
	}
	
	@ResponseBody
	@RequestMapping("/teachArea/unit/{unitId}/list")
	@ControllerInfo("读取学校{unitId}下的校区数据")
	public String showTeachAreaByUnitId(@PathVariable String unitId, HttpServletRequest req, HttpSession httpSession) {
		List<TeachArea> findByUnitId = teachAreaService.findByUnitId(unitId);
		List<TeachAreaDto> dtos = new ArrayList<TeachAreaDto>();
		TeachAreaDto dto;
		for (TeachArea item : findByUnitId) {
			dto = new TeachAreaDto();
			dto.setTeachArea(item);
			dtos.add(dto);
		}
		return JSON.toJSONString(dtos);
	}
	
	@RequestMapping("/teachArea/edit/page")
    @ControllerInfo(value = "新增修改校区")
    public String showTeachAreaAdd(String id,ModelMap map, HttpSession httpSession) {
		TeachArea teachArea = new TeachArea();
        if(StringUtils.isNotBlank(id)){
        	teachArea = teachAreaService.findOne(id);
//        	List<Student> findByClassId = studentService.findByClassId(id);
//        	map.put("studentList", findByClassId);
        }else{
        }
        TeachAreaDto dto = new TeachAreaDto();
        LoginInfo info = getLoginInfo(httpSession);
        String unitId = info.getUnitId();
        dto.setTeachArea(teachArea);
        teachArea.setUnitId(unitId);
        map.put("dto", dto);
        map.put("fields", ColumnInfoUtils.getEntityFiledNames(TeachArea.class));
		map.put("columnInfo", ColumnInfoUtils.getColumnInfos(TeachArea.class));
        return "/basedata/teachArea/teachAreaAdd.ftl";
    }
	
	@ResponseBody
    @RequestMapping("/teachArea/save")
    @ControllerInfo(value = "保存校区")
    public String doSaveTeachArea(@RequestBody TeachAreaDto dto) {
		try{
			TeachArea teachArea = dto.getTeachArea();
			if(StringUtils.isBlank(teachArea.getId())){
			}else{
				TeachArea teachAreaOld = teachAreaService.findOne(teachArea.getId());
				EntityUtils.copyProperties(teachArea, teachAreaOld, true);
				teachArea = teachAreaOld;
			}
			teachAreaService.saveAllEntitys(teachArea);
		}catch(Exception e){
			e.printStackTrace();
			return returnError();
		}
		return returnSuccess();
    }
	
	@ResponseBody
    @RequestMapping("/teachArea/{id}/delete")
    @ControllerInfo(value = "删除校区:{id}")
    public String doDeleteTeachArea(@PathVariable String id, HttpSession httpSession) {
		try{
			List<Clazz> findByTeachAreaIdIn = classService.findByTeachAreaIdIn(id);
			if(CollectionUtils.isNotEmpty(findByTeachAreaIdIn)){
				return error("该班级下存在班级，不能删除！");
			}
	        teachAreaService.deleteAllIsDeleted(id);
		}catch(Exception e){
			e.printStackTrace();
			return returnError();
		}
		return returnSuccess();
    }

}
