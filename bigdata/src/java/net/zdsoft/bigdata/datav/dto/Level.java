package net.zdsoft.bigdata.datav.dto;

import java.util.Objects;

/**
 * @author shenke
 * @since 2019/5/20 下午5:40
 */
public interface Level extends Comparable<Level> {

    Integer getLevel();

    @Override
    default int compareTo(Level o) {
        if (Objects.isNull(o.getLevel())) {
            return  -1;
        }
        if (Objects.isNull(getLevel())) {
            return 1;
        }
        return o.getLevel() - getLevel();
    }
}
