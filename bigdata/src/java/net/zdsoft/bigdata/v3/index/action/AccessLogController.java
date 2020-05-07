package net.zdsoft.bigdata.v3.index.action;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import net.zdsoft.bigdata.data.vo.Response;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.entity.LoginInfo;
import net.zdsoft.framework.utils.DateUtils;
import net.zdsoft.framework.utils.RedisUtils;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;

/**
 * @author shenke
 * @since 2019/3/4 上午9:35
 */
@RestController
@RequestMapping("/bigdata/accessLog")
public class AccessLogController extends BigdataBaseAction {


    @PostMapping("")
    public Response doLogAccess(String type, String value, Long length) {

        String luaScript = "local length=redis.call('lpush', KEYS[1], ARGV[1]) if (length>%s) then redis.call('ltrim',KEYS[1], 0, %s); end;";
        luaScript = String.format(luaScript, length, length - 1);
        RedisUtils.eval(luaScript, getAccessId() + type, Collections.singletonList(value));
        Json data=Json.parseObject(value, Json.class);
        data.put("log_time", DateUtils.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
        RedisUtils.lpush("bgmoduleOperationLog", JSONObject.toJSONString(data));
        return Response.ok().build();
    }

    @GetMapping("")
    public Response doGetAccessLog(String type) {
        List<String> datas = RedisUtils.lrange(getAccessId() + type);
        return Response.ok().data(datas).build();
    }

    private String getAccessId() {
        LoginInfo loginInfo = getLoginInfo();
        if (loginInfo != null) {
            return loginInfo.getUserId();
        }
        return null;
    }
}
