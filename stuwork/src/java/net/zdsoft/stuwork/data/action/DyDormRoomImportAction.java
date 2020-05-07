package net.zdsoft.stuwork.data.action;

import com.alibaba.fastjson.JSONObject;
import net.zdsoft.framework.dataimport.DataImportAction;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.ExcelUtils;
import net.zdsoft.framework.utils.ExportUtils;
import net.zdsoft.framework.utils.StringUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.stuwork.data.entity.DyDormBuilding;
import net.zdsoft.stuwork.data.entity.DyDormRoom;
import net.zdsoft.stuwork.data.service.DyDormBedService;
import net.zdsoft.stuwork.data.service.DyDormBuildingService;
import net.zdsoft.stuwork.data.service.DyDormRoomService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.DVConstraint;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDataValidation;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @ProjectName: v7
 * @Package: net.zdsoft.stuwork.data.action
 * @ClassName: DyDormRoomImportAction
 * @Description: java类作用描述
 * @Author: suwei
 * @CreateDate: 2018/11/14 16:44
 * @UpdateUser: 更新者
 * @UpdateDate: 2018/11/14 16:44
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
@Controller
@RequestMapping("/stuwork/dyDormRoomImport")
public class DyDormRoomImportAction extends DataImportAction {
    private Logger logger = Logger.getLogger(DyDormRoomImportAction.class);
    @Autowired
    private DyDormRoomService dyDormRoomService;
    @Autowired
    private DyDormBuildingService dyDormBuildingService;
    @Autowired
    private DyDormBedService dyDormBedService;
    @RequestMapping("/main")
    public String getMainImportView(ModelMap map, String acadyear, String semester, String buildingId,String roomType){
        // 业务名称
        map.put("businessName", "寝室信息维护");
        // 导入URL
        map.put("businessUrl", "/stuwork/dyDormRoomImport/import");
        // 导入模板
        map.put("templateDownloadUrl", "/stuwork/dyDormRoomImport/template?acadyear="+acadyear+"&semester="+semester+"&buildingId="+buildingId);
        // 导入对象
        map.put("objectName", getObjectName());
        // 导入说明
        map.put("description", getDescription());

        DyDormBuilding building=dyDormBuildingService.findOne(buildingId);

        map.put("acadyear", acadyear);
        map.put("semester", semester);
        map.put("buildingName", building!=null?building.getName():"");
        map.put("buildingId", buildingId);
        map.put("roomType", roomType);
        //map.put("roomType", roomType);
        //map.put("roomTypeName", roomType.equals("1")?"男生寝室":"女生寝室");

        map.put("businessKey", UuidUtils.generateUuid());
        return "/stuwork/dorm/dyDormRoomImport.ftl";
    }
    @Override
    public String getObjectName() {
        return "DyDormRoomImport";
    }

    @Override
    public String getDescription() {
        return "<h4 class='box-graybg-title'>说明</h4>"
                + "<p>1、导入文件中请确认数据是否正确</p>"
                + "<p>2、导入寝室信息不能重复</p>";
    }

    @Override
    public List<String> getRowTitleList() {
        List<String> tis = new ArrayList<String>();
        tis.add("寝室楼");
        tis.add("寝室类别");
        tis.add("寝室属性");
        tis.add("寝室号");
        tis.add("容纳人数");
        tis.add("楼层");
        return tis;
    }

    @ResponseBody
    @RequestMapping("/import")
    @Override
    public String dataImport(String filePath, String params) {
        logger.info("业务数据处理中......");
        List<String[]> datas = ExcelUtils.readExcel(filePath,
                getRowTitleList().size());
        System.out.println(datas.size());
        // 业务处理模块　具体的业务在这里处理
        //自己的处理接口　数据范围格式如下
        //业务类中返回给我的一个json格式的数据
        JSONObject json=JSONObject.parseObject(params);
        String buildingId=json.getString("buildingId");
        String roomType=json.getString("roomType");
        String jsonMsg =dyDormRoomService.doDyDormRoomImport(getLoginInfo().getUnitId(), buildingId,roomType, datas);
        logger.info("导入结束......");
        return jsonMsg;
    }

    @Override
    @RequestMapping("/template")
    public void downloadTemplate(HttpServletRequest request,
                                 HttpServletResponse response) {
//        List<String> tis = getRowTitleList();
//        Map<String, List<Map<String, String>>> sheetName2RecordListMap = new HashMap<String, List<Map<String, String>>>();
//        List<Map<String,String>> recordList = new ArrayList<Map<String, String>>();
//        //加入模板数据
//        String buildingId=request.getParameter("buildingId");
//        String roomType=request.getParameter("roomType");
////        getRecordList(recordList,buildingId,roomType);
//        sheetName2RecordListMap.put(getObjectName(),recordList);
//        Map<String, List<String>> titleMap = new HashMap<String, List<String>>();
//        titleMap.put(getObjectName(), tis);
//        ExportUtils ex = ExportUtils.newInstance();
//        ex.exportXLSFile("寝室信息导入模板", titleMap, sheetName2RecordListMap, response);
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
        List<DyDormBuilding> buildingList=dyDormBuildingService.findByUnitId(getLoginInfo().getUnitId());
        Set<String> buildingNameSet = new HashSet<>();
        for(DyDormBuilding dyDormBuilding:buildingList){
            if(StringUtils.isNotBlank(dyDormBuilding.getName())) {
                buildingNameSet.add(dyDormBuilding.getName());
            }
        }
        String[] roomTypes = new String[] {"男寝室","女寝室"};
        String[] roomPropretys = new String[] {"学生寝室","老师寝室"};

        setHSSFValidation(sheet, buildingNameSet.toArray(new String[0]), 1, 10000, 0, 0);
        setHSSFValidation(sheet, roomTypes, 1, 10000, 1, 1);
        setHSSFValidation(sheet, roomPropretys, 1, 10000, 2, 2);
        String fileName = "寝室信息导入模板";
        ExportUtils.outputData(workbook, fileName, response);
    }
    private void getRecordList(List<Map<String, String>> recordList,String buildingId,String roomType){
        String unitId = getLoginInfo().getUnitId();
        Map<String,String> buildingMap=EntityUtils.getMap(dyDormBuildingService.findByUnitId(unitId),"id", "name");
        List<DyDormRoom> roomList=dyDormRoomService.getDormRoomByCon(getLoginInfo().getUnitId(), buildingId, roomType,null, null);
        if(CollectionUtils.isNotEmpty(roomList)){
            Map<String,String> valueMap = null;
            for(DyDormRoom room:roomList){
                valueMap=new HashMap<String, String>();
                valueMap.put("寝室楼", buildingMap.get(room.getBuildingId()));
                valueMap.put("寝室类别",room.getRoomType().equals("1")?"男寝室":"女寝室");
                valueMap.put("寝室属性",room.getRoomProperty());
                valueMap.put("寝室号",room.getRoomName());
                //valueMap.put("楼层",String.valueOf(room.getFloor()));
                //valueMap.put("容纳人数",String.valueOf(room.getCapacity()));
                valueMap.put("容纳人数",room.getCapacity()+"");
                valueMap.put("楼层",room.getFloor()+"");
                recordList.add(valueMap);
            }
        }
    }

    /**
     * 设置某些列的值只能输入预制的数据,显示下拉框.
     *
     * @param sheet
     *            要设置的sheet.
     * @param textlist
     *            下拉框显示的内容
     * @param firstRow
     *            开始行
     * @param endRow
     *            结束行
     * @param firstCol
     *            开始列
     * @param endCol
     *            结束列
     * @return 设置好的sheet.
     */
    public static HSSFSheet setHSSFValidation(HSSFSheet sheet,
                                              String[] textlist, int firstRow, int endRow, int firstCol,
                                              int endCol) {
        // 加载下拉列表内容
        DVConstraint constraint = DVConstraint
                .createExplicitListConstraint(textlist);
        // 设置数据有效性加载在哪个单元格上,四个参数分别是：起始行、终止行、起始列、终止列
        CellRangeAddressList regions = new CellRangeAddressList(firstRow,
                endRow, firstCol, endCol);
        // 数据有效性对象
        HSSFDataValidation data_validation_list = new HSSFDataValidation(
                regions, constraint);
        sheet.addValidationData(data_validation_list);
        return sheet;
    }

    /**
     * 设置单元格上提示
     *
     * @param sheet
     *            要设置的sheet.
     * @param promptTitle
     *            标题
     * @param promptContent
     *            内容
     * @param firstRow
     *            开始行
     * @param endRow
     *            结束行
     * @param firstCol
     *            开始列
     * @param endCol
     *            结束列
     * @return 设置好的sheet.
     */
    public static HSSFSheet setHSSFPrompt(HSSFSheet sheet, String promptTitle,
                                          String promptContent, int firstRow, int endRow, int firstCol,
                                          int endCol) {
        // 构造constraint对象
        DVConstraint constraint = DVConstraint
                .createCustomFormulaConstraint("BB1");
        // 四个参数分别是：起始行、终止行、起始列、终止列
        CellRangeAddressList regions = new CellRangeAddressList(firstRow,
                endRow, firstCol, endCol);
        // 数据有效性对象
        HSSFDataValidation data_validation_view = new HSSFDataValidation(
                regions, constraint);
        data_validation_view.createPromptBox(promptTitle, promptContent);
        sheet.addValidationData(data_validation_view);
        return sheet;
    }
}
