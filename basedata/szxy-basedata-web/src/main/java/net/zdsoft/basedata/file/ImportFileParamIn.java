package net.zdsoft.basedata.file;

public class ImportFileParamIn {
	   private String importFile;// 导入文件

       // --------xls-----------
       private String xlsSheetName;
       private String xmlObjecDefine;
       private int beginRow;// 开始解析行

       public String getImportFile() {
           return importFile;
       }

       public void setImportFile(String importFile) {
           this.importFile = importFile;
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
       
       
}
