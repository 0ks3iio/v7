package net.zdsoft.basedata.action;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import net.zdsoft.basedata.entity.Dept;
import net.zdsoft.basedata.entity.Unit;
import net.zdsoft.basedata.entity.User;
import net.zdsoft.basedata.service.DeptService;
import net.zdsoft.basedata.service.UnitService;
import net.zdsoft.basedata.service.UserService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.entity.Json;

@Controller
@RequestMapping("/basedata")
public class UnitAction extends BaseAction {

	@Autowired
	private UnitService unitService;

	@Autowired
	private DeptService deptService;

	@Autowired
	private UserService userService;

	@RequestMapping("/unit/page")
	public String unitIndex(ModelMap map) {
	    
		return "/basedata/unit/unitIndex.ftl";
	}

	@RequestMapping(value = "/unit/{id}/detail/page")
	public String unit(@PathVariable String id, ModelMap map) {
		User user = userService.findOne(id);
		map.put("user", user);
		return "/basedata/unit/unitDetail.ftl";
	}

	@ResponseBody
	@RequestMapping(value = "/unit/{id}/delete")
	public String doDeleteUnit(ModelMap map, HttpServletRequest request, HttpServletResponse response, @PathVariable String id) {
		List<Dept> depts = deptService.findByUnitId(id);
		if (CollectionUtils.isNotEmpty(depts)) {
			return error("此单位存在部门信息，不能删除！");
		}
		unitService.deleteAllByIds(id);
		return "删除成功！";
	}
	
	/**
	 * 获取下属单位
	 * @param unitId
	 * @return
	 */
	@RequestMapping("/unit/{pid}/underling")
	@ResponseBody
	public String getUnderlingUnits(final String unitId){
		Assert.notNull(unitId);
		List<Unit> units = unitService.findAll(new Specification<Unit>() {
			@Override
			public Predicate toPredicate(Root<Unit> root, CriteriaQuery<?> criteria,
					CriteriaBuilder builder) {
				Predicate p1 = builder.equal(root.get("isDeleted").as(String.class), "0");
				Predicate p2 = builder.equal(root.get("").as(String.class), unitId);
				criteria.where(new Predicate[]{p1,p2});
				return criteria.getRestriction();
			}
		});
		return Json.toJSONString(units);
	}
}
