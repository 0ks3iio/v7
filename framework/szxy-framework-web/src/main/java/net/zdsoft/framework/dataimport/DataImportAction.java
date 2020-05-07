package net.zdsoft.framework.dataimport;

import java.io.*;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.zdsoft.framework.action.BaseAction;

import net.zdsoft.framework.utils.ExportUtils;
import net.zdsoft.framework.utils.FileUtils;
import net.zdsoft.framework.utils.Validators;
import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public abstract class DataImportAction extends BaseAction {

	/**
	 * 对象名称
	 * 
	 * @return
	 */
	public abstract String getObjectName();

	/**
	 * 导入说明
	 */
	public abstract String getDescription();

	/**
	 * 列头
	 * 
	 * @return
	 */
	public abstract List<String> getRowTitleList();

	/**
	 * 数据导入
	 */
	public abstract String dataImport(String filePath, String params);

	/**
	 * 下载模板
	 */
	public abstract void downloadTemplate(HttpServletRequest request,
			HttpServletResponse response);

    /**
     * 错误信息导出
     */
	public void exportErrorExcel(HttpServletRequest request,
                                          HttpServletResponse response) {
        String errorFilePath = request.getParameter("errorPath");
        try {
            ExportUtils.outputFile(errorFilePath, "错误信息.xls", response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

	/**
	 * 模板验证
	 */

	public String templateValidate(List<String[]> allDataList,
			List<String> titleList) {
		String errorDataMsg = "";
		if (CollectionUtils.isNotEmpty(allDataList)) {
			String[] realTitles = allDataList.get(0);
			if (realTitles != null) {
				if (realTitles.length != titleList.size()) {
					return errorDataMsg = "导入数据列数(" + realTitles.length + ")与模板列数不符("
							+ titleList.size() + ")";
				}
				for (int i = 0; i < realTitles.length; i++) {
					if (!titleList.contains(realTitles[i])) {
						errorDataMsg = "模板中不存在列名：" + realTitles[i];
						break;
					}
				}
			}
		} else {
			errorDataMsg = "模板中不存在数据";
		}
		return errorDataMsg;
	}

    /**
     * 将错误数据导入Excel
     * @param filePath
     * @param workbook
     * @return
     */
	public String saveErrorExcel(String filePath, HSSFWorkbook workbook) {
	    if (Validators.isEmpty(filePath)) {
	        return "";
        }
        filePath = filePath.substring(0, filePath.lastIndexOf(".")) + "-errorMessage-" + System.currentTimeMillis() + filePath.substring(filePath.lastIndexOf("."));
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(filePath);
            workbook.write(fileOutputStream); // 输出文件
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            FileUtils.close(fileOutputStream);
        }
        return filePath;
    }
}
