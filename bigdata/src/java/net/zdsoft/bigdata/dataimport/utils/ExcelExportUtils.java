package net.zdsoft.bigdata.dataimport.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import net.zdsoft.bigdata.dataimport.entity.ExcelTemplate;
import net.zdsoft.bigdata.dataimport.entity.ExcelTemplate4NoAnn;

import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.metadata.BaseRowModel;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.metadata.Table;
import com.alibaba.excel.support.ExcelTypeEnum;

public class ExcelExportUtils {
	// 导出excel java映射
	public static void export(String fileName, String filePath,
			int headLineMum, List<? extends BaseRowModel> datas,
			Class<? extends BaseRowModel> clazz) throws Exception {
		String fullFilePath = filePath + java.io.File.separator + fileName;
		File fileParent = new File(filePath);
		if (!fileParent.exists()) {
			fileParent.mkdirs();
		}
		File file = new File(fullFilePath);
		if (!file.exists()) {
			file.createNewFile();
		}
		OutputStream out = new FileOutputStream(fullFilePath);
		ExcelTypeEnum excelType = ExcelTypeEnum.XLSX;
		if (fullFilePath.endsWith(".xls")) {
			excelType = ExcelTypeEnum.XLS;
		}
		try {
			ExcelWriter writer = new ExcelWriter(out, excelType);
			// 写第一个sheet
			Sheet sheet = new Sheet(1, headLineMum, clazz);
			writer.write(datas, sheet);
			writer.finish();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	// 导出excel 每行数据是List无表头
	public static void export(String fileName, String filePath,
			List<List<String>> datas, List<List<String>> head)
			throws Exception {
		String fullFilePath = filePath + java.io.File.separator + fileName;
		File fileParent = new File(filePath);
		if (!fileParent.exists()) {
			fileParent.mkdirs();
		}
		File file = new File(fullFilePath);
		if (!file.exists()) {
			file.createNewFile();
		}
		OutputStream out = new FileOutputStream(fullFilePath);
		ExcelTypeEnum excelType = ExcelTypeEnum.XLSX;
		if (fullFilePath.endsWith(".xls")) {
			excelType = ExcelTypeEnum.XLS;
		}
		try {
			ExcelWriter writer = new ExcelWriter(out, excelType);
			Sheet sheet = new Sheet(1, 1);

			sheet.setHead(head);
			writer.write0(datas, sheet);
			writer.finish();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	// 写入多sheet的demo
	public void multiShetetDemo() throws FileNotFoundException {
		OutputStream out = new FileOutputStream("d:\\test\\multiSheet.xlsx");
		try {
			ExcelWriter writer = new ExcelWriter(out, ExcelTypeEnum.XLSX);

			// sheet1 模型上打有表头的注解
			Sheet sheet1 = new Sheet(1, 0, ExcelTemplate.class);
			sheet1.setSheetName("第一个sheet");

			writer.write(getData(), sheet1);

			// sheet2 模型上没有注解，表头数据动态传入
			List<List<String>> head = new ArrayList<List<String>>();
			List<String> headCoulumn1 = new ArrayList<String>();
			List<String> headCoulumn2 = new ArrayList<String>();
			List<String> headCoulumn3 = new ArrayList<String>();
			headCoulumn1.add("第一列");
			headCoulumn2.add("第二列");
			headCoulumn3.add("第三列");
			head.add(headCoulumn1);
			head.add(headCoulumn2);
			head.add(headCoulumn3);
			Sheet sheet2 = new Sheet(2, 1, ExcelTemplate4NoAnn.class,
					"第二个sheet", head);
			writer.write(getNoAnnData(), sheet2);
			writer.finish();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	// 多个Table的demo
	public void multiTableDemo() throws FileNotFoundException {
		OutputStream out = new FileOutputStream("d:\\test\\multiTable.xlsx");
		try {
			ExcelWriter writer = new ExcelWriter(out, ExcelTypeEnum.XLSX);

			// 写sheet1 数据全是List<String> 无模型映射关系
			Sheet sheet1 = new Sheet(1, 0);
			sheet1.setSheetName("第一个sheet");
			Table table1 = new Table(1);
			writer.write0(getListString(), sheet1, table1);
			writer.write0(getListString(), sheet1, table1);

			// 写sheet2 模型上打有表头的注解
			Table table2 = new Table(2);
			table2.setClazz(ExcelTemplate.class);
			writer.write(getData(), sheet1, table2);

			// 写sheet3 模型上没有注解，表头数据动态传入,此情况下模型field顺序与excel现实顺序一致
			List<List<String>> head = new ArrayList<List<String>>();
			List<String> headCoulumn1 = new ArrayList<String>();
			List<String> headCoulumn2 = new ArrayList<String>();
			List<String> headCoulumn3 = new ArrayList<String>();
			headCoulumn1.add("第一列");
			headCoulumn2.add("第二列");
			headCoulumn3.add("第三列");
			head.add(headCoulumn1);
			head.add(headCoulumn2);
			head.add(headCoulumn3);
			Table table3 = new Table(3);
			table3.setHead(head);
			table3.setClazz(ExcelTemplate4NoAnn.class);
			writer.write(getNoAnnData(), sheet1, table3);
			writer.write(getNoAnnData(), sheet1, table3);

			writer.finish();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void test() throws Exception {
		ExcelExportUtils.export("error3.xls", "d:\\test2\\", 1, getData(),
				ExcelTemplate.class);
	}

	private List<List<String>> getListString() {
		List<List<String>> result = new ArrayList<List<String>>();
		List<String> data = new ArrayList<String>();
		data.add("1");
		data.add("2");
		data.add("3");
		result.add(data);
		return result;
	}

	private List<ExcelTemplate> getData() {
		List<ExcelTemplate> list = new ArrayList<ExcelTemplate>();
		ExcelTemplate info1 = new ExcelTemplate();
		info1.setCol1(11);
		info1.setCol2("12");
		info1.setCol3("13");

		ExcelTemplate info2 = new ExcelTemplate();
		info2.setCol1(21);
		info2.setCol2("22");
		info2.setCol3("23");

		ExcelTemplate info3 = new ExcelTemplate();
		info3.setCol1(31);
		info3.setCol2("32");
		info3.setCol3("33");

		list.add(info1);
		list.add(info2);
		list.add(info3);
		return list;
	}

	private List<ExcelTemplate4NoAnn> getNoAnnData() {
		List<ExcelTemplate4NoAnn> list = new ArrayList<ExcelTemplate4NoAnn>();
		ExcelTemplate4NoAnn info1 = new ExcelTemplate4NoAnn();
		info1.setCol1(11);
		info1.setCol2("12");
		info1.setCol3("13");

		ExcelTemplate4NoAnn info2 = new ExcelTemplate4NoAnn();
		info2.setCol1(21);
		info2.setCol2("22");
		info2.setCol3("23");

		ExcelTemplate4NoAnn info3 = new ExcelTemplate4NoAnn();
		info3.setCol1(31);
		info3.setCol2("32");
		info3.setCol3("33");

		list.add(info1);
		list.add(info2);
		list.add(info3);
		return list;
	}
}
