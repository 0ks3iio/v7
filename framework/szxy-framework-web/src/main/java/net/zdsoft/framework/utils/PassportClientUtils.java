package net.zdsoft.framework.utils;

import net.zdsoft.framework.config.Evn;
import net.zdsoft.framework.entity.Constant;
import net.zdsoft.passport.service.client.PassportClient;
import net.zdsoft.passport.service.client.PassportClientParam;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

/**
 * @author shenke
 * @since 2017.09.26
 */
public class PassportClientUtils {

    private static Logger logger = Logger.getLogger(PassportClientUtils.class);

    public static PassportClient getPassportClient() {
        return PassportClient.getInstance();
    }

    public static void init(String secondDomain, String passportSecondUrl) {
        String passportVerifyKey = Evn.getString("passport_verifyKey");
        String passportUrl = Evn.getString(Constant.PASSPORT_URL);
        int passportServerId = Evn.getInt("passport_server_id");

        PassportClientParam param = new PassportClientParam(passportUrl, passportServerId, passportVerifyKey);
        if (StringUtils.isNotBlank(secondDomain)
                && StringUtils.isNotBlank(passportSecondUrl)) {
            param.addPassportURL(secondDomain, passportSecondUrl);
        } else {
            System.out.println("[passportClient]--[未初始化内网地址，请确认没有区分内外网]");
        }
        if (StringUtils.isBlank(passportUrl)) {
            System.out.println("[passportClient]--[passport地址为空，请确保还未执行部署工具passportClient未初始化]");
            return;
        }
        if (StringUtils.isBlank(passportVerifyKey)) {
            System.out.println("[passportClient]--[passportVerifyKey为空，请确保还未执行部署工具[passportClient未初始化]");
            return;
        }
        if (passportServerId == 0) {
            System.out.println("[passportClient]--[passportServerId是0,passportClient仍会初始化]");
        }
        PassportClient.getInstance().init(param);
    }
}
