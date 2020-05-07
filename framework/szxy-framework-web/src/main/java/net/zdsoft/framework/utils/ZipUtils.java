package net.zdsoft.framework.utils;

/*
 * Author: Sophie Dong
 * Copyright (c) 2006 winupon Networks, Inc. All rights reserved.
 * 
 * Last modified & Comments:
 * <BR>$ZipUtils.java$
 * <BR>Revision 1.00 2008-5-12 下午01:38:46 dongxx
 * <BR>first version
 * <BR>
 */

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Map.Entry;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;

/**
 * 
 * @author Sophie Dong
 * @version 1.0
 */
public class ZipUtils {

    public static String zipEncode(String str) {
        byte[] input;
        try {
            input = (str).getBytes("utf8");
        }
        catch (UnsupportedEncodingException e) {
            input = (str).getBytes();
        }
        Deflater compressor = new Deflater();
        compressor.setLevel(Deflater.DEFAULT_COMPRESSION);
        compressor.setInput(input);
        compressor.finish();
        ByteArrayOutputStream bos = new ByteArrayOutputStream(input.length);
        byte[] buf = new byte[1024];
        while (!compressor.finished()) {
            int count = compressor.deflate(buf);
            if (count <= 0)
                break;
            bos.write(buf, 0, count);
        }
        byte[] compressedData = bos.toByteArray();
        return Base64.encodeBase64URLSafeString(compressedData);
    }

    public static String unzipDecode(String encode) {

        byte[] bs;
        try {
            bs = Base64.decodeBase64(encode);
            Inflater decompressor = new Inflater();
            decompressor.setInput(bs);
            ByteArrayOutputStream bos = new ByteArrayOutputStream(bs.length);
            byte[] buf = new byte[1024];
            buf = new byte[1024];
            while (!decompressor.finished()) {
                int count = decompressor.inflate(buf);
                if (count <= 0)
                    break;
                bos.write(buf, 0, count);
            }
            bos.close();
            byte[] decompressedData = bos.toByteArray();
            return new String(decompressedData, "utf8");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Creates a Zip archive. If the name of the file passed in is a directory,
     * the directory's contents will be made into a Zip file.
     */
    public static String makeZip(String fileName) throws IOException, FileNotFoundException {
        File file = new File(fileName);
        ZipOutputStream zos = null;

        try {
            zos = new ZipOutputStream(new FileOutputStream(file + ".zip"));
            // Call recursion.
            recurseFiles(file, zos);
        }
        finally {
            if (null != zos) {
                zos.close();
            }
        }
        return file + ".zip";
    }
    
    public static void makeZip(Map<String, HSSFWorkbook> excels, String zipName, HttpServletResponse resp) {
        try {
        	resp.reset(); 
    		OutputStream out = resp.getOutputStream();
    		resp.setHeader("Cache-Control", "");
    		resp.setContentType("application/data;charset=UTF-8");
    		resp.setHeader("Content-Disposition","attachment; filename=" + encodeName(zipName, ".zip"));
    		ZipOutputStream zos = new ZipOutputStream(out);
    		BufferedOutputStream buf = new BufferedOutputStream(zos);
    		for (Entry<String, HSSFWorkbook> entry : excels.entrySet()) {
    			ZipEntry zipEntry = new ZipEntry(entry.getKey() +".xls");
                zos.putNextEntry(zipEntry);
                zos.setEncoding("utf-8");//设置编码格式
                entry.getValue().write(buf);
                buf.flush();  // 此处close 会影响response OutputStream，只能flush
                zos.closeEntry();
    		}
    		buf.close();
    		zos.close(); // 不可省略
        	out.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			
		}
    }
    
    private static String encodeName(String fileName, String suffix) {
    	try {
			return URLEncoder.encode(fileName + (suffix == null ? "" : suffix), "utf-8");
		} catch (UnsupportedEncodingException e1) {
			return fileName;
		}
    }

    private static void recurseFiles(File file, ZipOutputStream zos) throws IOException,
            FileNotFoundException {
        if (file.isDirectory()) {
            // Create an array with all of the files and subdirectories
            // of the current directory.
            String[] fileNames = file.list();
            if (fileNames != null) {

                // Recursively add each array entry to make sure that we get
                // subdirectories as well as normal files in the directory.
                for (int i = 0; i < fileNames.length; i++) {
                    recurseFiles(new File(file, fileNames[i]), zos);// 给子目录里的文件打包!!
                }
            }
        }
        // Otherwise, a file so add it as an entry to the Zip file.
        else {
            byte[] buf = new byte[1024];
            int len;
            // Create a new Zip entry with the file's name.
            ZipEntry zipEntry = new ZipEntry(file.getName());
            // Create a buffered input stream out of the file
            // we're trying to add into the Zip archive.
            FileInputStream fin = new FileInputStream(file);
            BufferedInputStream in = new BufferedInputStream(fin);
            zos.putNextEntry(zipEntry);
            // Read bytes from the file and write into the Zip archive.
            while ((len = in.read(buf)) >= 0) {
                zos.write(buf, 0, len);
            }
            // Close the input stream.
            in.close();
            // Close this entry in the Zip stream.
            zos.closeEntry();
        }
    }
    
    /**
     * Creates a Zip archive. If the name of the file passed in is a directory,
     * the directory's contents will be made into a Zip file.
     */
    public static String makeZipWithFile(String fileName) throws IOException, FileNotFoundException {
        File file = new File(fileName);
        ZipOutputStream zos = null;

        try {
            zos = new ZipOutputStream(new FileOutputStream(file + ".zip"));
            zos.setEncoding("UTF-8");
            // Call recursion.
            String filePath = "";
            if(file.isDirectory()){
            	filePath = file.getName();
            	recurseFiles(file, zos,filePath);
            }else{
            	recurseFiles(file, zos);
            }
        }
        finally {
            if (null != zos) {
                zos.close();
            }
        }
        return file + ".zip";
    }
    
	private static void recurseFiles(File file, ZipOutputStream zos,
			String filePath) throws IOException, FileNotFoundException {
		if (file.isDirectory()) {
			// Create an array with all of the files and subdirectories
			// of the current directory.
			if(file.getPath().endsWith(filePath)){
				ZipEntry zipEntry = new ZipEntry(file.getName() + "/");  
				zos.putNextEntry(zipEntry);
			}else{
				ZipEntry zipEntry = new ZipEntry(file.getPath().substring(file.getPath().indexOf(filePath), file.getPath().length()) + "/");  
				zos.putNextEntry(zipEntry);
			}
			
            
			String[] fileNames = file.list();
			if (fileNames != null) {

				// Recursively add each array entry to make sure that we get
				// subdirectories as well as normal files in the directory.
			  
				for (int i = 0; i < fileNames.length; i++) {
					recurseFiles(new File(file, fileNames[i]), zos,filePath);// 给子目录里的文件打包!!
				}
			}
		}
		// Otherwise, a file so add it as an entry to the Zip file.
		else {
				
			byte[] buf = new byte[1024];
			int len;
			// Create a new Zip entry with the file's name.
			ZipEntry zipEntry = new ZipEntry(file.getPath().substring(file.getPath().indexOf(filePath), file.getPath().length()));  
			// Create a buffered input stream out of the file
			// we're trying to add into the Zip archive.
			FileInputStream fin = new FileInputStream(file);
			BufferedInputStream in = new BufferedInputStream(fin);
			zos.putNextEntry(zipEntry);
			// Read bytes from the file and write into the Zip archive.
			while ((len = in.read(buf)) >= 0) {
				zos.write(buf, 0, len);
			}
			// Close the input stream.
			in.close();
			// Close this entry in the Zip stream.
			zos.closeEntry();
		}
	}
    
}
