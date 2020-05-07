package net.zdsoft.datareport.data.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import net.zdsoft.basedata.dto.AttFileDto;
import net.zdsoft.basedata.entity.Attachment;
import net.zdsoft.basedata.remote.service.AttachmentRemoteService;
import net.zdsoft.datareport.data.constants.ReportConstants;
import net.zdsoft.datareport.data.entity.DataReportColumn;
import net.zdsoft.datareport.data.entity.DataReportInfo;
import net.zdsoft.datareport.data.entity.DataReportResults;
import net.zdsoft.datareport.data.entity.DataReportTitle;
import net.zdsoft.datareport.data.service.*;
import net.zdsoft.framework.config.Evn;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.DateUtils;
import net.zdsoft.framework.utils.MathUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.system.constant.Constant;
import net.zdsoft.system.remote.service.SysOptionRemoteService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.curator.shaded.com.google.common.collect.Lists;
import org.apache.curator.shaded.com.google.common.collect.Maps;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

@Service("dataReportTableService")
public class DataReportTableServiceImpl implements DataReportTableService{

	@Autowired
	private AttachmentRemoteService attachmentRemoteService;
	@Autowired
	private DataReportTitleService dataReportTitleService;
	@Autowired
	private DataReportColumnService dataReportColumnService;
	@Autowired
	private DataReportInfoService dataReportInfoService;
	@Autowired
	private DataReportResultsService dataReportResultsService;
	
	@Override
	public void createExcel(String infoId, String unitId, Integer tableType) throws Exception {
		File file = createExcelFile(infoId,tableType);
		String filePath = file.getPath();
		List<AttFileDto> dtoList = Lists.newArrayList();
		AttFileDto attFileDto = new AttFileDto();
		attFileDto.setFileName(file.getName());
		attFileDto.setFilePath(filePath.substring(filePath.indexOf("upload")));
		attFileDto.setObjectUnitId(unitId);
		attFileDto.setObjectType(ReportConstants.REPORT_INFO_TEMPLATE);
		attFileDto.setObjectId(infoId);
		dtoList.add(attFileDto);
		SUtils.dt(attachmentRemoteService.saveAttachment(SUtils.s(dtoList)),new TR<List<Attachment>>() {});
	}

	private File createExcelFile(String infoId, Integer tableType) throws Exception {
		List<DataReportColumn> rowColumns = dataReportColumnService.findByIdAndType(infoId, ReportConstants.REPORT_COLUMN_TYPE_1);
		List<DataReportColumn> rankColumns = dataReportColumnService.findByIdAndType(infoId, ReportConstants.REPORT_COLUMN_TYPE_2);
		List<DataReportTitle> titles = dataReportTitleService.findByReportId(infoId);
		
		HSSFWorkbook workbook = creatWorkbook(titles, tableType, true, rowColumns, rankColumns);
		String fileSystemPath = Evn.<SysOptionRemoteService> getBean(
				"sysOptionRemoteService").findValue(Constant.FILE_PATH);// 文件系统地址
		
		String filePath = fileSystemPath + File.separator + "upload" + File.separator + 
				DateUtils.date2String(new Date(), "yyyyMMdd") + File.separator + UuidUtils.generateUuid();
		File dir = new File(filePath);
		if (!dir.exists()) {     // 目录不存在则创建
			dir.mkdirs();
		}
		File file = new File(filePath + File.separator + UuidUtils.generateUuid() + ".xls");
		file.createNewFile();
		FileOutputStream stream= FileUtils.openOutputStream(file);
		workbook.write(stream);
	    stream.close();
		return file;
	}	
	
	private HSSFWorkbook creatWorkbook(List<DataReportTitle> titles, Integer tableType, boolean needRemark, List<DataReportColumn> rowColumns, List<DataReportColumn> rankColumns) {
		String remark = "";
		List<String> headers = titles.stream().filter(tit->Objects.equals(ReportConstants.TITLE_TYPE_1, tit.getType())).
				sorted((t1,t2) -> t1.getOrderIndex().compareTo(t2.getOrderIndex())).map(tit->tit.getContent()).collect(Collectors.toList());
		Optional<DataReportTitle> remarkTitle = titles.stream().filter(tit->Objects.equals(ReportConstants.TITLE_TYPE_3, tit.getType())).findFirst();
		if (remarkTitle.isPresent()) {
			remark = remarkTitle.get().getContent();
		}
		
		HSSFWorkbook workbook=new HSSFWorkbook();
		HSSFSheet sheet=workbook.createSheet();
		
		//设置行宽
		int rowWidthSize = 8;
		if (Objects.equals(ReportConstants.REPORT_TABLE_TYPE_1, tableType)) {
			rowWidthSize = rowColumns.size();
		}
		if (Objects.equals(ReportConstants.REPORT_TABLE_TYPE_3, tableType)) {
			rowWidthSize = rowColumns.size() + 1;
		}
		for(int i=0;i<rowWidthSize;i++){
			sheet.setColumnWidth(i, (int)(18 + 0.72) * 256);
		}
		
		HSSFRow hssfRow = null;
		HSSFCell hssfCell = null;
		int hssfRowIndex = headers.size();
		excelTitle(workbook,sheet,rowWidthSize,headers,hssfRow,hssfCell);
        
		HSSFCellStyle colStyle = cellStyle(workbook, true, HorizontalAlignment.CENTER, true, font(workbook, (short)11, true));
		HSSFCellStyle contStyle = cellStyle(workbook, true, HorizontalAlignment.CENTER, true, font(workbook, (short)11, false));
		//Excel列名行
		if (Objects.equals(ReportConstants.REPORT_TABLE_TYPE_1, tableType) || Objects.equals(ReportConstants.REPORT_TABLE_TYPE_3, tableType)) {
			hssfRow = sheet.createRow(hssfRowIndex);
			hssfRow.setHeightInPoints(2*sheet.getDefaultRowHeightInPoints());
			int rowIndex = 0; 
			if (Objects.equals(ReportConstants.REPORT_TABLE_TYPE_3, tableType)) {
				rowIndex = 1;
				hssfCell = hssfRow.createCell(0);
				hssfCell.setCellStyle(colStyle);
			}
			for (DataReportColumn column : rowColumns) {
				hssfCell = hssfRow.createCell(rowIndex);
				if (Objects.equals(column.getIsNotnull(), 1)) {
					hssfCell.setCellValue("* " + column.getColumnName());
				} else {
					hssfCell.setCellValue(column.getColumnName());
				}
				hssfCell.setCellStyle(colStyle);
				rowIndex++;
			}
			if (Objects.equals(ReportConstants.REPORT_TABLE_TYPE_1, tableType)) {
				for (int i = hssfRowIndex+1;i<hssfRowIndex+6;i++) {
		        	hssfRow=sheet.createRow(i);
		        	hssfRow.setHeightInPoints(1.5f*sheet.getDefaultRowHeightInPoints());
					for (int j = 0; j < rowColumns.size(); j++) {
						hssfCell = hssfRow.createCell(j);
						hssfCell.setCellStyle(contStyle);
					}
		        }
				hssfRowIndex += 6;
			}
		}
		
		if (Objects.equals(ReportConstants.REPORT_TABLE_TYPE_2, tableType) || Objects.equals(ReportConstants.REPORT_TABLE_TYPE_3, tableType)) {
			if (Objects.equals(ReportConstants.REPORT_TABLE_TYPE_3, tableType)) {
				hssfRowIndex++;
			}
			for (DataReportColumn column : rankColumns) {
				hssfRow = sheet.createRow(hssfRowIndex);
				hssfRow.setHeightInPoints(2*sheet.getDefaultRowHeightInPoints());
				hssfCell = hssfRow.createCell(0);
				if (Objects.equals(column.getIsNotnull(), 1)) {
					hssfCell.setCellValue("* " + column.getColumnName());
				} else {
					hssfCell.setCellValue(column.getColumnName());
				}
				hssfCell.setCellStyle(colStyle);
				for (int i=1;i<rowWidthSize;i++) {
					hssfCell = hssfRow.createCell(i);
					hssfCell.setCellStyle(contStyle);
				}
				hssfRowIndex++;
			}
		}
        
        if (StringUtils.isNotEmpty(remark)&&needRemark) {
        	hssfRowIndex+=2;
        	if (rowWidthSize > 1) {
        		sheet.addMergedRegion(new CellRangeAddress(hssfRowIndex,hssfRowIndex,0,rowWidthSize-1));
        	}
        	hssfRow = sheet.createRow(hssfRowIndex);
        	hssfRow.setHeightInPoints(2*sheet.getDefaultRowHeightInPoints());
        	hssfCell = hssfRow.createCell(0);
        	hssfCell.setCellValue("备注："+remark);
        }
		return workbook;
	}
	
	private void excelTitle (HSSFWorkbook workbook, HSSFSheet sheet, int rowWidthSize, 
			List<String> headers, HSSFRow hssfRow, HSSFCell hssfCell) {
		if (rowWidthSize > 1) {
			for (int i=0;i<headers.size();i++) {
				sheet.addMergedRegion(new CellRangeAddress(i,i,0,rowWidthSize-1));
			}
		}
		RichTextString richStr = null;
		HorizontalAlignment alignment = HorizontalAlignment.LEFT;
		String titleStr = "";
		String textStr = "";
		String[] textStrs = null;
		String subString = "";
		int starStr = 0;
		int endStr = 0;
		List<String[]> strList = null;
		String[] strs = null;
		for (int i=0;i<headers.size();i++) {
			titleStr = headers.get(i).replaceAll("&nbsp; ", " ");
			textStrs = titleStr.split("</?[^>]+>");
			textStr = titleStr.replaceAll("</?[^>]+>", "");
			hssfRow = sheet.createRow(i);
			hssfCell = hssfRow.createCell(0);
			if (titleStr.indexOf("style=\"text-align: center;\"") > 0) {
				alignment = HorizontalAlignment.CENTER;
			}
			if (titleStr.indexOf("style=\"text-align: right;\"") > 0) {
				alignment = HorizontalAlignment.RIGHT;
			}
			hssfCell.setCellStyle(cellStyle(workbook, false, alignment, false, null));
			richStr = new HSSFRichTextString(textStr);
			strList = Lists.newArrayList();
			for (String str : textStrs) {
				if (StringUtils.isNotEmpty(str)) {
					strs = new String[3];
					subString = titleStr.substring(0, titleStr.indexOf(str));
					while (true) {
						starStr = subString.lastIndexOf("<span");
						endStr = subString.indexOf("</span",starStr);
						if (starStr==-1 && endStr==-1) {
							strs[0] = str; 
							strs[1] = 16 + "";
							break;
						} else if (starStr!=-1 && endStr==-1) {
							strs[0] = str; 
							strs[1] = subString.substring(starStr+24,subString.indexOf("px",starStr));
							break;
						} else {
							subString = subString.replace(subString.substring(starStr, endStr+7), "");
						}
					}
					subString = titleStr.substring(0, titleStr.indexOf(str));
					while (true) {
						starStr = subString.lastIndexOf("<strong");
						endStr = subString.indexOf("</strong",starStr);
						if (starStr==-1 && endStr==-1) {
							strs[2] = "false";
							break;
						} else if (starStr!=-1 && endStr==-1) {
							strs[2] = "true";
							break;
						} else {
							subString = subString.replace(subString.substring(starStr, endStr+9), "");
						}
					}
					strList.add(strs);
				}
			}
			for (String[] str : strList) {
				richStr.applyFont(textStr.indexOf(str[0]), textStr.indexOf(str[0]) + str[0].length(), font(workbook, Short.parseShort(str[1]), Boolean.parseBoolean(str[2])));
			}
			hssfCell.setCellValue(richStr);
		}
	}
	
	private HSSFCellStyle cellStyle (HSSFWorkbook workbook,boolean haveTopAndBot,
			HorizontalAlignment alignment,boolean needBr,Font font) {
		HSSFCellStyle style = workbook.createCellStyle();
		style.setFillForegroundColor(IndexedColors.WHITE.getIndex());
		if (haveTopAndBot) {
			style.setBorderTop(BorderStyle.THIN);//上边框  
			style.setBorderBottom(BorderStyle.THIN); //下边框  
		}
		style.setBorderLeft(BorderStyle.THIN);//左边框  
		style.setBorderRight(BorderStyle.THIN);//右边框  
		if (alignment!=null) {
			style.setAlignment(alignment);//水平
		}
		style.setVerticalAlignment(VerticalAlignment.CENTER);//垂直
		if (needBr) {
			style.setWrapText(true);//自动换行
		}
		style.setFillForegroundColor(IndexedColors.WHITE1.getIndex());
		if (font!=null) {
			style.setFont(font);
		}
		return style;
	}
	
	private Font font (HSSFWorkbook workbook,short fontSize,boolean needBold) {
		Font headfont = workbook.createFont();
		if (!Objects.equals(fontSize, (short)0)) {
			headfont.setFontHeightInPoints(fontSize);
		}
		headfont.setFontName("宋体");  
		if (needBold) {
			headfont.setBold(true);
		}
		return headfont;
	}
		
	@Override
	public void exportExcel(String infoId, String taskId, Integer tableType , Integer type) throws Exception{
		List<DataReportColumn> rowColumns = null;
		List<DataReportColumn> rankColumns = null;
		int colSize = 0;
		boolean needStat = false;
		if (Objects.equals(ReportConstants.REPORT_TABLE_TYPE_1, tableType)) {
			rowColumns = dataReportColumnService.findByIdAndType(infoId, ReportConstants.REPORT_COLUMN_TYPE_1);
			colSize = rowColumns.size();
			needStat = rowColumns.stream().anyMatch(data -> data.getMethodType() != null);
		} 
		if (Objects.equals(ReportConstants.REPORT_TABLE_TYPE_2, tableType)) {
			rankColumns = dataReportColumnService.findByIdAndType(infoId, ReportConstants.REPORT_COLUMN_TYPE_2);
			colSize = rankColumns.size();
			needStat = rankColumns.stream().anyMatch(data -> data.getMethodType() != null);
		}
		if (Objects.equals(ReportConstants.REPORT_TABLE_TYPE_3, tableType)) {
			rowColumns = dataReportColumnService.findByIdAndType(infoId, ReportConstants.REPORT_COLUMN_TYPE_1);
			rankColumns = dataReportColumnService.findByIdAndType(infoId, ReportConstants.REPORT_COLUMN_TYPE_2);
			colSize = rowColumns.size();
			needStat = rowColumns.stream().anyMatch(data -> data.getMethodType() != null);
		}
		List<DataReportResults> dataReportResults = null;
		DataReportResults sumResult = null;
		if (Objects.equals(type, 1)) {
			dataReportResults = dataReportResultsService.findByTaskId(taskId);
			if (needStat) {
				sumResult = dataReportResults.stream().filter(data->Objects.equals(data.getType(), ReportConstants.RESULT_TYPE_2)).findFirst().get();
				dataReportResults.remove(sumResult);
			}
		} else {
			dataReportResults = dataReportResultsService.findByInfoIdAndType(infoId,ReportConstants.RESULT_TYPE_1);
			Map<String,List<DataReportResults>> dataMap = dataReportResults.stream().collect(Collectors.groupingBy(DataReportResults::getTaskId));
			dataReportResults = Lists.newArrayList();
			for (Map.Entry<String,List<DataReportResults>> entry : dataMap.entrySet()) {
				dataReportResults.addAll(entry.getValue());
			}
		}
		List<DataReportTitle> titles = dataReportTitleService.findByReportId(infoId);
		int titleSize = (int) titles.stream().filter(tit->Objects.equals(ReportConstants.TITLE_TYPE_1, tit.getType())).count();
		HSSFWorkbook workbook = creatWorkbook(titles, tableType, false, rowColumns, rankColumns);
		HSSFSheet sheet=workbook.getSheetAt(0);
		HSSFRow hssfRow = null;
		HSSFCell hssfCell = null;
		Double[] stats = null;
		if (Objects.equals(type, 2)&&needStat) {
			stats = new Double[colSize];
			for (int i=0;i<colSize;i++) {
				stats[i] = 0.0;
			}
		}
		String[] resultStr = null;
		HSSFCellStyle contStyle = cellStyle(workbook, true, HorizontalAlignment.CENTER, true, font(workbook, (short)11, false));
		if (Objects.equals(ReportConstants.REPORT_TABLE_TYPE_1, tableType)) {
			for (int i=0;i<dataReportResults.size();i++) {
				hssfRow=sheet.createRow(i+titleSize+1);
				hssfRow.setHeightInPoints(1.5f*sheet.getDefaultRowHeightInPoints());
				resultStr = JSONArray.parseArray(dataReportResults.get(i).getData()).toArray(new String[colSize]);
				for (int j=0;j<colSize;j++) {
					hssfCell = hssfRow.createCell(j);
					if (Objects.equals(rowColumns.get(j).getColumnType(), 3)) {
						hssfCell.setCellValue(i+1);
					} else {
						if (!Objects.equals(resultStr[j], "null")) {
							hssfCell.setCellValue(resultStr[j]);
						}
					}
					hssfCell.setCellStyle(contStyle);
					if (Objects.equals(type, 2)) {
						if (needStat && !Objects.equals(rowColumns.get(j).getMethodType(), null) && !Objects.equals(resultStr[j], "null")) {
							stats[j] = MathUtils.add(stats[j], Double.valueOf(resultStr[j]));
						}
					}
				}
			}
			
			if (needStat) {
				if (Objects.equals(type, 1)) {
					hssfRow=sheet.createRow(dataReportResults.size()+titleSize+1);
					hssfRow.setHeightInPoints(1.5f*sheet.getDefaultRowHeightInPoints());
					String[] sumResults = JSONArray.parseArray(sumResult.getData()).toArray(new String[colSize]);
					for (int i=0;i<sumResults.length;i++) {
						hssfCell = hssfRow.createCell(i);
						if (Objects.equals(rowColumns.get(i).getMethodType(), 1)) {
							hssfCell.setCellValue("取平均：" + sumResults[i]);
						}
						if (Objects.equals(rowColumns.get(i).getMethodType(), 2)) {
							hssfCell.setCellValue("求和：" + sumResults[i]);
						}
						hssfCell.setCellStyle(contStyle);
					}
				} else {
					hssfRow=sheet.createRow(dataReportResults.size()+titleSize+1);
					hssfRow.setHeightInPoints(1.5f*sheet.getDefaultRowHeightInPoints());
					for (int i=0;i<colSize;i++) {
						hssfCell = hssfRow.createCell(i);
						if (Objects.equals(rowColumns.get(i).getMethodType(), 1)) {
							hssfCell.setCellValue("取平均：" + MathUtils.divString(stats[i], dataReportResults.size(),2));
						}
						if (Objects.equals(rowColumns.get(i).getMethodType(), 2)) {
							hssfCell.setCellValue("求和：" + MathUtils.roundString(stats[i], 2));
						}
						hssfCell.setCellStyle(contStyle);
					}
				}
			}
		}
		
		if (Objects.equals(ReportConstants.REPORT_TABLE_TYPE_2, tableType)) {
			List<String[]> resultList = Lists.newArrayList();
			for (int i=0;i<colSize;i++) {
				resultList.add(new String[dataReportResults.size()]);
			}
			for (int i=0;i<dataReportResults.size();i++) {
				resultStr = JSONArray.parseArray(dataReportResults.get(i).getData()).toArray(new String[colSize]);
				for (int j=0;j<colSize;j++) {
					resultList.get(j)[i] = resultStr[j];
				}
			}
			for (int i=0;i<colSize;i++) {
				hssfRow=sheet.getRow(i+titleSize);
				resultStr = resultList.get(i);
				for (int j=0;j<resultStr.length;j++) {
					hssfCell = hssfRow.createCell(j+1);
					hssfCell.setCellStyle(contStyle);
					if (Objects.equals(rankColumns.get(i).getColumnType(), 3)) {
						hssfCell.setCellValue(j+1);
					} else {
						if (!Objects.equals(resultStr[j], "null")) {
							hssfCell.setCellValue(resultStr[j]);
						}
					}
					if (Objects.equals(type, 2)) {
						if (needStat && !Objects.equals(rankColumns.get(i).getMethodType(), null) && !Objects.equals(resultStr[j], "null")) {
							stats[i] = MathUtils.add(stats[i], Double.valueOf(resultStr[j]));
						}
					}
				}
			}
			
			if (needStat) {
				if (Objects.equals(type, 1)) {
					String[] sumResults = JSONArray.parseArray(sumResult.getData()).toArray(new String[colSize]);
					for (int i=0;i<colSize;i++) {
						hssfRow=sheet.getRow(i+titleSize);
						hssfCell = hssfRow.createCell(resultList.get(i).length+1);
						if (Objects.equals(rankColumns.get(i).getMethodType(), 1)) {
							hssfCell.setCellValue("取平均：" + sumResults[i]);
						}
						if (Objects.equals(rankColumns.get(i).getMethodType(), 2)) {
							hssfCell.setCellValue("求和：" + sumResults[i]);
						}
						hssfCell.setCellStyle(contStyle);
					}
				} else {
					for (int i=0;i<colSize;i++) {
						hssfRow=sheet.getRow(i+titleSize);
						hssfCell = hssfRow.createCell(resultList.get(i).length+1);
						if (Objects.equals(rankColumns.get(i).getMethodType(), 1)) {
							hssfCell.setCellValue("取平均：" + MathUtils.divString(stats[i], resultList.get(i).length,2));
						}
						if (Objects.equals(rankColumns.get(i).getMethodType(), 2)) {
							hssfCell.setCellValue("求和：" + MathUtils.roundString(stats[i], 2));
						}
						hssfCell.setCellStyle(contStyle);
					}
				}
			}
		}
		
		if (Objects.equals(ReportConstants.REPORT_TABLE_TYPE_3, tableType)) {
			for (int i=0;i<dataReportResults.size();i++) {
				hssfRow=sheet.getRow(i+titleSize+1);
				hssfRow.setHeightInPoints(1.5f*sheet.getDefaultRowHeightInPoints());
				resultStr = JSONArray.parseArray(dataReportResults.get(i).getData()).toArray(new String[colSize]);
				for (int j=0;j<colSize;j++) {
					hssfCell = hssfRow.createCell(j+1);
					if (Objects.equals(rowColumns.get(j).getColumnType(), 3)) {
						hssfCell.setCellValue(i+1);
					} else {
						if (!Objects.equals(resultStr[j], "null")) {
							hssfCell.setCellValue(resultStr[j]);
						}
					}
					hssfCell.setCellStyle(contStyle);
				}
			}
			
			if (needStat) {
				hssfRow=sheet.createRow(dataReportResults.size()+titleSize+1);
				hssfRow.setHeightInPoints(1.5f*sheet.getDefaultRowHeightInPoints());
				String[] sumResults = JSONArray.parseArray(sumResult.getData()).toArray(new String[colSize]);
				hssfCell = hssfRow.createCell(0);
				hssfCell.setCellValue("统计");
				hssfCell.setCellStyle(contStyle);
				for (int i=0;i<sumResults.length;i++) {
					hssfCell = hssfRow.createCell(i+1);
					if (Objects.equals(rowColumns.get(i).getMethodType(), 1)) {
						hssfCell.setCellValue("取平均：" + sumResults[i]);
					}
					if (Objects.equals(rowColumns.get(i).getMethodType(), 2)) {
						hssfCell.setCellValue("求和：" + sumResults[i]);
					}
					hssfCell.setCellStyle(contStyle);
				}
			}
		}
		
		String fileSystemPath = Evn.<SysOptionRemoteService> getBean(
				"sysOptionRemoteService").findValue(Constant.FILE_PATH);// 文件系统地址
		
		String filePath = fileSystemPath + File.separator + "upload" + File.separator + 
				DateUtils.date2String(new Date(), "yyyyMMdd") + File.separator + UuidUtils.generateUuid();
		File dir = new File(filePath);
		if (!dir.exists()) {     // 目录不存在则创建
			dir.mkdirs();
		}
		File file = new File(filePath + File.separator + UuidUtils.generateUuid() + ".xls");
		file.createNewFile();
		FileOutputStream stream= FileUtils.openOutputStream(file);
		workbook.write(stream);
		stream.flush();
		stream.close();
		DataReportInfo info = dataReportInfoService.findOne(infoId);
		List<AttFileDto> dtoList = Lists.newArrayList();
		AttFileDto attFileDto = new AttFileDto();
		attFileDto.setFileName(file.getName());
		attFileDto.setFilePath(file.getPath().substring(file.getPath().indexOf("upload")));
		attFileDto.setObjectUnitId(info.getUnitId());
		if (Objects.equals(type, 1)) {
			attFileDto.setObjectType(ReportConstants.REPORT_TASK_ATT);
			attFileDto.setObjectId(taskId);
		} else {
			attFileDto.setObjectType(ReportConstants.REPORT_INFO_STATS);
			attFileDto.setObjectId(infoId);
		}
		dtoList.add(attFileDto);
		SUtils.dt(attachmentRemoteService.saveAttachment(SUtils.s(dtoList)),new TR<List<Attachment>>() {});
	}

	@Override
	public String saveAttFiles(List<AttFileDto> fileDtos, String taskId,String unitId) throws Exception {
		Map<String,String> fileNameMap = Maps.newHashMap();
		Set<String> fns = new HashSet<>();
		Iterator<AttFileDto> it = fileDtos.iterator();
		while (it.hasNext()) {
			AttFileDto fileDto = it.next();
			if (fns.contains(fileDto.getFileName())) {
				it.remove();
				continue;
			}
			fns.add(fileDto.getFileName());
			fileDto.setObjectType(ReportConstants.REPORT_TASK_ACCESSORY);
			fileDto.setObjectId(taskId);
			fileDto.setObjectUnitId(unitId);
		}
		List<Attachment> attachments = SUtils.dt(attachmentRemoteService.findAttachmentDirPathByObjTypeAndId(ReportConstants.REPORT_TASK_ACCESSORY,taskId),new TR<List<Attachment>>(){});
		List<String> toDels = new ArrayList<>();
		for (Attachment at : attachments) {
			if (fns.contains(at.getFilename())) {
				toDels.add(at.getId());
			} else {
				fileNameMap.put(at.getId(), at.getFilename());
				fns.add(at.getFilename());
			}
		}
		if(toDels.size() > 0){
			attachmentRemoteService.deleteAttachments(toDels.toArray(new String[0]), null);
		}
		if (CollectionUtils.isNotEmpty(fileDtos)) {
			List<Attachment> attSaveFiles = SUtils.dt(attachmentRemoteService.saveAttachment(SUtils.s(fileDtos)),new TR<List<Attachment>>(){});
			for (Attachment att : attSaveFiles) {
				fileNameMap.put(att.getId(), att.getFilename());
			}
			return JSONObject.toJSONString(fileNameMap);
		} else {
			return "";
		}
	}

	@Override
	public void saveNewStructure() throws Exception{
		List<DataReportResults> results = dataReportResultsService.findAll();
		if (CollectionUtils.isNotEmpty(results)) {
			List<DataReportColumn> columns = dataReportColumnService.findAll();
			Map<String,Long> colSizeMap = columns.stream().collect(Collectors.groupingBy(DataReportColumn::getReportId,Collectors.counting()));
			Long colSize = 0L;
			Method method;
			JSONArray array = null;
			String dataStr = "";
			for (DataReportResults result : results) {
				colSize = colSizeMap.get(result.getReportId());
				array = new JSONArray();
				for (int i=0;i<colSize;i++) {
					method = result.getClass().getMethod("getData"+ (i + 1), new Class[]{});
					dataStr = (String) method.invoke(result, new Object[]{});
					if (StringUtils.isNotBlank(dataStr)) {
						if (Objects.equals(result.getType(), ReportConstants.RESULT_TYPE_2)) {
							array.add(dataStr.split("：")[1]);
						} else {
							array.add(dataStr); 
						}
					} else {
						array.add("null"); 
					}
				}
				result.setData(array.toJSONString());
			}
			List<DataReportResults> newResults = results;
			dataReportResultsService.saveAll(newResults.toArray(new DataReportResults[newResults.size()]));
		}
	}
}
