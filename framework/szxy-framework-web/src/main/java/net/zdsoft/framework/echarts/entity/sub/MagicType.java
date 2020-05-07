package net.zdsoft.framework.echarts.entity.sub;

import java.util.List;

public class MagicType {
    private boolean show = true;
    private List<String> type;

    public MagicType() {
    }

    public MagicType(List<String> type) {
        this.type = type;

    }

    public boolean isShow() {
        return show;
    }

    public void setShow(boolean show) {
        this.show = show;
    }

    public List<String> getType() {
        return type;
    }

    public void setType(List<String> type) {
        this.type = type;
    }

}
