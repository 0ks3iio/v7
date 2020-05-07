package net.zdsoft.basedata.file;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;

import net.zdsoft.basedata.entity.ImportEntity;

public class ImportFileResult {

	public static final String FILE_SUCCESS = "0";
	public static final String FILE_ERROR = "1";
	public static final String FILE_INFO_ERROR = "2";

	public static void exportXLSFileResult(ImportFileParamOut paramOut,
			ImportEntity importEntity, String resultType) throws IOException {
		HSSFWorkbook workbook = new HSSFWorkbook();
		String[] fields = paramOut.getFields();
		List<String[]> rowDatas = paramOut.getRowDatas();
		// int i = 0;
		// Map.Entry entry = (Entry) iter.next();
		// String sheetName = (String) entry.getKey(); // key 工作表名称
		// List sheetRecords = (List) entry.getValue(); // value 工作表上的记录
		workbook.createSheet();
		workbook.setSheetName(0, " ");
		int rowIndex = 0;
		if (FILE_ERROR.equals(resultType)
				&& StringUtils.isNotBlank(paramOut.getErrorMsg())) {
			HSSFSheet sheetAt = workbook.getSheetAt(0);
			CellRangeAddress cra = new CellRangeAddress(0, 0, 0, fields.length-1);
			sheetAt.addMergedRegion(cra);
			HSSFRow row = sheetAt.createRow(rowIndex);
			// 合並單元格,下標從0開始
			// sheet.addMergedRegion(new Region(0,(short)0,0,(short)2));
			// HSSFRow rowT = sheet.createRow(0);
			// HSSFCell cellT = rowT.createCell((short)0);
			// cellT.setEncoding(HSSFCell.ENCODING_UTF_16);
			HSSFCellStyle titleStyle = workbook.createCellStyle();
			HSSFFont headfont = workbook.createFont();
			headfont.setFontName("宋体");
			headfont.setFontHeightInPoints((short) 14);// 字体大小
//			headfont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);// 加粗
			headfont.setBold(true);
			headfont.setColor(HSSFFont.COLOR_RED);
			titleStyle.setFont(headfont);
			HSSFCell cell = row.createCell(0);
			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			cell.setCellValue(paramOut.getErrorMsg());
			cell.setCellStyle(titleStyle);
			rowIndex++;
		}
		HSSFSheet sheetAt = workbook.getSheetAt(0);
		HSSFRow row = sheetAt.createRow(rowIndex);
		for (int j = 0; j < fields.length; j++) {
			sheetAt.setColumnWidth(j, 15 * 256);
		}
		HSSFCellStyle titleStyle = workbook.createCellStyle();
		HSSFFont titlefont = workbook.createFont();
		titlefont.setFontName("宋体");
		titlefont.setFontHeightInPoints((short) 12);// 字体大小
//		titlefont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);// 加粗
		titlefont.setBold(true);
		titleStyle.setFont(titlefont);
		for (int j = 0; j < fields.length; j++) {
			HSSFCell cell = row.createCell(j);
			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			cell.setCellValue(new HSSFRichTextString(fields[j]));
			cell.setCellStyle(titleStyle);
		}

		HSSFPatriarch patriarch = workbook.getSheetAt(0)
				.createDrawingPatriarch();
		List<Integer> indexs = paramOut.getIndexs();
		// 写入每条记录
		int rowNum = rowIndex + 1; // 行号
		for (int j = 0, m = rowDatas.size(); j < m; j++) {
			HSSFRow _row = workbook.getSheetAt(0).createRow(rowNum++);
			String[] dates = rowDatas.get(j);
			for (int k = 0; k < fields.length; k++) {
				HSSFCell cell = _row.createCell(k);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellValue(dates[k]);
				if(CollectionUtils.isNotEmpty(indexs)){
					if(indexs.get(j).intValue()==(k-1)){
						HSSFCellStyle cellStyle = workbook.createCellStyle();
						HSSFFont cellfont = workbook.createFont();
						cellfont.setColor(HSSFFont.COLOR_RED);
						cellStyle.setFont(cellfont);
						cell.setCellStyle(cellStyle);
					}
				}
			}
		}
		FileOutputStream resultFile;
		if(FILE_SUCCESS.equals(resultType)){
			resultFile = new FileOutputStream(importEntity.getFilePath()
					+ File.separator + importEntity.getId() + "_success."
					+ importEntity.getFileType());
		}else {
			resultFile = new FileOutputStream(importEntity.getFilePath()
					+ File.separator + importEntity.getId() + "_error."
					+ importEntity.getFileType());
		}
		workbook.write(resultFile);
		if(resultFile!=null)
			resultFile.close();
	}
	public static void main(String[] args) throws IOException {
		ImportFileParamOut paramOut = new ImportFileParamOut();
		paramOut.setFields(new String[] { "姓名", "身份证件号", "现学校", "现班级", "调入时间",
				"调入原因", "验证码", "操作人" });
		paramOut.setErrorMsg("xxxxxx");
		List<String[]> rowDatas = new ArrayList<String[]>();
		rowDatas.add(paramOut.getFields());
		rowDatas.add(paramOut.getFields());
		rowDatas.add(paramOut.getFields());
		paramOut.setRowDatas(rowDatas);
		ImportEntity importEntity = new ImportEntity();
		importEntity.setId("xx");
		importEntity.setFileType("xls");
		importEntity.setFilePath("d://");
		exportXLSFileResult(paramOut, importEntity, ImportFileResult.FILE_ERROR);
	}

}
