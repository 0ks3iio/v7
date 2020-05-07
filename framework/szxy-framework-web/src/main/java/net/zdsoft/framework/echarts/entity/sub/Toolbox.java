package net.zdsoft.framework.echarts.entity.sub;


public class Toolbox {
    private boolean show = true;
    private Feature feature;
    private Mark mark;
    private DataView dataView;
    private Restore restore;
    private SaveAsImage saveAsImage;

    public boolean isShow() {
        return show;
    }

    public void setShow(boolean show) {
        this.show = show;
    }

    public Restore getRestore() {
        return restore;
    }

    public void setRestore(Restore restore) {
        this.restore = restore;
    }

    public Feature getFeature() {
        return feature;
    }

    public void setFeature(Feature feature) {
        this.feature = feature;
    }

    public DataView getDataView() {
        return dataView;
    }

    public void setDataView(DataView dataView) {
        this.dataView = dataView;
    }

    public Mark getMark() {
        return mark;
    }

    public void setMark(Mark mark) {
        this.mark = mark;
    }

    public SaveAsImage getSaveAsImage() {
        return saveAsImage;
    }

    public void setSaveAsImage(SaveAsImage saveAsImage) {
        this.saveAsImage = saveAsImage;
    }
}
