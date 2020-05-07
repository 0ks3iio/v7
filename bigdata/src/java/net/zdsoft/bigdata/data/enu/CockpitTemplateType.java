/**
 * FileName: CockpitTemplateType.java
 * Author:   shenke
 * Date:     2018/5/30 下午5:53
 * Descriptor:
 */
package net.zdsoft.bigdata.data.enu;

/**
 * @author shenke
 * @since 2018/5/30 下午5:53
 */
public enum CockpitTemplateType {

    cockpit_1("/static/bigdata/images/cockpit_1.png"),
    cockpit_2("/static/bigdata/images/cockpit_2.png"),
    cockpit_3("/static/bigdata/images/cockpit_3.png"),
    cockpit_4("/static/bigdata/images/cockpit_4.png"),
    cockpit_5("/static/bigdata/images/cockpit_5.png"),
    cockpit_6("/static/bigdata/images/cockpit_6.png"),
    cockpit_7("/static/bigdata/images/cockpit_7.png"),
    cockpit_8("/static/bigdata/images/cockpit_8.png"),
    cockpit_9("/static/bigdata/images/cockpit_9.png");


    private String thumbnail;

    CockpitTemplateType(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getThumbnail() {
        return thumbnail;
    }
}
