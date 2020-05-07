package net.zdsoft.szxy.operation.servermanage.controller;

import net.zdsoft.szxy.base.entity.UnitExtension;
import net.zdsoft.szxy.operation.servermanage.dto.ServerExtensionDto;
import net.zdsoft.szxy.operation.servermanage.service.OpServerExService;
import net.zdsoft.szxy.operation.unitmanage.dto.EnableServerDto;
import net.zdsoft.szxy.operation.unitmanage.service.NoUnitExtensionException;
import net.zdsoft.szxy.operation.unitmanage.service.OpUnitService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping(value = "/operation/serverEx/manage/page")
public class ServerExPageController {
    @Resource
    private OpServerExService opServerExService;

    @Resource
    private OpUnitService opUnitService;

    @RequestMapping("/authorManage")
    public String authorManage(Model model,String unitId){

        //未授权的系统
        List<EnableServerDto> unAuthorizeSystem = opServerExService.unAuthorizeSystem(unitId);
        model.addAttribute("unAuthorizedSystem",unAuthorizeSystem);

        //已授权的系统

        List<ServerExtensionDto> authorizeSystem = opServerExService.findSystemByUnitId(unitId);
        //到期时间换算成 剩下的天数
        for (ServerExtensionDto dto:authorizeSystem){
            dto.setDayApart(getDaysBetween(Calendar.getInstance().getTime(),dto.getExpireTime())+1);
        }
        model.addAttribute("authorizedSystem",authorizeSystem);
        //单位有效期
        UnitExtension unitInfo = null;
        try {
            unitInfo = opUnitService.findByUnitId(unitId);
            model.addAttribute("unitInfo",unitInfo);
        } catch (NoUnitExtensionException e) {

            e.printStackTrace();
        }

        return "unitmanage/unitmanage-authorize-manage.ftl";
    }

    public static Long getDaysBetween(Date startDate, Date endDate) {
        Calendar fromCalendar = Calendar.getInstance();
        if(endDate==null)return 31L;
        fromCalendar.setTime(startDate);
        fromCalendar.set(Calendar.HOUR_OF_DAY, 0);
        fromCalendar.set(Calendar.MINUTE, 0);
        fromCalendar.set(Calendar.SECOND, 0);
        fromCalendar.set(Calendar.MILLISECOND, 0);

        Calendar toCalendar = Calendar.getInstance();
        toCalendar.setTime(endDate);
        toCalendar.set(Calendar.HOUR_OF_DAY, 0);
        toCalendar.set(Calendar.MINUTE, 0);
        toCalendar.set(Calendar.SECOND, 0);
        toCalendar.set(Calendar.MILLISECOND, 0);
        return (toCalendar.getTime().getTime() - fromCalendar.getTime().getTime()) / (1000 * 60 * 60 * 24);
    }

}
