package net.zdsoft.framework.echarts.entity.sub;

public class SaveAsImage {
    private boolean readOnly;

    public SaveAsImage() {
    }

    public SaveAsImage(boolean readOnly) {
        this.readOnly = readOnly;
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

}
