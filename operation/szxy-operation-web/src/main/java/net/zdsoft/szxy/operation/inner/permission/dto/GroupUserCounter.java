package net.zdsoft.szxy.operation.inner.permission.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author shenke
 * @since 2019/4/10 下午2:58
 */
@Data
@AllArgsConstructor
public final class GroupUserCounter {

    private String groupId;
    private long count;
}
