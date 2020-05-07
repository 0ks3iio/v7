package net.zdsoft.szxy.operation.unitmanage.controller;

import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.fastjson.JSON;
import net.zdsoft.szxy.base.api.MicrocodeDetailRemoteService;
import net.zdsoft.szxy.base.entity.MicrocodeDetail;
import net.zdsoft.szxy.monitor.Record;
import net.zdsoft.szxy.monitor.RecordType;
import net.zdsoft.szxy.operation.unitmanage.UnitOperateCode;
import net.zdsoft.szxy.operation.unitmanage.dao.OpUnitDao;
import net.zdsoft.szxy.operation.unitmanage.dto.StudentAndFamilyAccount;
import net.zdsoft.szxy.operation.unitmanage.dto.StudentAndFamilyAccountQuery;
import net.zdsoft.szxy.operation.unitmanage.dto.UnitExportDto;
import net.zdsoft.szxy.operation.unitmanage.service.UserManageService;
import net.zdsoft.szxy.operation.unitmanage.vo.StudentAndFamilyExportVo;
import net.zdsoft.szxy.operation.unitmanage.vo.UnitExportVo;
import net.zdsoft.szxy.plugin.mvc.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author panlf
 * @since 2019/2/25 下午5:36
 */
@Controller
@RequestMapping("/operation/unit/manage/export")
public class UnitManageExportController {
    @Resource
    private OpUnitDao opUnitDao;
    @Resource
    private MicrocodeDetailRemoteService microcodeDetailRemoteService;
    @Resource
    private UserManageService userManageService;

    //@RequestMapping("/excel")
    //@ResponseBody
    //public Response findUnitVo() {
    //    List<UnitExportDto> list = opUnitDao.findAllUnitVo();
    //    return Response.ok().data("list", list).build();
    //}

    private Logger logger = LoggerFactory.getLogger(UnitManageExportController.class);

    @Secured(UnitOperateCode.UNIT_EXPORT_EXCEL)
    @Record(type = RecordType.URL)
    @GetMapping(
            value = "/getAllunits"
    )
    public void doExportTeacherAccounts(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            //获得unitType 数值 和 名字 的map映射
            List<MicrocodeDetail> microcodeDetails = microcodeDetailRemoteService.getMicrocodesByMicrocode("DM-DWLX");
            Map<String, String> unitTypeMap = microcodeDetails.stream().collect(Collectors.toMap(MicrocodeDetail::getThisId, MicrocodeDetail::getMicrocodeContent));
            //获得所有单位的信息
            List<UnitExportDto> allUnitExport = opUnitDao.findAllUnitVo();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ExcelWriter writer = new ExcelWriter(out, ExcelTypeEnum.XLSX, true);
            int i = 1;
            ArrayList<UnitExportVo> list = new ArrayList<>();
            for (UnitExportDto unit : allUnitExport) {
                UnitExportVo excel = new UnitExportVo();
                excel.setIndex(i++);
                if (unit.getRegionName() != null)
                    excel.setRegionName(unit.getRegionName());
                if (unit.getSysExpiredCount() != null)
                    excel.setSystemCount(unit.getSysExpiredCount());
                if (unit.getUnionCode() != null)
                    excel.setUnionCode(unit.getUnionCode());
                //单位类型
                if (unit.getUnitType() != null) {
                    excel.setUnitType(unitTypeMap.get(unit.getUnitType().toString()));
                }
                if (unit.getExpireTimeType() == null || unit.getExpireTimeType().equals(0) || unit.getExpireTime() == null) {
                    excel.setExpireTime("永久");
                } else {
                    excel.setExpireTime(formatter.format(unit.getExpireTime()));
                }
                if (unit.getUnitName() != null)
                    excel.setUnitName(unit.getUnitName());
                if (unit.getUsingState() != null)
                    excel.setUsingState(unit.getUsingState().equals(0) ? "正常" : (unit.getUsingState().equals(1) ? "停用" : "到期停用"));
                if (unit.getUsingNature() != null)
                    excel.setUsingNature(unit.getUsingNature().equals(0) ? "试用" : "正式");
                list.add(excel);
            }

            String fileName = "单位信息 -" + new SimpleDateFormat("yyyy/MM/dd").format(new Date());
            fileName = new String(fileName.getBytes(), StandardCharsets.ISO_8859_1);
            Sheet sheet1 = new Sheet(1, 0, UnitExportVo.class);
            sheet1.setSheetName("第一个sheet");
            writer.write(list, sheet1);
            writer.finish();
            response.setHeader("Cache-Control", "");
            response.setContentType("application/data;charset=UTF-8");
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
            OutputStream ro = response.getOutputStream();
            ro.write(out.toByteArray());
            ro.flush();
        } catch (IOException e) {
            logger.error("导出单位失败", e);
            throw e;
        }
    }

    @Record(type = RecordType.URL)
    @GetMapping(
            value = "/{unitId}/getAllStudentAndFamilyAccounts"
    )
    public void doExportStudentAndFamilyAccounts(HttpServletRequest request, HttpServletResponse response,
                                                 @PathVariable("unitId") String unitId,
                                                 @RequestParam("clazzId") String clazzId, Pageable pageable) throws IOException {
        try {
            //获得所有学生和家长的信息
            List<StudentAndFamilyAccount> allStudentAndFamilyExport = userManageService.getStudentAndFamilyAccountsByUnitId(unitId,
                    StudentAndFamilyAccountQuery.of(clazzId), pageable).getContent();
            System.out.println(JSON.toJSONString(allStudentAndFamilyExport));
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ExcelWriter writer = new ExcelWriter(out, ExcelTypeEnum.XLSX, true);
            int i = 1;
            ArrayList<StudentAndFamilyExportVo> list = new ArrayList<>();
            for (StudentAndFamilyAccount account : allStudentAndFamilyExport) {
                StudentAndFamilyExportVo excel = new StudentAndFamilyExportVo();
                excel.setIndex(i++);
                if (account.getStudentName() != null) {
                    excel.setStudentName(account.getStudentName());
                }
                if (account.getStudentUsername() != null) {
                    excel.setStudentUsername(account.getStudentUsername());
                }
                if (account.getFamilyName() != null){
                    excel.setFamilyName(account.getFamilyName());
                }
                if (account.getFamilyUsername() != null){
                    excel.setFamilyUsername(account.getFamilyUsername());
                }
                if (account.getFamilyPhone() != null){
                    excel.setFamilyPhone(account.getFamilyPhone());
                }
                list.add(excel);
            }

            String fileName = "学生与家长信息 -" + new SimpleDateFormat("yyyy/MM/dd").format(new Date());
            fileName = new String(fileName.getBytes(), StandardCharsets.ISO_8859_1);
            Sheet sheet1 = new Sheet(1, 0, StudentAndFamilyExportVo.class);
            sheet1.setSheetName("第一个sheet");
            writer.write(list, sheet1);
            writer.finish();
            response.setHeader("Cache-Control", "");
            response.setContentType("application/data;charset=UTF-8");
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
            OutputStream ro = response.getOutputStream();
            ro.write(out.toByteArray());
            ro.flush();
        } catch (IOException e) {
            logger.error("导出单位失败", e);
            throw e;
        }
    }
}
