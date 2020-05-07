package net.zdsoft.newgkelective.data.optaplanner.c3f7sectioningv4.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

import javax.swing.filechooser.FileSystemView;

public class ImportUseExample {
	public static void main(String[] args) {
		FileInputStream fis = null;
		try {
			//File desktopDir = FileSystemView.getFileSystemView().getHomeDirectory();
			//String desktopPath = desktopDir.getAbsolutePath();
			
			String inputFilePath = Class.class.getClass().getResource("/").getPath() + "../../data/net/zdsoft/newgk/twoplusx/严州中学.xls";
			System.out.println(inputFilePath);
			fis = new FileInputStream(inputFilePath);
			
			ExcelUtil<StudentVO> util = new ExcelUtil<StudentVO>(
					StudentVO.class);// 创建excel工具类
			List<StudentVO> list = util.importExcel("学生信息", fis);// 导入
			System.out.println("Finihsed importing, total number of students: " + list.size());
			for (StudentVO st : list) {
				System.out.println(st.toString());
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}
}
