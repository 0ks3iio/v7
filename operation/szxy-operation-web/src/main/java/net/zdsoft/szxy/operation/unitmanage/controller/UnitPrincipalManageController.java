package net.zdsoft.szxy.operation.unitmanage.controller;


import net.zdsoft.szxy.base.api.UnitRemoteService;
import net.zdsoft.szxy.base.entity.Unit;
import net.zdsoft.szxy.base.entity.UnitExtension;
import net.zdsoft.szxy.monitor.Record;
import net.zdsoft.szxy.monitor.RecordType;
import net.zdsoft.szxy.operation.unitmanage.UnitPrincipalOperateCode;
import net.zdsoft.szxy.operation.unitmanage.entity.OpUnitPrincipal;
import net.zdsoft.szxy.operation.unitmanage.service.OpUnitPrincipalService;
import net.zdsoft.szxy.operation.unitmanage.service.OpUnitService;
import net.zdsoft.szxy.operation.unitmanage.vo.UnitPrincipalEditVo;
import net.zdsoft.szxy.plugin.mvc.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Date;
import java.util.List;

/**
 *  单位联系人管理
 * @author zhanWenze
 * @since 2019年4月3日
 */
@RestController
@RequestMapping("/operation/unitPrincipal/manage")
public class UnitPrincipalManageController {

    @Autowired
    private OpUnitPrincipalService opUnitPrincipalService;

    @Resource
    private UnitRemoteService unitRemoteService;

    @Resource
    private OpUnitService opUnitService;

    private Logger logger = LoggerFactory.getLogger(UnitPrincipalManagePageController.class);


    @Secured(UnitPrincipalOperateCode.UNIT_PRINCIPALS_EDIT)
    @Record(type = RecordType.URL)
    @GetMapping(
            value = "unitPrincipalAccount"
    )
    public Response doEdit(@RequestParam("unitId") String unitId) {
        Unit unit = unitRemoteService.getUnitById(unitId);
        if (unit == null) {
            return Response.error().message("用户不存在").build();
        }
        UnitPrincipalEditVo editVo = new UnitPrincipalEditVo();
        editVo.setId(unit.getId());
        editVo.setUnitName(unit.getUnitName());

        Date unitExpireTime = null;
        List<OpUnitPrincipal> unitPrincipals = null;
        UnitExtension unitExtension = null;
        try {
            unitExtension = opUnitService.findByUnitId(unitId);
            if (unitExtension.getExpireTimeType() != null) {
                unitExpireTime = unitExtension.getExpireTime();
            }
            // 获取单位联系人List
            unitPrincipals = opUnitPrincipalService.findUnitPrincipalsByUnitId(unitId);
        } catch (Exception e) {
            logger.error("获取单位联系人失败");
            return Response.error().message("获取单位联系人信息失败").build();
        }
        editVo.setExpireTime(unitExpireTime);
        editVo.setUnitPrincipals(unitPrincipals);
        return Response.ok().data("unitPrincipalAccount", editVo).build();
    }
    /**
     *  编辑单位联系人
     * @return
     */
    @Secured(UnitPrincipalOperateCode.UNIT_PRINCIPALS_EDIT)
    @Record(type = RecordType.URL)
    @PutMapping("/unitPrincipalAccount")
    public Response doUpdateUnitPrincipalAccount(@Valid @RequestBody UnitPrincipalEditVo editVo, BindingResult errors) {
        if (errors.hasFieldErrors()) {
            return Response.error().message(errors.getFieldError().getDefaultMessage()).build();
        }
        try {
            opUnitPrincipalService.updateUnitPrincipals(editVo);
            return Response.ok().message("更新成功").build();
        }catch (Exception e) {
            logger.error("单位联系人信息更新失败");
            return Response.ok().message("更新失败").build();
        }
    }
}
