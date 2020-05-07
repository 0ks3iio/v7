/*
 * Project: v7
 * Author : shenke
 * @(#) SysOptionServiceImpl.java Created on 2016-8-25
 * @Copyright (c) 2016 ZDSoft Inc. All rights reserved
 */
package net.zdsoft.system.service.config.impl;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.config.Evn;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.utils.Objects;
import net.zdsoft.framework.utils.PassportClientUtils;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.system.config.PassportServerClient;
import net.zdsoft.system.constant.Constant;
import net.zdsoft.system.dao.config.SysOptionDao;
import net.zdsoft.system.entity.config.SysOption;
import net.zdsoft.system.entity.server.Server;
import net.zdsoft.system.service.config.SysOptionService;
import net.zdsoft.system.service.server.ServerService;

/**
 * @description: 系统参数
 * @author: shenke
 * @version: 1.0
 * @date: 2016-8-25下午5:52:21
 */
@Service("sysOptionService")
@Lazy(false)
public class SysOptionServiceImpl extends BaseServiceImpl<SysOption, String> implements SysOptionService {

    private Logger logger = Logger.getLogger(SysOptionServiceImpl.class);

    public static final String KEY = "sysOption.";
    @Autowired
    private SysOptionDao sysOptionDao;
    @Autowired
    private ServerService serverService;
    @Autowired
    private PassportServerClient passportServerClient;

    @PostConstruct
    public void initSystem() {
        //初始化passport参数
        try {
            Server server = serverService.findOneBy("code", Constant.EIS_RUN_CODE);
            if (server == null) {
                logger.error("base_server desktop7 不存在，" +
                        "请确保/opt/server_data/v7/framework.properties中passport相关参数正确，或deploy未执行");
                return;
            }
            String passportUrl = findValueByOptionCode(Constant.PASSPORT_URL);
            if (StringUtils.isBlank(passportUrl)) {
                logger.error("base_sys_option 中passport地址为空，请确保还未执行deploy");
                return;
            }
            System.setProperty("passport_url", passportUrl);

            net.zdsoft.passport.remoting.system.ServerService service = passportServerClient.getPassportServerService();
            Object sysServer = service.getServer(server.getId());
            if (sysServer == null) {
                logger.error("passport sys_server 不存在desktop7对应的记录，" +
                        "请确保/opt/server_data/v7/framework.properties中passport相关参数正确");
                return;
            }

            String serverId = Objects.getVaule(server.getId(), "");
            String serverKey = server.getServerKey();
            if (StringUtils.isBlank(serverKey)) {
                logger.error("desktop7 serverKey 为null 请确保语法执行正确" +
                        "或者/opt/server_data/v7/framework.properties中passport相关参数已修改正确");
                return;
            }
            System.setProperty("passport_verifyKey", serverKey);
            System.setProperty("passport_server_id", serverId);
            String console = "[oa7 passport地址: {0} ]\n" +
                    "[oa7 passportVerifyKey: {1}, server_id: {2}, default code: {3}]";

            System.out.println(MessageFormat.format(console,
                    passportUrl, serverKey, serverId, Constant.EIS_RUN_CODE));

            String passportSecondUrl = findValueByOptionCode(Constant.PASSPORT_SECOND_URL);
            System.out.println("[passportClient]--[SysOptionService init start]");
            //当passport内网地址部位空，且服务配置了内网地址，则初始化passport内网相关参数
            if (StringUtils.isNotBlank(server.getSecondDomain())
                    && StringUtils.isNotBlank(passportSecondUrl)) {
                PassportClientUtils.init(server.getSecondDomain(), passportSecondUrl);
                System.setProperty("eis.use.passport.second", Boolean.TRUE.toString());
                System.out.println("[passportClient 内网地址已初始化]");
            } else {
                //重新初始化passportClient
                PassportClientUtils.init(server.getSecondDomain(), passportSecondUrl);
            }
            System.out.println("[passportClient]--[SysOptionService init over]");
        } catch (Exception e) {
            logger.error("获取base_sys_option和sys_server passport相关参数失败，" +
                    "请确保还未执行deploy或者/opt/server_data/v7/framework.properties中passport相关参数正确");
        }
    }

    @Override
    public SysOption saveOne(final SysOption sysOption) {
        final SysOption save = sysOptionDao.save(sysOption);
        doCacheValueSave(sysOption.getOptionCode(), save);
        return save;
    }

    private void doCacheValueSave(final String optionCode, final SysOption save) {
        String value = null;
        if (save != null) {
            value = StringUtils.isNotBlank(save.getNowValue()) ? save.getNowValue() : save.getDefaultValue();
        }
        if (value != null) {
            RedisUtils.setObject(RedisUtils.getKey(SysOption.class, optionCode), value);
        }
    }

    @Override
    public void doRefreshCache(String... optionCode) {
        List<SysOption> list = sysOptionDao.findByOptionCode(optionCode);
        if (list == null) {
            list = new ArrayList<>();
        }
        refreshCache(list, optionCode);
    }

    private void refreshCache(List<SysOption> list, String... optionCode) {
        final Map<String, SysOption> map = new HashMap<String, SysOption>();
        for (SysOption sysOption : list) {
            map.put(sysOption.getOptionCode(), sysOption);
        }
        for (String string : optionCode) {
            final String optCode = string;
            final SysOption sysOption = map.get(optCode);
            doCacheValueSave(optCode, sysOption);
        }
    }

    @Override
    public void doRefreshCacheAll() {
        List<SysOption> list = sysOptionDao.findAll();
        if (list == null) {
            list = new ArrayList<>();
        }
        Set<String> optionCode = new HashSet<>();
        for (SysOption sysOption : list) {
            optionCode.add(sysOption.getOptionCode());
        }
        refreshCache(list, optionCode.toArray(new String[0]));
    }

    @Override
    public String findValueByOptionCode(final String optionCode) {
    	SysOption option = findOneByOptionCode(optionCode);
    	if(option != null)
    		return option.getNowValue();
    	else
    		return null;
    }

    @Override
	public SysOption findOneByOptionCode(String optionCode, SysOption defaultOption) {
    	if (StringUtils.isBlank(optionCode)) {
			return null;
    	}
    	return RedisUtils.getObject(RedisUtils.getKey(SysOption.class, optionCode), RedisUtils.TIME_FIVE_MINUTES, SysOption.class, () ->{
    		List<SysOption> sysOptions = sysOptionDao.findByOptionCode(optionCode);
    		if(CollectionUtils.isNotEmpty(sysOptions))
    			return sysOptions.get(0);
    		else
    			return defaultOption;    		
    	});
	}

	@Override
    public SysOption findOneByOptionCode(String optionCode) {
    	return findOneByOptionCode(optionCode, null);
    }

    @Override
    protected BaseJpaRepositoryDao<SysOption, String> getJpaDao() {
        return sysOptionDao;
    }

    @Override
    protected Class<SysOption> getEntityClass() {
        return SysOption.class;
    }

    @Override
    public void updateNowValueByCode(String nowValue, String code) {
        sysOptionDao.updateNowValueByCode(nowValue, code);
        RedisUtils.del(RedisUtils.getKey(SysOption.class, code));
    }

    @Override
    public void updateValueType(int valueType, String code) {
        sysOptionDao.updateValueType(valueType, code);
    }

    @Override
    public boolean isSecondUrl(String currentServerName) {
        String serverId = Evn.getString(net.zdsoft.framework.entity.Constant.PASSPORT_SERVER_ID);

        Server server = RedisUtils.getObject(net.zdsoft.framework.entity.Constant.PASSPORT_SERVER_ID + ".passport",
                60 * 60, Server.class, () -> serverService.findOne(NumberUtils.toInt(serverId)) );
        if ( server == null ) {
            logger.warn("根据" + net.zdsoft.framework.entity.Constant.PASSPORT_SERVER_ID +
                    "{" +serverId + "}获取base_server的值为NULL， 请确认不使用内外网和passport");
            return false;
        }
        String indexURL = server.getUrl();
        String secondIndexURL = server.getSecondUrl();
        if ( indexURL != null && indexURL.equalsIgnoreCase(secondIndexURL) ) {
            return false;
        }
        return secondIndexURL != null && currentServerName != null && secondIndexURL.indexOf(currentServerName) > 0;
    }

    /* 系统中最多存在两个FILE_URL,因此根据域名进行缓存不会有问题*/
    private Map<String, Boolean> secondUrlCache = new ConcurrentHashMap<String, Boolean>();

    @Override
    public String getFileUrl(String serverName) {

        Boolean isSecondUrl;
        isSecondUrl = serverName == null ? null :secondUrlCache.get(serverName);
        if ( isSecondUrl == null ) {
            isSecondUrl = isSecondUrl(serverName);
            if(serverName !=null)
            secondUrlCache.put(serverName, isSecondUrl);
        }

        if ( isSecondUrl ) {
            String secondUrl = findValueByOptionCode(Constant.FILE_SECOND_URL);
            return StringUtils.isBlank(secondUrl) ? findValueByOptionCode(Constant.FILE_URL) : secondUrl;
        }
        else {
            return findValueByOptionCode(Constant.FILE_URL);
        }
    }

    @Override
    public String getIndexUrl(String serverName) {
        Boolean isSecondUrl = secondUrlCache.get(serverName);
        if ( isSecondUrl == null ) {
            isSecondUrl = isSecondUrl(serverName);
            secondUrlCache.put(serverName, isSecondUrl);
        }
        return isSecondUrl ? findValueByOptionCode(Constant.INDEX_SECOND_URL) : findValueByOptionCode(Constant.INDEX_URL) ;
    }

    @Override
    public List<SysOption> findSysOptionByCodes(String[] codes) {
        return sysOptionDao.findByOptionCode(codes);
    }
}
