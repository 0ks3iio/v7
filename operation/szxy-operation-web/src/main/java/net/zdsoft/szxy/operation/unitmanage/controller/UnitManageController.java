package net.zdsoft.szxy.operation.unitmanage.controller;

import net.zdsoft.szxy.base.api.UnitRemoteService;
import net.zdsoft.szxy.base.entity.UnitExtension;
import net.zdsoft.szxy.base.enu.UnitExtensionExpireType;
import net.zdsoft.szxy.base.enu.UnitExtensionNature;
import net.zdsoft.szxy.base.enu.UnitExtensionState;
import net.zdsoft.szxy.monitor.Record;
import net.zdsoft.szxy.monitor.RecordType;
import net.zdsoft.szxy.operation.security.CurrentUser;
import net.zdsoft.szxy.operation.unitmanage.Router;
import net.zdsoft.szxy.operation.unitmanage.UnitOperateCode;
import net.zdsoft.szxy.operation.unitmanage.service.IllegalRenewalTimeException;
import net.zdsoft.szxy.operation.unitmanage.service.NoUnitExtensionException;
import net.zdsoft.szxy.operation.unitmanage.service.OpUnitService;
import net.zdsoft.szxy.plugin.mvc.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import javax.annotation.Resource;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * @author yangkj
 * @since 2019/1/16
 */
@RestController
@RequestMapping(
        value = Router.UNIT_MANAGE
)
//@Secured(SecurityUser.LOGINED)
public class UnitManageController {

    @Resource
    private OpUnitService opUnitService;
    @Resource
    private UnitRemoteService unitRemoteService;

    private Logger logger = LoggerFactory.getLogger(UnitManageController.class);

    /**
     * 删除
     */
    @Secured(UnitOperateCode.UNIT_DELETE)
    @Record(type = RecordType.URL)
    @RequestMapping(value = "/{unitId}",method = RequestMethod.POST)
    public Response doDelete(@PathVariable("unitId") String unitId) {
        Boolean flag = unitRemoteService.existsUnderlingUnits(unitId);
        if (flag.equals(false)) {
            opUnitService.deleteUnit(unitId);
            return Response.ok().message("单位删除成功").build();
        }
        return Response.error().message("无法删除拥有下属单位的父级单位").build();
    }

    /**
     * 单位状态变更
     */
    @Secured(UnitOperateCode.UNIT_STATE_CHANGE)
    @Record(type = RecordType.URL)
    @GetMapping(value = "/state/usingState")
    public Response doChangeState(@RequestParam("unitId") String unitId,
                                   @RequestParam("usingState") Integer usingState,
                                   @CurrentUser("id") String operatorId) {
        String message = null;
        try {
            if (UnitExtensionState.DISABLE.equals(usingState)) {
                opUnitService.stopUnit(unitId,operatorId);
                message = "停用成功";
            } else if (UnitExtensionState.NORMAL.equals(usingState)) {
                opUnitService.recoverUnit(unitId,operatorId);
                message = "恢复成功";
            } else if (UnitExtensionState.EXPIRE.equals(usingState)) {
                return Response.error().message("不合法的状态值【%s】", new String[]{usingState.toString()}).build();
            }
            return Response.ok().message(message).build();
        } catch (NoUnitExtensionException e) {
            logger.error("单位[{}]状态变更为[{}]失败,没有该单位的扩展信息,操作人员[{}]", unitId, usingState, operatorId);
            return Response.error().message("没有该单位的信息，无法操作").build();
        }

    }

    /**
     * 单位试用转正式
     */
    @Secured(UnitOperateCode.UNIT_NATURE_CHANGE)
    @Record(type = RecordType.URL)
    @GetMapping(value = "/nature/official")
    public Response doChangeNature(@RequestParam("unitId") String unitId,
                                    @CurrentUser("id") String operatorId) {
        try {
            opUnitService.turnToOfficial(unitId,operatorId);
            return Response.ok().message("单位转正式成功").build();
        } catch (NoUnitExtensionException e) {
            logger.error("单位[{}]转正式失败,没有该单位的扩展信息,操作人员[{}]", unitId, operatorId);
            return Response.error().message("单位转正式失败").build();
        }
    }

    /**
     * 单位续期
     */
    @Secured(UnitOperateCode.UNIT_RENEWAL)
    @Record(type = RecordType.URL)
    @RequestMapping(value = "expire",method = RequestMethod.POST)
    public Response doChangeExpireTime(@RequestParam(value="expireTime",required = false) Date expireTime,
                                        @RequestParam("expireTimeType") Integer expireTimeType,
                                        @RequestParam("unitId") String unitId,
                                        @CurrentUser("id") String operatorId) {
        try {
            Integer usingNature = opUnitService.findByUnitId(unitId).getUsingNature();
            if (UnitExtensionExpireType.PERMANENT.equals(expireTimeType)&&usingNature.equals(UnitExtensionNature.TRIAL)){
                return Response.error().message("试用单位不能续期为永久").build();
            }else if (UnitExtensionExpireType.PERMANENT.equals(expireTimeType)) {
                opUnitService.updateExpireTimeToPermanent(unitId,operatorId);
                return Response.ok().message("续期成功").build();
            } else if (UnitExtensionExpireType.SPECIFY_TIME.equals(expireTimeType)) {
                opUnitService.renewal(expireTime, unitId, operatorId);
                return Response.ok().message("续期成功").build();
            }
        } catch (NoUnitExtensionException e) {
            logger.error("单位[{}]续期失败,没有该单位的扩展信息,操作人员[{}]", unitId, operatorId);
            return Response.error().message("没有该单位的续期信息，续期失败").build();
        } catch (IllegalRenewalTimeException e) {
            logger.error("单位[{}]续期失败,续期时间[{}]不合法,操作人员[{}]", unitId, expireTime, operatorId);
            return Response.error().message(e.getMessage()).build();
        }
        return Response.error().message("请输入正确的日期").build();
    }

    /**
     * 服务续期
     */
    @Record(type = RecordType.URL)
    @RequestMapping(value = "service",method = RequestMethod.POST)
    public Response doChangeServiceExpireTime(@RequestParam("serviceExpireTime") Date serviceExpireTime,
                                               @RequestParam("unitId") String unitId,
                                               @CurrentUser("id") String operatorId) {

        try {
            opUnitService.renewalServiceTime(serviceExpireTime, unitId);
            return Response.ok().message("续期成功").build();
        } catch (NoUnitExtensionException e) {
            logger.error("单位[{}]续期[{}]失败,没有该单位的扩展信息,操作人员[{}]", unitId, serviceExpireTime, operatorId);
            return Response.error().message("没有该单位的续期信息，续期失败").build();
        } catch (IllegalRenewalTimeException e) {
            logger.error("单位[{}]续期失败,续期时间[{}]不合法,操作人员[{}]", unitId, serviceExpireTime, operatorId);
            return Response.error().message(e.getMessage()).build();
        }
    }

    /**
     * 查看单位扩展状态
     */
    @Record(type = RecordType.URL)
    @GetMapping(value = "/check/state")
    public Response checkState(@RequestParam("unitId") String unitId){
        try {
            UnitExtension unitExtension = opUnitService.findByUnitId(unitId);
            return Response.ok().data("usingState",unitExtension.getUsingState())
                    .data("usingNature",unitExtension.getUsingNature()).message("查询成功").build();
        }catch (NoUnitExtensionException e){
            return Response.error().message(e.getMessage()).build();
        }
    }

    @InitBinder
    public void initBinder(WebDataBinder binder, WebRequest request) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

}
