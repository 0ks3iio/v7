package net.zdsoft.szxy.operation.usermanage.controller.vo;

import lombok.Data;
import net.zdsoft.szxy.base.enu.UserStateCode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author shenke
 * @since 2019/1/31 下午4:32
 */
@Data
public final class UserStateVo {

    private final static List<UserStateVo> userStates = new ArrayList<>(4);

    public static List<UserStateVo> getUserStates() {
        //lazy create
        if (userStates.isEmpty()) {
            synchronized (userStates) {
                if (userStates.isEmpty()) {
                    //ArrayList use arrayCopy -> one operation
                    List<UserStateVo> userStateVos = new ArrayList<>(4);
                    userStateVos.add(new UserStateVo("未审核", UserStateCode.USER_MARK_UNCHECKED));
                    userStateVos.add(new UserStateVo("锁定", UserStateCode.USER_MARK_LOCK));
                    userStateVos.add(new UserStateVo("注销", UserStateCode.USER_MARK_LOGOUT));
                    userStateVos.add(new UserStateVo("正常", UserStateCode.USER_MARK_NORMAL));
                    userStates.addAll(userStateVos);
                }
            }
        }
        return userStates;
    }

    private UserStateVo(String humanText, Integer userState) {
        this.humanText = humanText;
        this.userState = userState;
    }

    private String humanText;
    private Integer userState;
}
