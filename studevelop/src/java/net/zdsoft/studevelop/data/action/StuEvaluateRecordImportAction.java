package net.zdsoft.studevelop.data.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.framework.dataimport.DataImportAction;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.ExcelUtils;
import net.zdsoft.framework.utils.ExportUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.studevelop.data.service.StuEvaluateRecordService;
import net.zdsoft.system.entity.mcode.McodeDetail;
import net.zdsoft.system.remote.service.McodeRemoteService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
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

import com.alibaba.fastjson.JSONObject;
@Controller
@RequestMapping("/studevelop")
public class StuEvaluateRecordImportAction extends DataImportAction{
	private Logger logger = Logger.getLogger(StuEvaluateRecordImportAction.class);
	@Autowired
	private StuEvaluateRecordService stuEvaluateRecordService;
	@Autowired
	private ClassRemoteService classRemoteService;
	@Autowired
	private StudentRemoteService studentRemoteService;
	@Autowired
	private McodeRemoteService mcodeRemoteService;
	@RequestMapping("/evaluateRecord/import/main")
	public String execute(ModelMap map,String acadyear,String semester, String classId) {
		// 业务名称
		map.put("businessName", "学生评价");
		// 导入URL 
		map.put("businessUrl", "/studevelop/evaluateRecord/import");
		// 导入模板
		//String searchDateStr=DateUtils.date2String(searchDate,"yyyy-MM-dd");
		map.put("templateDownloadUrl", "/studevelop/evaluateRecord/template?acadyear="+acadyear+"&semester="+semester+"&classId="+classId);
		// 导入对象
		map.put("objectName", getObjectName());
		// 导入说明
		map.put("description", getDescription());
			
		map.put("businessKey", UuidUtils.generateUuid());
		map.put("acadyear", acadyear);
		map.put("semester", semester);
		map.put("classId", classId);
		return "/studevelop/record/stuEvaluateRecordImport.ftl";
	}

	@Override
	public String getObjectName() {
		return "stuEvaluateRecordImport";
	}

	@Override
	public String getDescription() {
		return "<h4 class='box-graybg-title'>说明</h4>"
				+ "<p>1、导入文件中请确认数据是否正确</p>";
	}

	@Override
	public List<String> getRowTitleList() {
		List<String> tis = new ArrayList<String>();
		tis.add("姓名");
		tis.add("学号");
		tis.add("班级");
		tis.add("班内编号");
		tis.add("评语等级");
		tis.add("老师寄语");
		tis.add("个性特点");
		return tis;
	}

	@Override
	@RequestMapping("/evaluateRecord/import")
	@ResponseBody
	public String dataImport(String filePath, String params) {
		logger.info("业务数据处理中......");
		List<String[]> datas = ExcelUtils.readExcel(filePath,
				getRowTitleList().size());
		if(datas.size() == 0){
			Json importResultJson=new Json();
			importResultJson.put("totalCount", 0);
			importResultJson.put("successCount", 0);
			importResultJson.put("errorCount", 0);
			return importResultJson.toJSONString();
		}
		JSONObject jsStr = JSONObject.parseObject(params); //将字符串{“id”：1}
		String acadyear = jsStr.getString("acadyear");
		String semester = jsStr.getString("semester");
		String jsonMsg = stuEvaluateRecordService.doImport(getLoginInfo().getUnitId(), datas, acadyear, semester);
		logger.info("导入结束......");
		return jsonMsg;
	}

	@Override
	@RequestMapping("/evaluateRecord/template")
	public void downloadTemplate(HttpServletRequest request,
			HttpServletResponse response) {
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
        String classId = request.getParameter("classId");
		Clazz cls = SUtils.dc(classRemoteService.findOneById(classId), Clazz.class);
		String className = cls.getClassNameDynamic();
		List<Student> studentList = SUtils.dt(studentRemoteService.findByClassIds(new String[]{classId}), new TR<List<Student>>() {});
		int i = 1;
		for(Student stu : studentList){			
			HSSFRow rowTitle2 = sheet.createRow(i);
			for(int t=0;t<4;t++){
	        	HSSFCell cell = rowTitle2.createCell(t);
	        	String str = "";
	        	if(t==0){
	        		str = stu.getStudentName();
	        	}else if(t==1){
	        		str = stu.getStudentCode();
	        	}else if(t==2){
	        		str = className;
	        	}else{
	        		str = stu.getClassInnerCode();
	        	}
	            cell.setCellValue(new HSSFRichTextString(str));
	        }
            i++;
		}
        setDataValidation(titleList.toArray(new String[0]), 1, size, workbook, sheet);
        
		String fileName = "学生评价导入";
		ExportUtils.outputData(workbook, fileName, response);		
	}
	
	 private String[] letters = new String[]{"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
		
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
		
		public Map<String, String[]> getColMcodeMap(){
			Map<String, String[]> dmcodeMap = new HashMap<String, String[]>();
			String[] mcodes = {"DM-PYDJLB"};
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
			String[] cols = {"评语等级"};
			String[] colm = {"DM-PYDJLB"};
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
