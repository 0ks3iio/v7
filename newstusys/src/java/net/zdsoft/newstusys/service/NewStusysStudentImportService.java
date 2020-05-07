package net.zdsoft.newstusys.service;

import java.util.List;

/**
 * 
 * @author weixh
 * @since 2018年3月5日 下午5:23:11
 */
public interface NewStusysStudentImportService {
	/**
	 * 处理学生导入数据
	 * @param unitId
	 * @param rowDatas
	 * @return
	 */
	public String saveImportStuDatas(String unitId, List<String[]> rowDatas);

	/**
	 *
	 * @param unitId
	 * @param beginRow
	 * @param filePath
	 * @return
	 */
	
	public String saveUpdateDatas(String unitId, int beginRow, String filePath);
}
