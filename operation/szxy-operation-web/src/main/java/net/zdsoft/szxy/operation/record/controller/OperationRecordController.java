package net.zdsoft.szxy.operation.record.controller;

import net.zdsoft.szxy.base.api.UnitRemoteService;
import net.zdsoft.szxy.base.entity.Unit;
import net.zdsoft.szxy.operation.inner.entity.OpUser;
import net.zdsoft.szxy.operation.record.controller.dto.OperationRecordQueryDto;
import net.zdsoft.szxy.operation.record.entity.OperationRecord;
import net.zdsoft.szxy.operation.record.enums.NotSupportOperateCodeException;
import net.zdsoft.szxy.operation.record.enums.OperateType;
import net.zdsoft.szxy.operation.record.service.OperationRecordService;
import net.zdsoft.szxy.operation.inner.service.OpUserService;
import net.zdsoft.szxy.plugin.mvc.Response;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 服务操作记录
 *
 * @author panlf 2019年1月16日
 */
@Controller
@RequestMapping("/operation/systemLog")
public class OperationRecordController {

    @Autowired
    private OperationRecordService operationRecordService;
    @Resource
    private OpUserService opUserService;

    @Resource
    private UnitRemoteService unitRemoteService;
    /**
     * 服务操作记录数据展示页面
     */
    @RequestMapping(value = "/account",method = RequestMethod.GET)
    public String findServerRecord(OperationRecordQueryDto queryDto, Pageable page, Model model) {
        Page<OperationRecord> pages;
        try {
            pages = operationRecordService.getOperationRecords(queryDto.getUnitId(),
                    OperateType.valueOf(queryDto.getType()), queryDto.getStart(), queryDto.getEnd(), page);
            convertHumanText(pages.getContent());
        } catch (NotSupportOperateCodeException e) {
            throw new RuntimeException("查询错误!");
        }
        model.addAttribute("pages",pages);
        return "/record/record-accountList.ftl";
    }

    private void convertHumanText(List<OperationRecord> records) {
        if (CollectionUtils.isEmpty(records)) {
            return;
        }
        String[] operatorIds = records.stream().map(OperationRecord::getOperatorId).toArray(String[]::new);
        String[] operateUnitIds = records.stream().map(OperationRecord::getOperateUnitId).toArray(String[]::new);
        Map<String, String> operators = opUserService.getOpUsersByUserIds(operatorIds)
                .stream().collect(Collectors.toMap(OpUser::getId, OpUser::getRealName, (k, v) -> v));
        Map<String, String> unitNames = unitRemoteService.getUnitsByIds(operateUnitIds).stream()
                .collect(Collectors.toMap(Unit::getId, Unit::getUnitName));
        for (OperationRecord record : records) {
            record.setOperatorName(operators.get(record.getOperatorId()));
            Optional<OperateType> operateType = OperateType.of(record.getOperateType());
            if (operateType.isPresent()) {
                record.setOperatorTypeName(operateType.get().getOperateName());
            } else {
                record.setOperatorTypeName("未知的操作类型");
            }
            record.setUnitName(unitNames.get(record.getOperateUnitId()));
        }
    }

    /**
     * 服务操作记录基础页面
     */
    @RequestMapping(value = "/page",method = RequestMethod.GET)
    public String page(Model model) {
        model.addAttribute("operateType",OperateType.values());
        return "/record/record-page.ftl";
    }

    /**
     * 服务操作记录首页(包含ztree列表)
     */
    @RequestMapping(value = "/index",method = RequestMethod.GET)
    public String index() {
        return "/record/record-index.ftl";
    }

    /**
     * 单位续期次数
     */
    @RequestMapping(value = "/renewalCount",method = RequestMethod.GET)
    @ResponseBody
    public Response getRenewalCount(String unitId){
        if(StringUtils.isBlank(unitId)){
            return Response.error().message("unitId不能为空").build();
        }
        Integer renewalCount= operationRecordService.getRenewalCount(unitId);
        return Response.ok().data("renewalCount",renewalCount).build();
    }
}
