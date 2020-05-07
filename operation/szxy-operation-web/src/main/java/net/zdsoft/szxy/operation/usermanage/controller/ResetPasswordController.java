package net.zdsoft.szxy.operation.usermanage.controller;

import net.zdsoft.szxy.base.api.UserRemoteService;
import net.zdsoft.szxy.base.entity.User;
import net.zdsoft.szxy.base.exception.SzxyPassportException;
import net.zdsoft.szxy.monitor.Record;
import net.zdsoft.szxy.monitor.RecordType;
import net.zdsoft.szxy.operation.usermanage.UserOperateCode;
import net.zdsoft.szxy.plugin.mvc.Response;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author shenke
 * @since 2019/4/16 下午2:32
 */
@RestController
@RequestMapping("/operation/user/manage/reset-password")
public class ResetPasswordController {

    private static final String USER_DEFAULT_PASSWORD = "12345678";

    @Resource
    private UserRemoteService userRemoteService;

    @Record(type = RecordType.URL)
    @Secured(UserOperateCode.USER_RESET_PASSWORD)
    @PostMapping("")
    public Response execute(@RequestParam("userIds[]") String[] userIds) {
        try {
            if (ArrayUtils.isEmpty(userIds)) {
                return Response.error().message("ownerIds为空").build();
            }
            //TODO 以后应该按照不同的单位获取不同的默认密码规则
            userRemoteService.batchUpdatePassword(userIds, USER_DEFAULT_PASSWORD);
        } catch (SzxyPassportException e) {
            return Response.error().message(e.getMessage()).build();
        }
        return Response.ok().message("密码重置成功：【%s】", USER_DEFAULT_PASSWORD).build();
    }

    @Record(type = RecordType.URL)
    @Secured(UserOperateCode.USER_RESET_PASSWORD)
    @PostMapping("/byOwnerIds")
    public Response executeByOwnerId(@RequestParam("ownerIds[]") String[] studentIds) {
        if (ArrayUtils.isEmpty(studentIds)) {
            return Response.error().message("ownerIds为空").build();
        }
        String[] userIds = userRemoteService.getUsersByOwnerIds(studentIds).stream().map(User::getId).toArray(String[]::new);
        if (ArrayUtils.isEmpty(userIds)) {
            return Response.error().message("没有对应的用户信息，无法重置密码").build();
        }
        return execute(userIds);
    }
}
