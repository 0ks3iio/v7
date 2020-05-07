package net.zdsoft.newgkelective.data.optaplanner.util;

import java.text.DecimalFormat;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.formula.eval.ErrorEval;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;

public class POIUtils {
    private static final DecimalFormat df = new DecimalFormat("#.#############");
	public static void setValue(Sheet sheet, int rowIndex, int colIndex, Object value){
		if(value instanceof String){
			setStringVal(sheet, rowIndex, colIndex, (String)value);
		}else if(value instanceof Double){
			setDoubleVal(sheet, rowIndex, colIndex, (Double)value);
		}else if(value instanceof Long){
			setLongVal(sheet, rowIndex, colIndex, (Long)value);
		}else if(value instanceof Integer){
			setIntVal(sheet, rowIndex, colIndex, (Integer)value);
		}
	}
	
	public static String getValue(HSSFSheet sheet, int rowIndex, int colIndex) {
		Cell cell = getCell(sheet, rowIndex, colIndex);
		switch (cell.getCellType()) {
		case HSSFCell.CELL_TYPE_BLANK:
			return "";
		case HSSFCell.CELL_TYPE_STRING:
			return getStringVal(sheet, rowIndex, colIndex);
		case HSSFCell.CELL_TYPE_BOOLEAN:
			return String.valueOf(cell.getBooleanCellValue());
		case HSSFCell.CELL_TYPE_FORMULA:
			FormulaEvaluator evaluator = sheet.getWorkbook().getCreationHelper().createFormulaEvaluator();
			CellValue cellValue = evaluator.evaluate(cell);
			switch (cellValue.getCellType()) {
			case Cell.CELL_TYPE_NUMERIC:
				return String.valueOf(Math.round(cellValue.getNumberValue()));
			case Cell.CELL_TYPE_STRING:
				return cellValue.getStringValue();
			case Cell.CELL_TYPE_BOOLEAN:
				return String.valueOf(cellValue.getBooleanValue());
			case Cell.CELL_TYPE_ERROR:
				return ErrorEval.getText(cellValue.getErrorValue());
			}
		case HSSFCell.CELL_TYPE_NUMERIC:
			if (HSSFDateUtil.isCellDateFormatted(cell)){
				return cell.getDateCellValue().toString();
			} else {
				return df.format(getDoubleVal(sheet, rowIndex, colIndex));
			}
		default:
			return getStringVal(sheet, rowIndex, colIndex);
		}
	}
	
	public static String getStringVal(Sheet sheet, int rowIndex, int colIndex){
		return getCell(sheet, rowIndex, colIndex).getStringCellValue();
	}
	
	public static void setStringVal(Sheet sheet, int rowIndex, int colIndex, String value){
		getCell(sheet, rowIndex, colIndex).setCellValue(value);
	}
	
	public static Double getDoubleVal(Sheet sheet, int rowIndex, int colIndex){
		return getCell(sheet, rowIndex, colIndex).getNumericCellValue();
	}
	
	public static void setDoubleVal(Sheet sheet, int rowIndex, int colIndex, Double value){
		getCell(sheet, rowIndex, colIndex).setCellValue(value);
	}
	
	public static Long getLongVal(Sheet sheet, int rowIndex, int colIndex){
		return (long)getCell(sheet, rowIndex, colIndex).getNumericCellValue();
	}
	
	public static void setLongVal(Sheet sheet, int rowIndex, int colIndex, Long value){
		getCell(sheet, rowIndex, colIndex).setCellValue(value.doubleValue());
	}
	
	public static Integer getIntVal(Sheet sheet, int rowIndex, int colIndex){
		return (int)getCell(sheet, rowIndex, colIndex).getNumericCellValue();
	}
	
	public static void setIntVal(Sheet sheet, int rowIndex, int colIndex, Integer value){
		
		getCell(sheet, rowIndex, colIndex).setCellValue(value.doubleValue());
	}
	
	public static Row getRow(Sheet sheet, int rowIndex){
		
		if (sheet instanceof HSSFSheet) {
			HSSFSheet sheetA = (HSSFSheet) sheet;
			HSSFRow row = sheetA.getRow(rowIndex);
			if(row == null){
				row = sheetA.createRow(rowIndex);
			}
			return row;
		} else {
			XSSFSheet sheetA = (XSSFSheet) sheet;
			XSSFRow row = sheetA.getRow(rowIndex);
			if(row == null){
				row = sheetA.createRow(rowIndex);
			}
			return row;
			
		}
		
	}
	
	public static Cell getCell(Row row, int colIndex){
		if (row instanceof HSSFRow) {
			HSSFRow rowA = (HSSFRow) row;
			HSSFCell cell = rowA.getCell(colIndex);
			if(cell == null){
				cell = rowA.createCell(colIndex);
			}
			return cell;
		} else {
			XSSFRow rowA = (XSSFRow) row;
			XSSFCell cell = rowA.getCell(colIndex);
			if(cell == null){
				cell = rowA.createCell(colIndex);
			}
			return cell;
		}
	}
	
	public static Cell getCell(Sheet sheet, int rowIndex, int colIndex){
		Row row = getRow(sheet, rowIndex);
		return getCell(row, colIndex);
	}
}
