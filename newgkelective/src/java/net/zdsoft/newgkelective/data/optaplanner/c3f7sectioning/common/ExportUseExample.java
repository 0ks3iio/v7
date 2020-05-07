package net.zdsoft.newgkelective.data.optaplanner.c3f7sectioning.common;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.swing.filechooser.FileSystemView;

public class ExportUseExample {
	public static void main(String[] args) {
		// 初始化数据
		List<StudentVO> list = new ArrayList<StudentVO>();
		StudentVO vo = null;
		for(int i=0 ; i < 800 ; i++){
			vo = new StudentVO();
			vo.setStuId(Integer.toString(i));
			vo.setName("张三"+i);
			vo.setSex("男");
			vo.setRawClassName("高二（3）班");
			vo.setChooseSubject1("物");
			vo.setChooseSubject2("化");
			vo.setChooseSubject3("生");
			list.add(vo);
		}

		FileOutputStream out = null;
		try {
			File desktopDir = FileSystemView.getFileSystemView().getHomeDirectory();
			String desktopPath = desktopDir.getAbsolutePath();
			out = new FileOutputStream(desktopPath+"\\exportTest.xls");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		ExcelUtil<StudentVO> util = new ExcelUtil<StudentVO>(StudentVO.class);// 创建工具类.
		util.exportExcel(list, "学生信息", out);// 导出
		System.out.println("----执行完毕----------");
	}

}

