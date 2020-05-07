package net.zdsoft.szxy.operation.inner.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author shenke
 * @since 2019/4/9 上午11:07
 */
@Getter
@Setter
public final class ManagementUser {

    private EditUser user;
    private List<String> groups;
    private List<EditUserModule> modules;
}
