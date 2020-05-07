package net.zdsoft.newgkelective.data.optaplanner.c2f3sectionscheduling.common;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import javax.swing.filechooser.FileSystemView;

import org.apache.poi.ss.usermodel.Workbook;


public class ExcelWrite {

	public static final String FILENAME_OUT = "7_3";
	
	private Workbook wb = null;
	
	public static BufferedWriter getBw(String name,long newParam) throws Exception{
		File desktopDir = FileSystemView.getFileSystemView().getHomeDirectory();
		String desktopPath = desktopDir.getAbsolutePath();
		String outFileDir = desktopPath + "\\" +ExcelWrite.FILENAME_OUT+"\\"+newParam;
		System.out.println(outFileDir);
		File dir=new File(outFileDir);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		File file = new File(dir, name);  
		if (!file.exists()) {
			file.createNewFile();
		}
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
		                new FileOutputStream(file,true), "UTF-8"));
		return bw;
	}
	
	private void close(String name,long newParam) {
		File desktopDir = FileSystemView.getFileSystemView().getHomeDirectory();
		String desktopPath = desktopDir.getAbsolutePath();
		String outFileDir = desktopPath + "\\" +FILENAME_OUT+"\\"+newParam;
		File outDirFile = new File(outFileDir);
		outDirFile.mkdirs();
		String outFileName = outFileDir +"\\"+ name + ".xlsx";
		try {
			FileOutputStream out = new FileOutputStream(outFileName);
			wb.write(out);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	


}
