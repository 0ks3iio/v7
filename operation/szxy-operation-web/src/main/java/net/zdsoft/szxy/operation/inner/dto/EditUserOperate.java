package net.zdsoft.szxy.operation.inner.dto;

import lombok.Data;

/**
 * @author shenke
 * @since 2019/4/9 下午1:57
 */
@Data
public final class EditUserOperate {

    private String id;
    private String operateName;
    private boolean hasAuth;
}
