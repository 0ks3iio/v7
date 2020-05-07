/* 
 * @(#)ImportFileParam.java    Created on Apr 29, 2010
 * Copyright (c) 2010 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package net.zdsoft.newstusys.businessimport.file;

import java.util.List;

import net.zdsoft.newstusys.businessimport.param.DataImportParam;

/**
 * 导入文件解析参数
 * 
 * @author zhaosf
 * @version $Revision: 1.0 $, $Date: Apr 29, 2010 10:02:19 AM $
 */
public class ImportFileParserParam {

    public InParam getInParamInstance() {
        return new InParam();
    }

    public OutParam getOutParamInstance() {
        return new OutParam();
    }

    /**
     * 入参
     * 
     * @author zhaosf
     * @version $Revision: 1.0 $, $Date: Apr 29, 2010 10:18:55 AM $
     */
    public class InParam {
        private String importFile;// 导入文件

        // --------xls-----------
        private String xlsSheetName;
        private String xmlObjecDefine;
        private int beginRow;// 开始解析行
        private boolean hasTitle;

        // --------dbf-----------
        private boolean batchImport;// 是否分批导入
        
        
        private DataImportParam param;

        public InParam() {
        }

        public String getImportFile() {
            return importFile;
        }

        public void setImportFile(String importFile) {
            this.importFile = importFile;
        }

        public boolean isBatchImport() {
            return batchImport;
        }

        public void setBatchImport(boolean batchImport) {
            this.batchImport = batchImport;
        }

        public String getXlsSheetName() {
            return xlsSheetName;
        }

        public void setXlsSheetName(String xlsSheetName) {
            this.xlsSheetName = xlsSheetName;
        }

        public String getXmlObjecDefine() {
            return xmlObjecDefine;
        }

        public void setXmlObjecDefine(String xmlObjecDefine) {
            this.xmlObjecDefine = xmlObjecDefine;
        }

        public int getBeginRow() {
            return beginRow;
        }

        public void setBeginRow(int beginRow) {
            this.beginRow = beginRow;
        }
        
        public DataImportParam getParam() {
            return param;
        }

        public void setParam(DataImportParam param) {
            this.param = param;
        }

		public boolean isHasTitle() {
			return hasTitle;
		}

		public void setHasTitle(boolean hasTitle) {
			this.hasTitle = hasTitle;
		}

        
    }

    /**
     * 出参
     * 
     * @author zhaosf
     * @version $Revision: 1.0 $, $Date: Apr 29, 2010 10:19:04 AM $
     */
    public class OutParam {
        private String objectDefine;// 标题
        private String[] fields;// 列字段
        private List<String[]> rowDatas;// 数据行

        public OutParam() {

        }

        public String getObjectDefine() {
            return objectDefine;
        }

        public void setObjectDefine(String objectDefine) {
            this.objectDefine = objectDefine;
        }

        public String[] getFields() {
            return fields;
        }

        public void setFields(String[] fields) {
            this.fields = fields;
        }

        public List<String[]> getRowDatas() {
            return rowDatas;
        }

        public void setRowDatas(List<String[]> rowDatas) {
            this.rowDatas = rowDatas;
        }

    }

}
