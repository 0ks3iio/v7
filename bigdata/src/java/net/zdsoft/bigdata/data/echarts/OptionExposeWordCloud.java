package net.zdsoft.bigdata.data.echarts;

/**
 * @author shenke
 * @since 2018/8/22 10:31
 */
public class OptionExposeWordCloud {

    private OptionExposeTitle exposeTitle;

    public static OptionExposeWordCloud getDefault() {
        OptionExposeWordCloud default_oe = new OptionExposeWordCloud();
        default_oe.setExposeTitle(OptionExposeTitle.getDefault());
        return default_oe;
    }

    public OptionExposeTitle getExposeTitle() {
        return exposeTitle;
    }

    public void setExposeTitle(OptionExposeTitle exposeTitle) {
        this.exposeTitle = exposeTitle;
    }
}
