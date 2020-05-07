package net.zdsoft.szxy.operation.unitmanage.controller;

import net.zdsoft.szxy.monitor.LogType;
import net.zdsoft.szxy.monitor.Record;
import net.zdsoft.szxy.monitor.RecordType;
import net.zdsoft.szxy.operation.security.CurrentUser;
import net.zdsoft.szxy.operation.security.UserDataRegionHolder;
import net.zdsoft.szxy.operation.unitmanage.UnitOperateCode;
import net.zdsoft.szxy.operation.unitmanage.dto.OpUnitPrincipalQueryDto;
import net.zdsoft.szxy.operation.unitmanage.service.OpUnitPrincipalService;
import net.zdsoft.szxy.operation.unitmanage.service.OpUnitService;
import net.zdsoft.szxy.operation.unitmanage.vo.UnitPrincipalTypeVo;
import net.zdsoft.szxy.operation.unitmanage.vo.UnitPrincipalVo;
import net.zdsoft.szxy.operation.unitmanage.vo.UnitStarLevelVo;
import net.zdsoft.szxy.plugin.mvc.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Iterator;
import java.util.Set;

/**
 * @author zhanWenze
 * @since 2019年4月8日
 */
@Controller
@RequestMapping("/operation/unitPrincipal")
public class UnitPrincipalManagePageController {

    @Autowired
    private OpUnitPrincipalService opUnitPrincipalService;

    @Autowired
    private OpUnitService opUnitService;

    private Logger logger = LoggerFactory.getLogger(UnitPrincipalManagePageController.class);

    @GetMapping(
            value = "/index"
    )
    @Record(type = RecordType.MVC, event = LogType.EVENT)
    public String execute(Model model) {
        model.addAttribute("unitStarLevelList", UnitStarLevelVo.getUnitLevels());
        return "/unitmanage/unitPrincipalManager-index.ftl";
    }

    /**
     *  根据条件展示单位负责人列表
     * @param parentId
     * @param opUnitPrincipalQueryDto
     * @param pageable
     * @param model
     * @return
     */

    @GetMapping(
            value = "/unitPrincipalList"
    )
    public String unitPrincipalListPage(@RequestParam(value = "parentId", required = false) String parentId,
                                        OpUnitPrincipalQueryDto opUnitPrincipalQueryDto,
                                        Pageable pageable, Model model) {
        try {
            Set<String> regionsSet = UserDataRegionHolder.getRegions();
            Page<UnitPrincipalVo> unitPrincipalVos = opUnitPrincipalService.findPageByParentId(regionsSet,parentId, opUnitPrincipalQueryDto, pageable);
            model.addAttribute("pages", unitPrincipalVos);
            model.addAttribute("unitStarLevelList", UnitStarLevelVo.getUnitLevels());
            model.addAttribute("unitPrincipalTypeList", UnitPrincipalTypeVo.getUnitPrincipalTypeVos());
            return "/unitmanage/unitPrincipalManager-unitPrincipalList.ftl";
        }catch (Exception e) {
            logger.error("分页查询失败");
            return "/unitmanage/unitPrincipalManager-index.ftl";
        }
    }
    @Secured(UnitOperateCode.UNIT_EDIT)
    @Record(type = RecordType.URL)
    @RequestMapping(
            value = "/updateUnitStarLevel"
    )
    @ResponseBody
    public Response doUpdateUnitStarLevel(String unitId, Integer starLevel) {
        try {
            opUnitService.updateUnitStarLevel(unitId, starLevel);
            return Response.ok().message("更新成功").build();
        }catch(Exception e) {
            logger.error("单位联系人列表中单位星级更新失败");
            return Response.error().message("更新失败").build();
        }
    }
}
