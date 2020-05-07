/* 
 * @(#)ImportFileParserFactory.java    Created on Apr 29, 2010
 * Copyright (c) 2010 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package net.zdsoft.newstusys.businessimport.file;

import net.zdsoft.newstusys.businessimport.common.DataImportConstants;

/**
 * 导入文件解析工厂类
 * 
 * @author zhaosf
 * @version $Revision: 1.0 $, $Date: Apr 29, 2010 10:39:43 AM $
 */
public class ImportFileParserFactory {

    /**
     * 获得解析器
     * 
     * @param fileType
     * @return
     */
    public static ImportFileParser getParser(String fileType) {
        ImportFileParser parser = null;

        if (DataImportConstants.FILE_TYPE_XLS.equalsIgnoreCase(fileType)) {
            parser = new XlsFileParser();
        } else if (DataImportConstants.FILE_TYPE_XLSX.equalsIgnoreCase(fileType)) {
            parser = new XlsxFileParser();
        } else if (DataImportConstants.FILE_TYPE_ZIP.equalsIgnoreCase(fileType)) {
            parser = new XlsFileParser();
        }

        return parser;
    }
}
