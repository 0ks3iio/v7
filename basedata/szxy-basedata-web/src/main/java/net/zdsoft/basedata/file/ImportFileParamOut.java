package net.zdsoft.basedata.file;

import java.util.List;

public class ImportFileParamOut {
	private String objectDefine;// 标题
    private String[] fields;// 列字段
    private List<String[]> rowDatas;// 数据行
    private List<Integer> indexs;
    private String errorMsg;
    
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

	public List<Integer> getIndexs() {
		return indexs;
	}

	public void setIndexs(List<Integer> indexs) {
		this.indexs = indexs;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

    
}
