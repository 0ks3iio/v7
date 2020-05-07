package net.zdsoft.dataimport.core;

import java.util.ArrayList;
import java.util.List;

/**
 * @author shenke
 * @since 2017.07.31
 */
public class DataSheet {

    private String sheetName;
    private String title;               // maybe not exists
    private List<String> headers;
    private List<DataRow> dataRowList;

    public DataSheet() {
        this.dataRowList = new ArrayList<>();
    }

    public void addDataRowIfNotEmpty(DataRow dataRow) {

        if ( dataRow != null && dataRow.getDataCellList() != null && !dataRow.getDataCellList().isEmpty() ) {
            this.dataRowList.add(dataRow);
        }
    }

    public String getSheetName() {
        return sheetName;
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getHeaders() {
        return headers;
    }

    public void setHeaders(List<String> headers) {
        this.headers = headers;
    }

    public List<DataRow> getDataRowList() {
        return dataRowList;
    }
}
