package net.zdsoft.teaeaxam.action;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import net.zdsoft.basedata.entity.Unit;
import net.zdsoft.basedata.remote.service.UnitRemoteService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.teaeaxam.entity.TeaexamSite;
import net.zdsoft.teaeaxam.service.TeaexamSiteService;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/teaexam")
public class TeaexamSiteAction extends BaseAction{
	@Autowired
	private TeaexamSiteService teaexamSiteService;
	@Autowired
	private UnitRemoteService unitRemoteService;

	@RequestMapping("/site/index/page")
	public String pageIndex(ModelMap map,HttpServletRequest request){
		Unit unit = SUtils.dc(unitRemoteService.findOneById(getLoginInfo().getUnitId()), Unit.class);
		List<Unit> units = SUtils.dt(unitRemoteService.findByUnionId(unit.getUnionCode(), Unit.UNIT_MARK_NORAML, Unit.UNIT_CLASS_SCHOOL), new TR<List<Unit>>(){});
		Map<String, String> uMap = new HashMap<String, String>();
		for(Unit u : units){
			uMap.put(u.getId(), u.getUnitName());
		}
		Pagination page = createPagination();
		List<TeaexamSite> siteList = teaexamSiteService.findByUnionCode(unit.getUnionCode(), page);
		for(TeaexamSite site : siteList ){
			site.setSiteName(uMap.get(site.getSchoolId()));
		}
		map.put("siteList", siteList);
		map.put("Pagination", page);
		sendPagination(request, map, page);
		return "/teaexam/site/siteHead.ftl";
	}
	
	@RequestMapping("/site/edit")
	public String siteEdit(String id, ModelMap map){
		Unit unit = SUtils.dc(unitRemoteService.findOneById(getLoginInfo().getUnitId()), Unit.class);
		List<Unit> units = SUtils.dt(unitRemoteService.findByUnionId(unit.getUnionCode(), Unit.UNIT_MARK_NORAML, Unit.UNIT_CLASS_SCHOOL), new TR<List<Unit>>(){});
		TeaexamSite site = new TeaexamSite();
		if(StringUtils.isNotBlank(id)){
			site = teaexamSiteService.findOne(id);
		}
		map.put("units", units);
		map.put("site", site);
		return "/teaexam/site/siteEdit.ftl";
	}
	
	@ResponseBody
	@RequestMapping("/site/save")
	public String siteSave(TeaexamSite teaexamSite, ModelMap map){
		try{
			if(StringUtils.isBlank(teaexamSite.getSchoolId())){
				return error("考点名称不能为空！");
			}
			List<TeaexamSite> siteList = teaexamSiteService.findAll();
			Set<String> schoolIdSet = new HashSet<String>();
			for(TeaexamSite site : siteList){
				if(!(StringUtils.isNotBlank(teaexamSite.getId()) && teaexamSite.getId().equals(site.getId()))){
					schoolIdSet.add(site.getSchoolId());
				}
			}
			String schoolId = teaexamSite.getSchoolId().split("-")[0];
			String unionCode = teaexamSite.getSchoolId().split("-")[1];
            if(schoolIdSet.contains(schoolId)){
            	return error("该考点已维护，请重新选择！");
			}
			teaexamSite.setSchoolId(schoolId);
			teaexamSite.setUnionCode(unionCode);
			if(StringUtils.isBlank(teaexamSite.getId())){
				teaexamSite.setId(UuidUtils.generateUuid());
				teaexamSite.setCreationTime(new Date());
				teaexamSite.setModifyTime(new Date());
			}else{
				teaexamSite.setModifyTime(new Date());
			}
			teaexamSiteService.save(teaexamSite);
		}catch(Exception e){
			e.printStackTrace();
			return error("操作失败！"+e.getMessage());
		}
		return returnSuccess();
	}
	
	@ResponseBody
	@RequestMapping("/site/delete")
	public String siteDelete(String id, ModelMap map){
		try{
			teaexamSiteService.delete(id);
		}catch(Exception e){
			e.printStackTrace();
			return error("操作失败！"+e.getMessage());
		}
		return returnSuccess();
	}
}
