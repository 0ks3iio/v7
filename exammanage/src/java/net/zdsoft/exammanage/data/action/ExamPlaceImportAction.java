package net.zdsoft.exammanage.data.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import net.zdsoft.basedata.entity.TeachPlace;
import net.zdsoft.basedata.remote.service.TeachBuildingRemoteService;
import net.zdsoft.basedata.remote.service.TeachPlaceRemoteService;
import net.zdsoft.exammanage.data.entity.EmPlace;
import net.zdsoft.exammanage.data.service.EmPlaceService;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.dataimport.DataImportAction;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.entity.LoginInfo;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
@RequestMapping("/exammanage/examPlace")
public class ExamPlaceImportAction extends DataImportAction {
    private Logger logger = Logger.getLogger(ExamPlaceImportAction.class);
    @Autowired
    private EmPlaceService emPlaceService;
    @Autowired
    private TeachPlaceRemoteService teachPlaceRemoteService;
    @Autowired
    private TeachBuildingRemoteService teachBuildingRemoteService;


    @ControllerInfo("进入导入首页")
    @RequestMapping("/main")
    public String execute(String examId, ModelMap map) {
        LoginInfo loginInfo = getLoginInfo();
        String unitId = loginInfo.getUnitId();

        // 业务名称
        map.put("businessName", "考试场地设置");
        // 导入URL
        map.put("businessUrl", "/exammanage/examPlace/import");
        // 导入模板
        map.put("templateDownloadUrl", "/exammanage/examPlace/template");
        // 导入对象
        map.put("objectName", "");
        // 导入说明
        map.put("description", getDescription());
        map.put("businessKey", UuidUtils.generateUuid());

        //如果列名在第一行的就不需要传
        map.put("validRowStartNo", 1);
        //模板校验
        map.put("validateUrl", "/exammanage/examPlace/validate");

        JSONObject obj = new JSONObject();
        obj.put("examId", examId);
        obj.put("unitId", unitId);
        map.put("monthPermance", JSON.toJSONString(obj));
        map.put("examId", examId);
        return "/exammanage/importFtl/examPlaceImport.ftl";
    }

    @Override
    public String getObjectName() {
        return "examPlace";
    }

    @Override
    public String getDescription() {
        return "<h4 class='box-graybg-title'>说明</h4>"
                + "<p>1、导入时，考试场地编号不能重复，最长不超过9位数，例如：001，如果数据已存在会进行更新操作。</p>"
                + "<p>2、导入时，数据保存后，可能存在多个考场使用同一场地的可能性，可以在页面列表中查看，并可进行后续操作。</p>";
    }

    @Override
    public List<String> getRowTitleList() {
        //下载文件表头
        List<String> tis = new ArrayList<String>();
        tis.add("考试场地编号");
        tis.add("考试场地");
        tis.add("所属教学楼");
        tis.add("可容纳人数");
        return tis;
    }

    @Override
    @RequestMapping("/import")
    @ResponseBody
    public String dataImport(String filePath, String params) {
        logger.info("业务数据处理中......");
        //每一行数据   表头列名：0
        List<String[]> datas = ExcelUtils.readExcelByRow(filePath, 2, getRowTitleList().size());

        //参数
        JSONObject performance = JSON.parseObject(params, JSONObject.class);
        String examId = (String) performance.get("examId");
        String unitId = (String) performance.get("unitId");

        //验证数据
        List<TeachPlace> placeList = SUtils.dt(teachPlaceRemoteService.findTeachPlaceListByType(unitId, null), new TR<List<TeachPlace>>() {
        });
        //key:placeName,value:placeIds
        Map<String, List<String>> placeIdsByName = new HashMap<String, List<String>>();
        //buildingid不为空 key:placeId value:buildingid
        Map<String, String> buildingByPlaceId = new HashMap<String, String>();
        Set<String> buildId = new HashSet<String>();
        if (CollectionUtils.isNotEmpty(placeList)) {
            for (TeachPlace p : placeList) {
                if (StringUtils.isNotBlank(p.getTeachBuildingId())) {
                    buildingByPlaceId.put(p.getId(), p.getTeachBuildingId());
                    buildId.add(p.getTeachBuildingId());
                }
                if (!placeIdsByName.containsKey(p.getPlaceName())) {
                    placeIdsByName.put(p.getPlaceName(), new ArrayList<String>());
                }
                placeIdsByName.get(p.getPlaceName()).add(p.getId());
            }
        }
        Map<String, String> buildMap = new HashMap<String, String>();
        if (buildId.size() > 0) {
            buildMap = SUtils.dt(teachBuildingRemoteService.findTeachBuildMap(buildId.toArray(new String[]{})), new TR<Map<String, String>>() {
            });
        }
        //已有数据
        List<EmPlace> emList = emPlaceService.findByExamIdAndSchoolIdWithMaster(examId, unitId, false);
        //key:编号 value:EmPlace
        Map<String, EmPlace> emPlaceByCode = new HashMap<String, EmPlace>();
        if (CollectionUtils.isNotEmpty(emList)) {
            for (EmPlace em : emList) {
                emPlaceByCode.put(em.getExamPlaceCode(), em);
            }
        }
        List<EmPlace> insertList = new ArrayList<EmPlace>();
        Set<String> insertIds = new HashSet<String>();
        List<String[]> errorDataList = new ArrayList<String[]>();
        int sequence = 2;//初始行
        int successNum = 0;
        int totalSize = datas.size();
        for (String[] arr : datas) {
            String[] errorData = new String[4];
            sequence++;
            if (StringUtils.isBlank(arr[0])) {
                errorData[0] = String.valueOf(sequence);
                errorData[1] = "考试场地编号";
                errorData[2] = "";
                errorData[3] = "不能为空";
                errorDataList.add(errorData);
                continue;
            }
            String placeCode = arr[0].trim();
            //由001组成
            if (!isNumeric(placeCode, null) || placeCode.length() > 9) {
                errorData[0] = String.valueOf(sequence);
                errorData[1] = "考试场地编号";
                errorData[2] = "考试场地编号：" + placeCode;
                errorData[3] = "格式不对，需要不超过9位的数字构成";
                errorDataList.add(errorData);
                continue;
            }

            if (StringUtils.isBlank(arr[1])) {
                errorData[0] = String.valueOf(sequence);
                errorData[1] = "考试场地";
                errorData[2] = "";
                errorData[3] = "不能为空";
                errorDataList.add(errorData);
                continue;
            }
            String placeName = arr[1].trim();
            if (!placeIdsByName.containsKey(placeName)) {
                errorData[0] = String.valueOf(sequence);
                errorData[1] = "考试场地";
                errorData[2] = "";
                errorData[3] = "不存在该场地";
                errorDataList.add(errorData);
                continue;
            }
            List<String> pIds = placeIdsByName.get(placeName);
            String placeId = null;
            if (StringUtils.isBlank(arr[2])) {
                if (pIds.size() > 1) {
                    errorData[0] = String.valueOf(sequence);
                    errorData[1] = "考试场地";
                    errorData[2] = "考试场地：" + placeName;
                    errorData[3] = "不唯一";
                    errorDataList.add(errorData);
                    continue;
                } else {
                    //
                    placeId = pIds.get(0);
                }

            } else {
                String buildName = arr[2].trim();
                List<String> ppIds = new ArrayList<String>();
                for (String pp : pIds) {
                    if (!buildingByPlaceId.containsKey(pp)) {
                        continue;
                    }
                    String bbId = buildingByPlaceId.get(pp);
                    if (!buildMap.containsKey(bbId)) {
                        continue;
                    }
                    if (buildMap.get(bbId).equals(buildName)) {
                        ppIds.add(pp);
                    }
                }
                if (ppIds.size() == 0) {
                    errorData[0] = String.valueOf(sequence);
                    errorData[1] = "所属教学楼";
                    errorData[2] = "考试场地：" + placeName + "，所属教学楼：" + buildName;
                    errorData[3] = "数据错误，未匹配场地与教学楼";
                    errorDataList.add(errorData);
                    continue;
                }
                if (ppIds.size() > 1) {
                    errorData[0] = String.valueOf(sequence);
                    errorData[1] = "所属教学楼";
                    errorData[2] = "考试场地：" + placeName + "，所属教学楼：" + buildName;
                    errorData[3] = "匹配场地与教学楼，数据不唯一";
                    errorDataList.add(errorData);
                    continue;
                }
                placeId = ppIds.get(0);
            }

            if (StringUtils.isBlank(arr[3])) {
                errorData[0] = String.valueOf(sequence);
                errorData[1] = "可容纳人数";
                errorData[2] = "";
                errorData[3] = "不能为空";
                errorDataList.add(errorData);
                continue;
            }
            String numStr = arr[3].trim();

            if (!isNumeric(numStr, null)) {
                errorData[0] = String.valueOf(sequence);
                errorData[1] = "可容纳人数";
                errorData[2] = "可容纳人数：" + numStr;
                errorData[3] = "格式不对，需是正整数";
                errorDataList.add(errorData);
                continue;
            }
            int num = Integer.parseInt(numStr);
            if (num <= 0) {
                errorData[0] = String.valueOf(sequence);
                errorData[1] = "可容纳人数";
                errorData[2] = "可容纳人数：" + numStr;
                errorData[3] = "格式不对，需是正整数";
                errorDataList.add(errorData);
                continue;
            }
            if (StringUtils.isNotBlank(placeId)) {
                if (emPlaceByCode.containsKey(placeCode)) {
                    EmPlace pp = emPlaceByCode.get(placeCode);
                    pp.setPlaceId(placeId);
                    pp.setCount(num);
                    if (!insertIds.contains(pp.getId())) {
                        insertList.add(pp);
                    }

                } else {
                    EmPlace pp = new EmPlace();
                    pp.setPlaceId(placeId);
                    pp.setCount(num);
                    pp.setExamId(examId);
                    pp.setSchoolId(unitId);
                    pp.setExamPlaceCode(placeCode);
                    pp.setId(UuidUtils.generateUuid());
                    insertList.add(pp);
                    emPlaceByCode.put(pp.getExamPlaceCode(), pp);
                    insertIds.add(pp.getId());
                }

                successNum++;
            }

        }
        try {
            if (CollectionUtils.isNotEmpty(insertList)) {
                emPlaceService.saveAllEntitys(insertList.toArray(new EmPlace[]{}));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return result(totalSize, 0, totalSize, errorDataList);
        }
        int errorCount = totalSize - successNum;
        String result = result(totalSize, successNum, errorCount, errorDataList);
        return result;

    }

    private String result(int totalCount, int successCount, int errorCount, List<String[]> errorDataList) {
        Json importResultJson = new Json();
        importResultJson.put("totalCount", totalCount);
        importResultJson.put("successCount", successCount);
        importResultJson.put("errorCount", errorCount);
        importResultJson.put("errorData", errorDataList);
        return importResultJson.toJSONString();
    }

    private boolean isNumeric(String str, Integer num) {
        Pattern pattern = null;
        if (num == null) {
            pattern = Pattern.compile("[0-9]*");
        } else {
            pattern = Pattern.compile("[0-9]{" + num + "}");
        }

        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }


    /**
     * 模板填写说明
     *
     * @return
     */
    private String getExplanation() {
        StringBuffer message = new StringBuffer();
        message.append("填写注意：\n");
        message.append("1：考试场地编号，必填，需要由不超过9位的数字组成，建议使用连号，如果输入格式是数字，请改为文本，例如001,002。\n");
        message.append("2：考试场地，必填；所属教学楼，非必填，但一旦填写，那么需要匹配考试场地与所属教学楼。\n");
        message.append("3：可容纳人数，必填，需要是正整数。");
        return message.toString();
    }

    @Override
    @RequestMapping("/template")
    public void downloadTemplate(HttpServletRequest request,
                                 HttpServletResponse response) {
        List<String> titleList = getRowTitleList();//表头
        Map<String, List<Map<String, String>>> sheetName2Map = new HashMap<String, List<Map<String, String>>>();
        sheetName2Map.put(getObjectName(), new ArrayList<Map<String, String>>());
        Map<String, List<String>> titleMap = new HashMap<String, List<String>>();
        titleMap.put(getObjectName(), titleList);
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet();
        int size = titleList.size();

        //sheet 合并单元格
        CellRangeAddress car = new CellRangeAddress(0, 0, 0, size - 1);
        sheet.addMergedRegion(car);
        //列宽
        for (int i = 0; i < size; i++) {
            sheet.setColumnWidth(i, 20 * 256);
        }

        // HSSFCell----单元格样式
        HSSFCellStyle style = workbook.createCellStyle();
        HSSFFont headfont = workbook.createFont();
        headfont.setFontHeightInPoints((short) 9);// 字体大小
        headfont.setColor(HSSFFont.COLOR_RED);//字体颜色
        style.setFont(headfont);
        style.setAlignment(HorizontalAlignment.LEFT);//水平
        style.setVerticalAlignment(VerticalAlignment.CENTER);//垂直
        style.setWrapText(true);//自动换行

        //第一行
        HSSFRow titleRow = sheet.createRow(0);
        //高度：5倍默认高度
        titleRow.setHeightInPoints(5 * sheet.getDefaultRowHeightInPoints());

        HSSFCell titleCell = titleRow.createCell(0);
        titleCell.setCellStyle(style);


        //注意事项
        titleCell.setCellValue(new HSSFRichTextString(getExplanation()));
        titleCell.setCellStyle(style);

        HSSFRow rowTitle = sheet.createRow(1);
        for (int j = 0; j < size; j++) {
            HSSFCell cell = rowTitle.createCell(j);
            cell.setCellValue(new HSSFRichTextString(titleList.get(j)));
        }
        ExportUtils exportUtils = ExportUtils.newInstance();
        exportUtils.outputData(workbook, "考试场地设置", response);

//		Map<String, List<Map<String, String>>> sheetName2RecordListMap = new HashMap<String, List<Map<String, String>>>();
//		sheetName2RecordListMap.put(getObjectName(),
//				new ArrayList<Map<String, String>>());
//		Map<String, List<String>> titleMap = new HashMap<String, List<String>>();
//		titleMap.put(getObjectName(), titleList);
//		ExportUtils exportUtils = ExportUtils.newInstance();
//		exportUtils.exportXLSFile("考试场地设置", titleMap, sheetName2RecordListMap, response);

    }

    @RequestMapping("/validate")
    @ResponseBody
    public String validate(String filePath, String validRowStartNo) {
        logger.info("模板校验中......");
        if (StringUtils.isBlank(validRowStartNo)) {
            validRowStartNo = "0";
        }
        try {
            List<String[]> datas = ExcelUtils.readExcelByRow(filePath,
                    Integer.valueOf(validRowStartNo), getRowTitleList().size());

            return templateValidate(datas, getRowTitleList());

        } catch (Exception e) {
            e.printStackTrace();
            return "上传文件不符合模板要求";
        }
    }

}
