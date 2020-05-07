package net.zdsoft.bigdata.v3.index;

import org.apache.commons.lang3.StringUtils;

/**
 * @author shenke
 * @since 2019/2/19 上午10:25
 */
public final class Templates {

    public static final String PATH = "/bigdata/v3/templates";

    public static String of(String template) {
        if (StringUtils.startsWith(template, "/")) {
            return PATH + template;
        } else {
            return PATH + "/" + template;
        }
    }
}
