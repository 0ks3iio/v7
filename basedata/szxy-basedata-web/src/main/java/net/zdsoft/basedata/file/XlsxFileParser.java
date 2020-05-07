/* 
 * @(#)XlsFileParser.java    Created on Apr 29, 2010
 * Copyright (c) 2010 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package net.zdsoft.basedata.file;

import java.io.FileInputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * @author zhaosf
 * @version $Revision: 1.0 $, $Date: Apr 29, 2010 9:57:30 AM $
 */
public class XlsxFileParser  {

	public  static final ImportFileParamOut parseFile(
			ImportFileParamIn inParam) throws Exception {
		ImportFileParamOut outParam = new ImportFileParamOut();

		String importFile = inParam.getImportFile();
		String xlsSheetName = inParam.getXlsSheetName();
		String xmlObjecDefine = inParam.getXmlObjecDefine();
		int beginRow = inParam.getBeginRow();// 开始解析行

		XSSFWorkbook wb = null;
		try {
			wb = new XSSFWorkbook(new FileInputStream(importFile));
			XSSFSheet sheet = null;
			if (org.apache.commons.lang3.StringUtils.isNotBlank(xlsSheetName)) {
				sheet = wb.getSheet(xlsSheetName);
			} 
			if(sheet == null){
                sheet = wb.getSheetAt(0);
            }
//			String objectDefine = null;
//			if(inParam.isHasTitle()){
//				XSSFCell tableCell = sheet.getRow(0).getCell(0);
//				if (tableCell != null) {
//					objectDefine = tableCell.getStringCellValue();
//				}
//				if (StringUtils.isBlank(objectDefine)) {
//					throw new Exception("-->导入文件格式错误，请确保第一行是导入数据类别信息！");
//				}
//	
//				if (!(xmlObjecDefine.equals(objectDefine))) {
//					throw new Exception("-->导入文件内容为“" + objectDefine + "”与当前模块不匹配。");
//				}
//			}	
			List<String> fieldList = new ArrayList<String>();
			String[] fields = null;// 字段
			List<String[]> rowDatas = new ArrayList<String[]>();// 行数据

			int cols = 0;
			// 以第二行的第一个为空的列，表示列内容结束。
			XSSFRow fieldRow = sheet.getRow(beginRow);
			XSSFCell fieldCell = null;
			int cells = fieldRow.getLastCellNum();
			for (int j = 0; j < cells; j++) {
//				String value = fieldRow.getCell(j).getStringCellValue().trim();
				fieldCell = fieldRow.getCell(j);
            	if(fieldCell == null)
            		break;
            	String value = fieldCell.getStringCellValue().trim();
				if (value.equals("")) {
					break;
				} else {
					fieldList.add(value);
				}
				cols++;
			}
			fields = fieldList.toArray(new String[0]);

			String value = "";
			boolean isAllNone = true;

			DataFormatter formatter = new DataFormatter();
			// i从1开始，是因为第0行是对象的名称，如教职工导入信息等。
			// 如果含有副标题，则第1行是副标题。

			int rows = sheet.getLastRowNum() + 1;// 以0开始
			if (rows == 0)
				rows = sheet.getPhysicalNumberOfRows();
			for (int i = beginRow + 1; i < rows; i++) {
				// 遇到所有列为空的行,中断读取数据
				isAllNone = true;
				XSSFRow row = sheet.getRow(i);
				String[] rowValues = new String[cols];
				if (null != row) {
					for (int j = 0; j < cols; j++) {
						XSSFCell cell = row.getCell(j);
						if (null != cell) {
							int type = cell.getCellType();
							switch (type) {
							case XSSFCell.CELL_TYPE_NUMERIC:
								if (DateUtil.isCellDateFormatted(cell)) {
									DateFormat sdf = new SimpleDateFormat(
											"yyyy-MM-dd");
									value = sdf.format(cell.getDateCellValue());
								} else {
									value = formatter.formatCellValue(cell);
								}
								break;
							default:
								value = cell.toString();
								break;
							}
						} else {
							value = "";
						}
						value = value.trim();
						rowValues[j] = value;
						if (!value.equals(""))
							isAllNone = false;
					}
				}
				// 如果这行的所有列都为空，则结束导入
				if (isAllNone)
					break;

				rowDatas.add(rowValues);
			}
			// wb.close();

//			outParam.setObjectDefine(objectDefine);
			outParam.setFields(fields);
			outParam.setRowDatas(rowDatas);
		} catch (Exception ex) {
			throw new Exception("文件解析错误，可能是格式不对，错误代码：" + ex.getMessage());
		} finally {
			if (wb != null) {
				// wb.close();
				wb = null;
			}
		}
		return outParam;
	}

}
