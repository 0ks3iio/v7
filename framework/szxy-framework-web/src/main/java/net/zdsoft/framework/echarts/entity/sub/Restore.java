package net.zdsoft.framework.echarts.entity.sub;

public class Restore {
    private boolean readOnly;
    
    public Restore() {}
    
    public Restore(boolean readOnly) {
        this.readOnly = readOnly;
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

}
