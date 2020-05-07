/* 
 * @(#)TaskUtil.java    Created on Sep 20, 2009
 * Copyright (c) 2009 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package net.zdsoft.newstusys.businessimport.common;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;

import javax.servlet.http.HttpServletResponse;

/**
 * @author Administrator
 * @version $Revision: 1.0 $, $Date: Sep 20, 2009 12:41:26 PM $
 */
public class TaskUtil {
    public static String collection2HtmlStr(Collection<String> c) {
        String msg = "";
        if (null == c || c.size() == 0)
            return msg;

        for (String str : c) {
            msg += str + "<br>";
        }
        return msg;
    }

    public static int getRealLength(String str) {
        if (str == null) {
            return 0;
        }

        char separator = 256;
        int realLength = 0;

        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) >= separator) {
                realLength += 2;
            } else {
                realLength++;
            }
        }
        return realLength;
    }

    /**
     * 取目录下的文件全路径
     * 
     * @param dirs
     * @param fileName
     * @return
     */
    public static String getFilePath(String[] dirs) {
        String path = "";

        if (null == dirs)
            return path;

        for (String dir : dirs) {
            if (dir.substring(0, 1).equals(File.separator)) {
                path += dir;
            } else {
                path += File.separator + dir;
            }
        }

        return path;
    }

    /**
     * 取文件类型
     * 
     * @param path
     * @return
     */
    public static String getFileTypePostfix(String path) {
        int index = path.lastIndexOf(File.separatorChar);
        String name = path.substring(index + 1);
        String[] arr = name.split("\\.");//转义
        if (null != arr && arr.length == 2) {
            return "."+arr[1];
        } else {
            return "";
        }
    }
   
    public static void getErrorData(HttpServletResponse res, String objectName, String errorDataPath)
            throws IOException {
        String fileName = objectName + "_ErrorData" + TaskUtil.getFileTypePostfix(errorDataPath);

        res.setHeader("Cache-Control", "");
        res.setContentType("application/data;charset=UTF-8");
        res.setHeader("Content-Disposition", "attachment; filename=" + fileName);
        OutputStream out = null;
        FileInputStream fis = null;
        try {
            out = new BufferedOutputStream(res.getOutputStream());
            fis = new FileInputStream(errorDataPath);
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            out.write(buffer);
            out.flush();

        } finally {
            fis.close();
            out.close();
        }
    }

}
