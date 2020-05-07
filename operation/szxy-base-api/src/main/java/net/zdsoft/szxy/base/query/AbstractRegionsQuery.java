package net.zdsoft.szxy.base.query;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Set;

/**
 * @author shenke
 * @since 2019/4/16 下午3:54
 */
abstract class AbstractRegionsQuery implements Serializable {

    @Getter
    @Setter
    protected Set<String> regions;
}
