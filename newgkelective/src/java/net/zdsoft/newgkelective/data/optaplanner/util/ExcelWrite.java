package net.zdsoft.newgkelective.data.optaplanner.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

import javax.swing.filechooser.FileSystemView;

import org.apache.poi.ss.usermodel.Workbook;

public class ExcelWrite {

	public static final String FILENAME_OUT = "7_3_排课3";
	private static Workbook wb;

	public static BufferedWriter getBw(String name, String newParam) throws Exception {
		File desktopDir = FileSystemView.getFileSystemView().getHomeDirectory();
		String desktopPath = desktopDir.getAbsolutePath();
		String outFileDir = desktopPath + "\\" + ExcelWrite.FILENAME_OUT + "\\" + newParam;
		File dir = new File(outFileDir);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		File file = new File(dir, name);
		if (!file.exists()) {
			file.createNewFile();
		}
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true), "UTF-8"));
		return bw;
	}
	
	public static void lectureWriteExcel(){}
	
	private static void close(String name,String newParam) {
		File desktopDir = FileSystemView.getFileSystemView().getHomeDirectory();
		String desktopPath = desktopDir.getAbsolutePath();
		String outFileDir = desktopPath + "\\" +FILENAME_OUT+"\\"+newParam;
		File outDirFile = new File(outFileDir);
		outDirFile.mkdirs();
		String outFileName = outFileDir +"\\"+ name + ".xlsx";
		try {
			FileOutputStream out = new FileOutputStream(outFileName);
			wb.write(out);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void jxbWriteExcel() {}

}
