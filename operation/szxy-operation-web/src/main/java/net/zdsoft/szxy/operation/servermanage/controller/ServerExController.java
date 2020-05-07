package net.zdsoft.szxy.operation.servermanage.controller;

import com.alibaba.fastjson.JSON;
import net.zdsoft.szxy.base.entity.ServerExtension;
import net.zdsoft.szxy.base.enu.UnitExtensionState;
import net.zdsoft.szxy.operation.security.CurrentUser;
import net.zdsoft.szxy.operation.servermanage.Router;
import net.zdsoft.szxy.operation.servermanage.dao.OpServerExtensionDao;
import net.zdsoft.szxy.operation.servermanage.dto.ServerExtensionDto;
import net.zdsoft.szxy.operation.servermanage.service.NoServerExtensionException;
import net.zdsoft.szxy.operation.servermanage.service.OpServerExService;
import net.zdsoft.szxy.operation.unitmanage.dto.EnableServerDto;
import net.zdsoft.szxy.operation.unitmanage.service.IllegalRenewalTimeException;
import net.zdsoft.szxy.operation.unitmanage.service.NoUnitExtensionException;
import net.zdsoft.szxy.operation.unitmanage.service.OpUnitService;
import net.zdsoft.szxy.plugin.mvc.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


/**
 * @author 张帆远
 * @since 2019/1/17  上午11:18
 */
@RestController
@RequestMapping(value = Router.SERVER_MANAGE)
public class ServerExController {

    @Resource
    private OpServerExService opServerExService;

    @Resource
    private OpServerExtensionDao opServerExtensionDao;

    @Autowired
    private OpUnitService opUnitService;

    private Logger logger = LoggerFactory.getLogger(ServerExController.class);

    /**
     * 更改系统使用状态（点击停用，停用恢复时调用）
     * @param usingState    系统使用状态
     * @param id             子系统扩展id
     * @return               更改结果
     * @author  zhangfy
     */
    @RequestMapping(value = "/{id}/state/{usingState}")
    private Response doChangeUsingState(@PathVariable("usingState") Integer usingState,
                                       @PathVariable("id") String id,
                                     @CurrentUser("id") String operatorId){
        String message = null;
        try {
            if (UnitExtensionState.DISABLE.equals(usingState)) {
               opServerExService.stopServer(id,operatorId);
                message = "停用成功";
            } else if (UnitExtensionState.NORMAL.equals(usingState)) {
                opServerExService.recoverServer(id,operatorId);
                message = "恢复成功";
            } else {
                return Response.error().message("不合法的状态值【%s】", new String[]{usingState.toString()}).build();
            }
            return Response.ok().message(message).build();
        } catch (NoServerExtensionException e) {
            logger.error("子系统[{}]状态变更为[{}]失败,没有该子系统的扩展信息,操作人员[{}]", id, usingState, operatorId);
            return Response.error().message("没有该子系统的信息，无法操作").build();
        }
    }

    /**
     * 更改系统使用性质（点击试用转正式时调用）
     * @param id 子系统扩展id
     * @param operatorId 操作人员id
     */
    @RequestMapping(value = "/{id}/nature/official")
    private Response doChangeUsingNature(@PathVariable("id") String id,
                                       @CurrentUser("id") String operatorId){
        try {
            opServerExService.turnToOfficial(id,operatorId);
            return Response.ok().message("子系统转正式成功").build();
        } catch (NoServerExtensionException e) {
            logger.error("子系统[{}]转正式失败,没有该单位的扩展信息,操作人员[{}]",id , operatorId);
            return Response.error().message("子系统转正式失败").build();
        }
    }

    /**
     * 系统续期操作（点击续期时调用）
     * @param expireTime   系统到期时间
     * @param id            子系统扩展id
     * @param operatorId 操作人员id
     * @return              更改结果
     * @author  zhangfy
     */
    @RequestMapping(value = "expire")
    private Response doChangeExpireTime(@RequestParam(value = "expireTime",required = false) Date expireTime,
                                         @RequestParam("id") String id,
                                        @CurrentUser("id") String operatorId) {

        try {
           opServerExService.renewal(expireTime,id,operatorId);
        } catch (NoServerExtensionException e) {
            logger.error("子系统[{}]续期失败,没有该子系统的扩展信息,操作人员[{}]", id, operatorId);
            return Response.error().message("没有该子系统的续期信息，续期失败").build();
        } catch (IllegalRenewalTimeException e) {
            logger.error("子系统[{}]续期失败,续期时间[{}]不合法,操作人员[{}]", id, expireTime, operatorId);
            return Response.error().message(e.getMessage()).build();
        }
        return Response.ok().message("续期成功!").build();

        }

    /**
     * 查询已授权子系统
     * @param unitId     单位ID
     * @return   将系统名称，系统状态，以list集合形式返回
     * @author  zhangfy
     */
    @RequestMapping(value = "/{unitId}/authorize")
        private Response doFindAuthorizeSystem(@PathVariable("unitId") String unitId) {
        List<ServerExtensionDto> authorizeSystem = opServerExService.findSystemByUnitId(unitId);
              try {
                  Date date = opUnitService.findByUnitId(unitId).getExpireTime();
                  return Response.ok().data("authorizeSystem", authorizeSystem)
                          .data("unitDate",date).message("查询成功").build();
              }catch (NoUnitExtensionException e){
                  return Response.ok().data("authorizeSystem", authorizeSystem).build();
              }
        }

    /**
     * 查找所有的系统(新增单位时调用) 返回base_server中所有系统的id和name
     *
     * @param
     * @return
     * @author panlf
     */
    @RequestMapping(value = "findAllSystem")
    public Response findAllSystem() {
        List<EnableServerDto> allSystem = opServerExService.getAllEnableServers();
        return Response.ok().data("allSystem", allSystem).build();
    }

    /**
     * 查找某个单位所没有授权的系统 返回id和name 数组的集合
     *
     * @param unitId
     * @return
     * @author panlf
     */
    @RequestMapping(value = "unAuthorizeSystem")
    public Response unAuthorizeSystem(String unitId) {
        List<EnableServerDto> unAuthorizeSystem = opServerExService.unAuthorizeSystem(unitId);
        return Response.ok().data("allSystem", unAuthorizeSystem).build();
    }

    /**
     * 对单位指定的子系统授权
     *
     * @param
     * @return
     * @author panlf
     */
    @RequestMapping(value = "saveAuthoringSystem")
    public Response saveAuthoringSystem(String systems,@CurrentUser("id") String operatorId) {
        String result=opServerExService.saveAuthoringSystem(JSON.parseArray(systems, ServerExtension.class),operatorId);
        logger.info(systems);
        return "ok".equals(result)?Response.ok().message("授权成功!").build():
                Response.error().message("授权失败").build();
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        // CustomDateEditor为自定义日期编辑器
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }


}
