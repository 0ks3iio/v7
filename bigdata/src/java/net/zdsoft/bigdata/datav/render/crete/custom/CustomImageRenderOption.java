package net.zdsoft.bigdata.datav.render.crete.custom;

import net.zdsoft.bigdata.datav.render.crete.RenderOption;

import java.util.List;

/**
 * @author shenke
 * @since 2018/10/31 上午11:25
 */
public class CustomImageRenderOption extends RenderOption<List<ImageOption>> {
    private Boolean showPoint;

    public Boolean getShowPoint() {
        return showPoint;
    }

    public void setShowPoint(Boolean showPoint) {
        this.showPoint = showPoint;
    }
}
