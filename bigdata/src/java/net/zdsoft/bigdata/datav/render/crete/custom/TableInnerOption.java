package net.zdsoft.bigdata.datav.render.crete.custom;

import java.util.List;

/**
 * @author shenke
 * @since 2018/10/14 13:14
 */
public class TableInnerOption {

    private List<String> heads;
    private List<List<String>> items;

    private TableHeaderStyle tableHeaderStyle;
    private TableBodyStyle tableBodyStyle;

    public List<String> getHeads() {
        return heads;
    }

    public void setHeads(List<String> heads) {
        this.heads = heads;
    }

    public List<List<String>> getItems() {
        return items;
    }

    public void setItems(List<List<String>> items) {
        this.items = items;
    }

    public TableHeaderStyle getTableHeaderStyle() {
        return tableHeaderStyle;
    }

    public void setTableHeaderStyle(TableHeaderStyle tableHeaderStyle) {
        this.tableHeaderStyle = tableHeaderStyle;
    }

    public TableBodyStyle getTableBodyStyle() {
        return tableBodyStyle;
    }

    public void setTableBodyStyle(TableBodyStyle tableBodyStyle) {
        this.tableBodyStyle = tableBodyStyle;
    }
}
