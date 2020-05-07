package net.zdsoft.dataimport.core;

import java.util.ArrayList;
import java.util.List;

/**
 * @author shenke
 * @since 2017.07.31
 */
public class DataRow {

    private List<DataCell> dataCellList;


    public DataRow() {
        this.dataCellList = new ArrayList<>();
    }

    public void addDataCellIfNotnull (DataCell dataCell) {
        if ( dataCell != null) {
            this.dataCellList.add(dataCell);
        }
    }

    public List<DataCell> getDataCellList() {
        return dataCellList;
    }
}
