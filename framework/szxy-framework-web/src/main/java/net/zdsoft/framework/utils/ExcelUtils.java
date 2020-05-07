package net.zdsoft.framework.utils;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class ExcelUtils {
    /**
     * 读取excel中的数据
     * @param path
     * @return List<String   [   ]>
     */
    public static List<String[]> readExcel(String path, int totalRow) {
        if (totalRow <= 0) {
            return new ArrayList<String[]>();
        }
        if (path != null && !path.equals("")) {
            String ext = getExt(path);
            if (ext != null && !ext.equals("")) {
                if (ext.equals("xls")) {
                    return readXls(path, 1, totalRow);
                } else if (ext.equals("xlsx")) {
                    return readXlsx(path, 1, totalRow);
                }
            }
        }
        return new ArrayList<String[]>();
    }

    /**
     * 读取excel中的数据
     * 动态获取说明可有可无
     * @param path
     * @return List<String   [   ]>
     */
    public static List<String[]> readExcelIgnoreDesc(String path, int totalRow) {
        if (totalRow <= 0) {
            return new ArrayList<String[]>();
        }
        if (path != null && !path.equals("")) {
            String ext = getExt(path);
            if (ext != null && !ext.equals("")) {
                if (ext.equals("xls")) {
                    return readXls(path, -1, totalRow);
                } else if (ext.equals("xlsx")) {
                    return readXlsx(path, -1, totalRow);
                }
            }
        }
        return new ArrayList<String[]>();
    }

    /**
     * 读取excel中的数据
     * 精确指定行数
     * @param path
     * @return List<String   [   ]>
     */
    public static List<String[]> readExcelByRow(String path, int startRow,
                                                int totalCell) {
        if (totalCell <= 0) {
            return new ArrayList<String[]>();
        }
        if (path != null && !path.equals("")) {
            String ext = getExt(path);
            if (ext != null && !ext.equals("")) {
                if (ext.equals("xls")) {
                    return readXls(path, startRow, totalCell);
                } else if (ext.equals("xlsx")) {
                    return readXlsx(path, startRow, totalCell);
                }
            }
        }
        return new ArrayList<String[]>();
    }

    /**
     * 读取后缀为xls的excel文件的数据
     *
     * @param path
     * @return List<String   [   ]>
     */
    private static List<String[]> readXls(String path, int startRow,
                                          int totalCell) {

        HSSFWorkbook hssfWorkbook = null;
        try {
            InputStream is = new FileInputStream(path);
            hssfWorkbook = new HSSFWorkbook(is);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 若startRow为-1，判断是否首行是否存在合并单元格
        if (startRow == -1) {
            Sheet sheet = hssfWorkbook.getSheetAt(0);
            int mergeCount = sheet.getNumMergedRegions();
            if (mergeCount == 0) {
                startRow = 0;
            } else {
                startRow = 1;
            }
        }

        List<String[]> list = new ArrayList<String[]>();
        if (hssfWorkbook != null) {
            // Read the Sheet
            HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(0);
            if (hssfSheet == null) {
                return list;
            }
            // Read the Row
            for (int rowNum = startRow; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
                HSSFRow hssfRow = hssfSheet.getRow(rowNum);
                if (hssfRow != null) {
                    boolean hasValue = false;
                    String[] data = new String[totalCell];
                    for (int i = 0; i < totalCell; i++) {
                        Cell cell = hssfRow.getCell(i);
                        data[i] = getCellValue(cell);
                        if (StringUtils.isNotBlank(data[i]))
                            hasValue = true;
                    }
                    if (hasValue)
                        list.add(data);
                }
            }
        }
        return list;
    }
    
	private static List<String[]> readXlsByRows(String path, int startRow, int endRowExclusive) {
		HSSFWorkbook hssfWorkbook = null;
		try {
			InputStream is = new FileInputStream(path);
			hssfWorkbook = new HSSFWorkbook(is);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

        // 若startRow为-1，判断是否首行是否存在合并单元格
        if (startRow == -1) {
            Sheet sheet = hssfWorkbook.getSheetAt(0);
            int mergeCount = sheet.getNumMergedRegions();
            if (mergeCount == 0) {
                startRow = 0;
            } else {
                startRow = 1;
            }
        }

		List<String[]> list = new ArrayList<String[]>();
		if (hssfWorkbook != null) {
			HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(0);
			if (hssfSheet == null) {
				return list;
			}
			int cellCount = 0;
			for (int rowNum = startRow; rowNum < endRowExclusive; rowNum++) {
				HSSFRow hssfRow = hssfSheet.getRow(rowNum);
				if(cellCount == 0) {
					cellCount = hssfRow.getLastCellNum();
				}
				if (hssfRow != null) {
					boolean hasValue = false;
					String[] data = new String[cellCount];
					for (int i = 0; i < cellCount; i++) {
						Cell cell = hssfRow.getCell(i);
						data[i] = getCellValue(cell);
						if (StringUtils.isNotBlank(data[i]))
							hasValue = true;
					}
					if (hasValue)
						list.add(data);
				}
			}
		}
		return list;
	}

    /**
     * 根据excel单元格类型获取excel单元格值
     *
     * @param cell
     * @return nizq
     */
    private static String getCellValue(Cell cell) {
        String cellvalue = "";
        if (cell != null) {
            // 判断当前Cell的Type
            switch (cell.getCellType()) {
                // 如果当前Cell的Type为NUMERIC
                case HSSFCell.CELL_TYPE_NUMERIC: {
                    short format = cell.getCellStyle().getDataFormat();
                    if (format == 14 || format == 31 || format == 57
                            || format == 58) { // excel中的时间格式
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        double value = cell.getNumericCellValue();
                        Date date = DateUtil.getJavaDate(value);
                        cellvalue = sdf.format(date);
                    }
                    // 判断当前的cell是否为Date
                    else if (HSSFDateUtil.isCellDateFormatted(cell)) { // 先注释日期类型的转换，在实际测试中发现HSSFDateUtil.isCellDateFormatted(cell)只识别2014/02/02这种格式。
                        // 如果是Date类型则，取得该Cell的Date值 // 对2014-02-02格式识别不出是日期格式
                        Date date = cell.getDateCellValue();
                        DateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
                        cellvalue = formater.format(date);
                    } else { // 如果是纯数字
                        // 取得当前Cell的数值
                        cellvalue = NumberToTextConverter.toText(cell
                                .getNumericCellValue());
                    }
                    break;
                }
                // 如果当前Cell的Type为STRIN
                case HSSFCell.CELL_TYPE_STRING:
                    // 取得当前的Cell字符串
                    cellvalue = cell.getStringCellValue().replaceAll("'", "''");
                    break;
                case HSSFCell.CELL_TYPE_BLANK:
                    cellvalue = null;
                    break;
                // 默认的Cell值
                default: {
                    cellvalue = "";
                }
            }
        } else {
            cellvalue = "";
        }
        return cellvalue;
    }

    /**
     * 读取后缀为xlsx的excel文件的数据
     *
     * @param path
     * @param totalCell
     * @return List<String   [   ]>
     */
    private static List<String[]> readXlsx(String path, int startRow,
                                           int totalCell) {

        XSSFWorkbook xssfWorkbook = null;
        try {
            InputStream is = new FileInputStream(path);
            xssfWorkbook = new XSSFWorkbook(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<String[]> list = new ArrayList<String[]>();
        if (xssfWorkbook != null) {
            // Read the Sheet
            XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(0);
            if (xssfSheet == null) {
                return list;
            }
            // Read the Row
            for (int rowNum = startRow; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
                XSSFRow xssfRow = xssfSheet.getRow(rowNum);
                if (xssfRow != null) {
                    boolean hasValue = false;
                    String[] data = new String[totalCell];
                    for (int i = 0; i < totalCell; i++) {
                        Cell cell = xssfRow.getCell(i);
                        data[i] = getCellValue(cell);
                        if (StringUtils.isNotBlank(data[i]))
                            hasValue = true;
                    }
                    if (hasValue)
                        list.add(data);
                }
            }
        }
        return list;
    }
    
	private static List<String[]> readXlsxByRows(String path, int startRow, int endRowExclusive) {

		XSSFWorkbook xssfWorkbook = null;
		try {
			InputStream is = new FileInputStream(path);
			xssfWorkbook = new XSSFWorkbook(is);
		} catch (IOException e) {
			e.printStackTrace();
		}
		List<String[]> list = new ArrayList<String[]>();
		if (xssfWorkbook != null) {
			XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(0);
			if (xssfSheet == null) {
				return list;
			}
			int cellCount = 0;
			for (int rowNum = startRow; rowNum < endRowExclusive; rowNum++) {
				XSSFRow xssfRow = xssfSheet.getRow(rowNum);
				if(cellCount == 0)
					cellCount = xssfRow.getLastCellNum();
				if (xssfRow != null) {
					boolean hasValue = false;
					String[] data = new String[cellCount];
					for (int i = 0; i < cellCount; i++) {
						Cell cell = xssfRow.getCell(i);
						data[i] = getCellValue(cell);
						if (StringUtils.isNotBlank(data[i]))
							hasValue = true;
					}
					if (hasValue)
						list.add(data);
				}
			}
		}
		return list;
	}

    /**
     * 获取文件扩展名
     *
     * @param path
     * @return String
     */
    private static String getExt(String path) {
        if (path == null || path.equals("") || !path.contains(".")) {
            return null;
        } else {
            return path.substring(path.lastIndexOf(".") + 1, path.length());
        }
    }

    /**
     * 从流中读取excel
     * @param in
     * @param suffix 后缀名（xls、xlsx）
     * @param startRow
     * @return
     * @throws IOException
     */
    public static Map<String, List<String[]>> readExcelFromStream(InputStream in, String suffix, int startRow) throws IOException {
        Map<String, List<String[]>> map = new LinkedHashMap<>();
        if (StringUtils.isBlank(suffix)) {
            throw new IOException("请输入文件后缀名");
        }
        Workbook hssfWorkbook = suffix.equals("xls") ? new HSSFWorkbook(in) : new XSSFWorkbook(in);
        for (int sheetIndex = 0; sheetIndex < hssfWorkbook.getNumberOfSheets(); sheetIndex++) {
            List<String[]> list = new ArrayList<>();
            // Read the Sheet
            Sheet hssfSheet = hssfWorkbook.getSheetAt(sheetIndex);
            // Read the Row
            for (int rowNum = startRow; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
                Row hssfRow = hssfSheet.getRow(rowNum);
                if (hssfRow == null) {
                    continue;
                }
                int tempRowSize = hssfRow.getLastCellNum();
                String[] values = new String[tempRowSize];
                Arrays.fill(values, "");
                boolean hasValue = false;
                for (int columnNum = 0; columnNum < hssfRow.getLastCellNum(); columnNum++) {
                    String value = getCellValue(hssfRow.getCell(columnNum));
                    values[columnNum] = value;
                    if (StringUtils.isNotBlank(values[columnNum])) {
                        hasValue = true;
                    }
                }
                if (hasValue) {
                    list.add(values);
                }
            }
            map.put(hssfSheet.getSheetName(), list);
        }
        return map;
    }

    /**
     * 从流中读取csv文件
     * @param in
     * @return
     * @throws IOException
     */
    public static List<String[]> readCvsFromStream(InputStream in) throws IOException {
        List<String[]> result = new ArrayList<>();
        Reader reader = new InputStreamReader(in);
        BufferedReader br = new BufferedReader(reader);
        String line = "";
        while ((line = br.readLine()) != null) { //读取到的内容给line变量
            result.add(line.split(","));
        }
        return result;
    }
    
    /**
     * 读取某一行所有列值  直至遇到某一列为空 则返回 getLastCellNum这个获取最后一列
     * @param path
     * @return
     */
    public static List<String> readExcelOneRow(String path,int oneRow) {
        if (oneRow < 0) {
            return new ArrayList<String>();
        }
        if (path != null && !path.equals("")) {
            String ext = getExt(path);
            if (ext != null && !ext.equals("")) {
                if (ext.equals("xls")) {
                    return readXlsOneRow(path,oneRow);
                } else if (ext.equals("xlsx")) {
                    return readXlsxOneRow(path,oneRow);
                }
            }
        }
        return new ArrayList<String>();
    }
    
    /**
     * 读取后缀为xls的excel文件的数据
     *
     * @param path
     * @return List<String>
     */
    private static List<String> readXlsOneRow(String path, int oneRow) {

        HSSFWorkbook hssfWorkbook = null;
        try {
            InputStream is = new FileInputStream(path);
            hssfWorkbook = new HSSFWorkbook(is);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<String> list = new ArrayList<String>();
        if (hssfWorkbook != null) {
            // Read the Sheet
            HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(0);
            if (hssfSheet == null) {
                return list;
            }
            HSSFRow hssfRow = hssfSheet.getRow(oneRow);
            if(hssfRow!=null) {
            	for(int jj=0;jj<hssfRow.getLastCellNum();jj++) {
            		Cell cell = hssfRow.getCell(jj);
                    String cellValue= getCellValue(cell);
                    if (StringUtils.isBlank(cellValue)) {
                    	list.add("");
                    }else {
                    	list.add(cellValue);
                    }
                    
            	}
//            	int ii=0;
//                while(true) {
//                	Cell cell = hssfRow.getCell(ii);
//                    String cellValue= getCellValue(cell);
//                    if (StringUtils.isBlank(cellValue)) {
//                    	break;
//                    }
//                    list.add(cellValue);
//                    ii++;   
//                }
            }
        }
        return list;
    }
    
    /**
     * 读取后缀为xlsx的excel文件的数据
     *
     * @param path
     * @return List<String>
     */
    private static List<String> readXlsxOneRow(String path, int oneRow) {

        XSSFWorkbook xssfWorkbook = null;
        try {
            InputStream is = new FileInputStream(path);
            xssfWorkbook = new XSSFWorkbook(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<String> list = new ArrayList<String>();
        if (xssfWorkbook != null) {
            // Read the Sheet
        	XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(0);
            if (xssfSheet == null) {
                return list;
            }
            XSSFRow xssfRow = xssfSheet.getRow(oneRow);
            if(xssfRow!=null) {
            	int ii=0;
            	for(int jj=0;jj<xssfRow.getLastCellNum();jj++) {
            		Cell cell = xssfRow.getCell(ii);
                    String cellValue= getCellValue(cell);
                    if (StringUtils.isBlank(cellValue)) {
                    	list.add("");
                    }else {
                    	list.add(cellValue);
                    }
                    
            	}
            	
            	
//                while(true) {
//                	Cell cell = xssfRow.getCell(ii);
//                    String cellValue= getCellValue(cell);
//                    if (StringUtils.isBlank(cellValue)) {
//                    	break;
//                    }
//                    list.add(cellValue);
//                    ii++;   
//                }
            }
        }
        return list;
    }

    
	public static List<String[]> readExcelByRows(String path, int startRow, int endRowExclusive) {
		if (endRowExclusive <= startRow) {
			return new ArrayList<String[]>();
		}
		if (path != null && !path.equals("")) {
			String ext = getExt(path);
			if (ext != null && !ext.equals("")) {
				if (ext.equals("xls")) {
					return readXlsByRows(path, startRow, endRowExclusive);
				} else if (ext.equals("xlsx")) {
					return readXlsxByRows(path, startRow, endRowExclusive);
				}
			}
		}
		return new ArrayList<String[]>();
	}
}
