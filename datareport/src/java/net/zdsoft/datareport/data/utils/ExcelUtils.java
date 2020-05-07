package net.zdsoft.datareport.data.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.zdsoft.framework.utils.DateUtils;
import net.zdsoft.framework.utils.StringUtils;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtils {
	/**				
	 * 读取excel中的数据
	 * @param path		excel文件路径
	 * @param startRow	从第几行开始读
	 * @param endRow	读到第几行结束（若未知填-1）
	 * @param startRank	从第几列开始读
	 * @param endRank	读到第几列结束（若未知填-1）
	 * @param tableType	表格类型
	 * @return List<String[]>
	 */
    public static List<String[]> readExcel(String path, int startRow, int endRow, int startRank, int endRank, int tableType) {
        if (path != null && !path.equals("")) {
            String ext = getExt(path);
            if (ext != null && !ext.equals("")) {
                if (ext.equals("xls")) {
                    return readXls(path, startRow, endRow, startRank, endRank, tableType);
                } else if (ext.equals("xlsx")) {
                    return readXlsx(path, startRow, endRow, startRank, endRank, tableType);
                }
            }
        }
        return new ArrayList<String[]>();
    }
    
    /**
     * 读取后缀为xls的excel文件的数据
     * @param path		excel文件路径
	 * @param startRow	从第几行开始读
	 * @param endRow	读到第几行结束（若未知填-1）
	 * @param startRank	从第几列开始读
	 * @param endRank	读到第几列结束（若未知填-1）
	 * @param tableType	表格类型
     * @return List<String[]>
     */
    private static List<String[]> readXls(String path, int startRow, int endRow, int startRank, int endRank, int tableType) {
		List<String[]> contentlist = new ArrayList<String[]>();
        try (InputStream is = new FileInputStream(path);
        		HSSFWorkbook hssfWorkbook = new HSSFWorkbook(is);) {
            if (hssfWorkbook != null) {
                // Read the Sheet
                HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(0);
                if (hssfSheet == null) {
                    return contentlist;
                }
                // Read content Row
                boolean hasValue = false;
                String[] data = null;
                HSSFRow hssfRow = null;
                Cell cell = null;
                String strValue = "";
                if (tableType == 1) {
                	for (int rowNum = startRow-1; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
                		hssfRow = hssfSheet.getRow(rowNum);
                		if (hssfRow != null) {
                			hasValue = false;
                			data = new String[endRank-startRank+1];
                			for (int i=0; i<endRank-startRank+1; i++) {
                				cell = hssfRow.getCell(i+startRank-1);
                				strValue = getCellValue(cell);
                				if (StringUtils.isBlank(strValue)) {
                					data[i] = null;
                				} else {
                					data[i] = strValue;
                					hasValue = true;
                				}
                			}
                			if (hasValue) {
                            	contentlist.add(data);
                            } else {
                            	break;
                            }
                		} else {
                			break;
                		}
                	}
                }
                if (tableType == 2) {
                	while (true) {
                		data = new String[endRow-startRow+1];
                		hasValue = false;
                		for (int i=0; i<endRow-startRow+1; i++) {
                			hssfRow = hssfSheet.getRow(i + startRow - 1);
                			cell = hssfRow.getCell(startRank-1);
                			strValue = getCellValue(cell);
                			if (StringUtils.isBlank(strValue)) {
            					data[i] = null;
            				} else {
            					data[i] = strValue;
            					hasValue = true;
            				}
                		}
                		startRank++;
                		if (hasValue) {
                			contentlist.add(data);
                		} else {
                			break;
                		}
                	}
                }
                if (tableType == 3) {
                	for (int rowNum = startRow-1; rowNum < endRow; rowNum++) {
                		hssfRow = hssfSheet.getRow(rowNum);
                		if (hssfRow != null) {
                			data = new String[endRank-startRank+1];
                			for (int i=0; i<endRank-startRank+1; i++) {
                				cell = hssfRow.getCell(i+startRank-1);
                				strValue = getCellValue(cell);
                    			if (StringUtils.isBlank(strValue)) {
                					data[i] = null;
                				} else {
                					data[i] = strValue;
                				}
                			}
                			contentlist.add(data);
                		} else {
                			break;
                		}
                	}
                }
            }
            is.close();
            hssfWorkbook.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return contentlist;
    }
    
    /**
     * 读取后缀为xlsx的excel文件的数据
     * @param path		excel文件路径
     * @param startRow	从第几行开始读
     * @param totalRow	需要读到第几列
     * @param tableType   表格类型
     * @return List<String[]>
     */
	private static List<String[]> readXlsx(String path, int startRow, int endRow, int startRank, int endRank, int tableType) {
		List<String[]> contentlist = new ArrayList<String[]>();
        try (InputStream is = new FileInputStream(path);
        		XSSFWorkbook xssfWorkbook = new XSSFWorkbook(is);) {
        	if (xssfWorkbook != null) {
                // Read the Sheet
                XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(0);
                if (xssfSheet == null) {
                    return contentlist;
                }
                // Read content Row
                boolean hasValue = false;
                String[] data = null;
                XSSFRow xssfRow = null;
                Cell cell = null;
                String strValue = "";
                if (tableType == 1) {
                	for (int rowNum = startRow-1; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
                		xssfRow = xssfSheet.getRow(rowNum);
                		if (xssfRow != null) {
                			hasValue = false;
                			data = new String[endRank-startRank+1];
                			for (int i=0; i<endRank-startRank+1; i++) {
                				cell = xssfRow.getCell(i+startRank-1);
                				strValue = getCellValue(cell);
                    			if (StringUtils.isBlank(strValue)) {
                					data[i] = null;
                				} else {
                					data[i] = strValue;
                					hasValue = true;
                				}
                			}
                			if (hasValue) {
                            	contentlist.add(data);
                            } else {
                            	break;
                            }
                		} else {
                			break;
                		}
                	}
                }
                if (tableType == 2) {
                	while (true) {
                		data = new String[endRow-startRow+1];
                		hasValue = false;
                		for (int i=0; i<endRow-startRow+1; i++) {
                			xssfRow = xssfSheet.getRow(i + startRow - 1);
                			cell = xssfRow.getCell(startRank-1);
                			strValue = getCellValue(cell);
                			if (StringUtils.isBlank(strValue)) {
            					data[i] = null;
            				} else {
            					data[i] = strValue;
            					hasValue = true;
            				}
                		}
                		startRank++;
                		if (hasValue) {
                			contentlist.add(data);
                		} else {
                			break;
                		}
                	}
                }
                if (tableType == 3) {
                	for (int rowNum = startRow-1; rowNum < endRow; rowNum++) {
                		xssfRow = xssfSheet.getRow(rowNum);
                		if (xssfRow != null) {
                			data = new String[endRank-startRank+1];
                			for (int i=0; i<endRank-startRank+1; i++) {
                				cell = xssfRow.getCell(i+startRank-1);
                				strValue = getCellValue(cell);
                    			if (StringUtils.isBlank(strValue)) {
                					data[i] = null;
                				} else {
                					data[i] = strValue;
                				}
                			}
                			contentlist.add(data);
                		} else {
                			break;
                		}
                	}
                }
            }
        	is.close();
        	xssfWorkbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return contentlist;
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
     * 根据excel单元格类型获取excel单元格值
     *
     * @param cell
     * @return nizq
     */
    @SuppressWarnings({ "deprecation"})
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
                        double value = cell.getNumericCellValue();
                        Date date = DateUtil.getJavaDate(value);
                        cellvalue = DateUtils.date2StringByDay(date);
                    }
                    // 判断当前的cell是否为Date
                    else if (HSSFDateUtil.isCellDateFormatted(cell)) { // 先注释日期类型的转换，在实际测试中发现HSSFDateUtil.isCellDateFormatted(cell)只识别2014/02/02这种格式。
                        // 如果是Date类型则，取得该Cell的Date值 // 对2014-02-02格式识别不出是日期格式
                        Date date = cell.getDateCellValue();
                        cellvalue = DateUtils.date2StringByDay(date);
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
     * 读取某一行所有列值  直至遇到某一列为空 则返回 getLastCellNum这个获取最后一列
     * @param path
     * @param oneRow
     * @return
     */
    public static List<String> readExcelOneRow(String path,int oneRow) {
        if (oneRow <= 0) {
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
     * @param path
     * @param oneRow
     * @return
     */
    private static List<String> readXlsOneRow(String path, int oneRow) {
    	List<String> list = new ArrayList<String>();
        try (InputStream is = new FileInputStream(path);
        		HSSFWorkbook hssfWorkbook = new HSSFWorkbook(is);) {
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
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    /**
     * 读取后缀为xlsx的excel文件的数据
     * @param path
     * @param oneRow
     * @return
     */
    private static List<String> readXlsxOneRow(String path, int oneRow) {
    	List<String> list = new ArrayList<String>();
        try (InputStream is = new FileInputStream(path);
        		XSSFWorkbook xssfWorkbook = new XSSFWorkbook(is);) {
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
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }
}
