package net.zdsoft.api.openapi.remote.openapi.action;

import net.zdsoft.api.openapi.remote.openapi.utils.VerifyCoder;
import net.zdsoft.bigdata.data.vo.Response;
import net.zdsoft.framework.utils.PWD;
import net.zdsoft.framework.utils.RedisUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.stereotype.Controller;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @author shenke
 */
@Controller
@RequestMapping("/bigdata/api/verify-code")
public class VerifyCodeAction {

    public static final String SALT = "ZdSoft";
    private static final String ITEMS = "123456789abcdefghjkmnprstuvwxyzABCDEFGHJKLMNPRSTUVWXYZ";


    @GetMapping(value = "/get")
    public void doGetVerifyCode(@RequestParam("key") String key, HttpServletRequest request, HttpServletResponse response) {
        String decodeKey = PWD.decode(key);
        if (!decodeKey.startsWith(SALT)) {
            throw new RuntimeException("Key error");
        }
        VerifyCoder coder = new VerifyCoder(32, 86, new Font("", 0, 25), Color.WHITE, Color.LIGHT_GRAY);
        try {
            OutputStream outputStream = response.getOutputStream();
            String verifyCode = getVerifyCodeString();
            RedisUtils.set(key, verifyCode, 5 *  60);
            coder.export(verifyCode, outputStream);
        } catch (IOException e) {
            ReflectionUtils.rethrowRuntimeException(e);
        }
    }

    @ResponseBody
    @GetMapping("validate")
    public Object doValidate(@RequestParam("key") String key, @RequestParam("verifyCode") String code) {
        String decodeKey = PWD.decode(key);
        if (!decodeKey.startsWith(SALT)) {
            return Response.error().message("Key-error").build();
        }
        String verifyCode = RedisUtils.get(key);
        if (StringUtils.isBlank(verifyCode)) {
            return Response.error().message("验证码已过期，请重新生成").build();
        }
        if (verifyCode.equalsIgnoreCase(code)) {
            return Response.ok().build();
        }
        return Response.error().message("验证码错误").build();
    }

    private String getVerifyCodeString() {
        StringBuilder code = new StringBuilder();
        for( int i=0; i<4; i++){
            code.append(ITEMS.charAt(RandomUtils.nextInt(0,ITEMS.length()-1)));
        }
        return code.toString();
    }
}
