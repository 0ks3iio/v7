package net.zdsoft.familydear.action;

import com.alibaba.fastjson.JSON;
import net.zdsoft.familydear.service.FamilyDearObjectService;
import net.zdsoft.framework.dataimport.DataImportAction;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.ExcelUtils;
import net.zdsoft.framework.utils.ExportUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.studevelop.data.entity.StudevelopDutySituation;
import net.zdsoft.system.entity.mcode.McodeDetail;
import net.zdsoft.system.remote.service.McodeRemoteService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Designed By luf
 *
 * @author luf
 * @date 2019/5/13 15:20
 */
@Controller
@RequestMapping("/familydear/object")
public class FamilyDearObjImportAction extends DataImportAction {

    @Autowired
    private McodeRemoteService mcodeRemoteService;
    @Autowired
    private FamilyDearObjectService familyDearObjectService;
    @RequestMapping("/import/main")
    public String execute(ModelMap map, String acadyear, String semester, String classId) {
        // 业务名称
        map.put("businessName", "结亲对象信息");
        // 导入URL
        map.put("businessUrl", "/familydear/object/import");
        // 导入模板
        //String searchDateStr=DateUtils.date2String(searchDate,"yyyy-MM-dd");
        map.put("templateDownloadUrl", "/familydear/object/template");
        // 导入对象
        map.put("objectName", getObjectName());
        // 导入说明
        map.put("description", getDescription());

        map.put("businessKey", UuidUtils.generateUuid());
        return "/familydear/cadresRelation/familyDearObjectImport.ftl";
    }
    @Override
    public String getObjectName() {
        return "familyDearObject";
    }

    @Override
    public String getDescription() {
        return "<h4 class='box-graybg-title'>说明</h4>"
                + "<p>1、导入文件中请确认数据是否正确；</p>"
                + "<p>2、导入数据时，表格中的数据只会累计增加，不会更新；</p>";
    }

    @Override
    public List<String> getRowTitleList() {
        List<String> tis = new ArrayList<String>();
        tis.add("结亲村");
//        tis.add("单位");
        tis.add("类别");
        tis.add("结亲对象");
        tis.add("性别");
        tis.add("身份证号");
        tis.add("民族");
//        tis.add("政治面貌");
        tis.add("手机号码");
        tis.add("家庭住址");
        tis.add("备注");
        return tis;
    }
    @RequestMapping("/import")
    @ResponseBody
    @Override
    public String dataImport(String filePath, String params) {
        log.info("业务数据处理中。。。");
        List<String[]> errorDataList = new ArrayList<>();

        String unitId = getLoginInfo().getUnitId();
        List<String[]> arrDatas = ExcelUtils.readExcelByRow(filePath,1,getRowTitleList().size());
        String result = familyDearObjectService.saveImportDate(arrDatas,getLoginInfo());
        log.info("业务数据导入结束。。。");
        return result;
    }
    @Override
    @RequestMapping("/template")
    public void downloadTemplate(HttpServletRequest request, HttpServletResponse response) {
        List<String> titleList = getRowTitleList();//表头
        Map<String,List<Map<String,String>>> sheetName2Map = new HashMap<String, List<Map<String, String>>>();
        sheetName2Map.put(getObjectName(),new ArrayList<Map<String, String>>());
        Map<String,List<String>> titleMap = new HashMap<String, List<String>>();
        titleMap.put(getObjectName(),titleList);
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet();
        int size = titleList.size();

        HSSFRow rowTitle = sheet.createRow(0);
        for(int j=0;j<size;j++){
            HSSFCell cell = rowTitle.createCell(j);
            cell.setCellValue(new HSSFRichTextString(titleList.get(j)));
        }
        setDataValidation(titleList.toArray(new String[0]), 1, size, workbook, sheet);
        ExportUtils.outputData(workbook,"结亲对象信息",response);
    }


    /**
     * 设置数据有效性
     * @param cols
     */
    private void setDataValidation(String[] cols, int beginRow, int endRow,
                                   HSSFWorkbook workbook, HSSFSheet sheet) {
        Map<String, String[]> defineMcodeMap = getColMcodeMap();

        if (MapUtils.isEmpty(defineMcodeMap))
            return;

        if (endRow - beginRow < 500) {
            endRow = beginRow + 500;
        }

        //微代码数量
        int mcodeNum = 0;

        for (int j = 0; j < cols.length; j++) {
            String[] texts=null;
            if(StringUtils.isNotBlank(cols[j]))
                texts = defineMcodeMap.get(cols[j]);
            if (null != texts && texts.length > 0) {
                // 指定第几列默认num行,都设置为含有下拉列表的格式，加入数据有效性到当前sheet对象
                try {
                    mcodeNum ++;
                    HSSFDataValidation datavalidation = createDataValidation(workbook, texts, beginRow, j,
                            endRow - 1, j, mcodeNum-1);
                    sheet.addValidationData(datavalidation);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }

        //隐藏微代码EXCEL
        if (mcodeNum > 0){
            workbook.setSheetHidden(1, true);
        }
    }
    private String[] letters = new String[]{"A","B","C","D","E","F","G","H","I","J","K"};

    /**
     *
     * @param wb
     * @param textList 微代码内容
     * @param firstRow 开始行
     * @param firstCol 开始列
     * @param endRow 结束行
     * @param endCol 结束列
     * @param mcodeNum 第几列
     * @return
     */
    private HSSFDataValidation createDataValidation(HSSFWorkbook wb, String[] textList, int firstRow, int firstCol,
                                                    int endRow, int endCol, int mcodeNum) {
        HSSFSheet hidden = wb.getSheet("hidden");
        if (hidden==null){
            hidden = wb.createSheet("hidden");
        }
        for (int i = 0; i < textList.length; i++) {
            String name = textList[i];
            HSSFRow row = hidden.getRow(i);
            if (row == null){
                row = hidden.createRow(i);
            }
            HSSFCell cell = row.createCell(mcodeNum);
            cell.setCellValue(name);
        }

        // 加载下拉列表内容
//    	char col = (char)('A' + mcodeNum);
        //2014-12-17 modify by like
        //功能完善--当微代码的数量超过26个时  理论上支持（26+1）*26个
        String col = "";
        if(mcodeNum < 26){
            col = String.valueOf((char)('A' + mcodeNum));
        }else{
            int m = mcodeNum%26;
            int n = mcodeNum/26;
            col = letters[n-1]+String.valueOf((char)('A' + m));
        }
        DVConstraint constraint = DVConstraint.createFormulaListConstraint("hidden!$" + col + "$1:$" + col + "$" + textList.length);
        CellRangeAddressList cellRangeAddressList = new CellRangeAddressList(firstRow, endRow, firstCol, endCol);

        // 数据有效性对象
        HSSFDataValidation data_validation = new HSSFDataValidation(cellRangeAddressList, constraint);
        return data_validation;
    }

    public Map<String, String[]> getColMcodeMap(){
        Map<String, String[]> dmcodeMap = new HashMap<String, String[]>();
        String[] mcodes = {"DM-XB","DM-MZ", "DM-ZZMM","DM-XJJQC"};
        List<McodeDetail> mds = SUtils.dt(mcodeRemoteService.findByMcodeIds(mcodes), new TR<List<McodeDetail>>() {} );
        // <code, [内容,值]>
        Map<String, List<String>> mcodeMap = new HashMap<String, List<String>>();
        for(McodeDetail md : mds) {
            List<String> ml = mcodeMap.get(md.getMcodeId());
            if(ml == null) {
                ml = new ArrayList<String>();
            }
            ml.add(md.getMcodeContent());
            mcodeMap.put(md.getMcodeId(), ml);
        }
        String[] cols = {"结亲村","性别", "民族", "政治面貌"};
        String[] colm = {"DM-XJJQC","DM-XB","DM-MZ", "DM-ZZMM"};
        for(int i=0;i<cols.length;i++) {
            String col = cols[i];
            List<String> ml = mcodeMap.get(colm[i]);
            if(CollectionUtils.isNotEmpty(ml)) {
                dmcodeMap.put(col, ml.toArray(new String[0]));
            }
        }
        return dmcodeMap;
    }
}
