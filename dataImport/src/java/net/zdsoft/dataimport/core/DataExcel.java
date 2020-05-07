package net.zdsoft.dataimport.core;

import java.util.ArrayList;
import java.util.List;

/**
 * @author shenke
 * @since 2017.07.31
 */
public class DataExcel {

    private String excelName;
    private List<DataSheet> dataSheetList;

    public DataExcel() {
        this.dataSheetList = new ArrayList<>();
    }

    public void addDataSheet(DataSheet dataSheet) {
        this.dataSheetList.add(dataSheet);
    }

    public void addDataSheetIfNotNull(DataSheet dataSheet) {
        if ( dataSheet != null ) {
            addDataSheet(dataSheet);
        }
    }

    public String getExcelName() {
        return excelName;
    }

    public void setExcelName(String excelName) {
        this.excelName = excelName;
    }

    public List<DataSheet> getDataSheetList() {
        return dataSheetList;
    }

}
