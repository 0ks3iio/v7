package net.zdsoft.framework.echarts.entity.sub;

public class DataView {

    public DataView() {

    }

    public DataView(boolean readOnly) {
        this.readOnly = readOnly;
    }

    private boolean readOnly;

    public boolean isReadOnly() {
        return readOnly;
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

}
