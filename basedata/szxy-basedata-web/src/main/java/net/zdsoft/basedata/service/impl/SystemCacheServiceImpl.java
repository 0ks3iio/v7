package net.zdsoft.basedata.service.impl;

import org.springframework.stereotype.Service;

import net.zdsoft.basedata.service.SystemCacheService;

@Service
public class SystemCacheServiceImpl implements SystemCacheService {

//    private static final Logger log = Logger.getLogger(SystemCacheServiceImpl.class);
//
//    @Autowired
//    private McodeRemoteService mcodeRemoteService;
//    @Autowired
//    private SysOptionService sysOptionService;
//    @Autowired
//    private SystemIniService systemIniService;
//    @Autowired
//    private ChartsService chartsService;
//
//    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//
//    @Override
//    public JSONObject clearBefore(String key) {
//        String findValue = systemIniService.findValue(SystemIniConstants.REDIS_ACTION_OPEN);
//        if ("1".equals(findValue)) {
//            if (StringUtils.isNotBlank(key)) {
//                RedisUtils.delBeginWith(key);
//                String msg = "清除以" + key + "开头的缓存成功" + sdf.format(new Date());
//                return doReturn(msg, true);
//            }
//            else {
//                return doReturn("key不能为空", false);
//            }
//        }
//        else {
//            return doReturn("非法操作" + sdf.format(new Date()), false);
//        }
//    }
//
//    private JSONObject doReturn(String msg, boolean isSuccess) {
//        log.info(msg);
//        JSONObject obj = new JSONObject();
//        obj.put("success", isSuccess);
//        obj.put("msg", msg);
//        return obj;
//    }
//
//    @Override
//    public JSONObject clearKey(String key) {
//        String findValue = systemIniService.findValue(SystemIniConstants.REDIS_ACTION_OPEN);
//        if ("1".equals(findValue)) {
//            if (StringUtils.isNotBlank(key)) {
//                RedisUtils.del(key);
//                String msg = "清除" + key + "的缓存成功" + sdf.format(new Date());
//                return doReturn(msg, true);
//            }
//            else {
//                return doReturn("key不能为空", false);
//            }
//        }
//        else {
//            return doReturn("非法操作" + sdf.format(new Date()), false);
//        }
//    }
//
//    @Override
//    public JSONObject refreshMcode(String mcodeId) {
//        String findValue = systemIniService.findValue(SystemIniConstants.REDIS_ACTION_OPEN);
//        if ("1".equals(findValue)) {
//            if (StringUtils.isNotBlank(mcodeId)) {
////                mcodeRemoteService.doRefreshCache(mcodeId);
//            }
//            else {
////                mcodeRemoteService.doRefreshCacheAll();
//            }
//            String msg = "刷新微代码缓存成功" + sdf.format(new Date());
//            return doReturn(msg, true);
//        }
//        else {
//            return doReturn("非法操作" + sdf.format(new Date()), false);
//        }
//    }
//
//    @Override
//    public JSONObject refreshBaseSysOption() {
//        String findValue = systemIniService.findValue(SystemIniConstants.REDIS_ACTION_OPEN);
//        if ("1".equals(findValue)) {
//            sysOptionService.doRefreshCacheAll();
//            String msg = "刷新BSysOpt开关缓存成功" + sdf.format(new Date());
//            return doReturn(msg, true);
//        }
//        else {
//            return doReturn("非法操作" + sdf.format(new Date()), false);
//        }
//    }
//
//    @Override
//    public JSONObject refreshSysOption() {
//        String findValue = systemIniService.findValue(SystemIniConstants.REDIS_ACTION_OPEN);
//        if ("1".equals(findValue)) {
//            systemIniService.doRefreshCacheAll();
//            String msg = "刷新SysOpt开关缓存成功" + sdf.format(new Date());
//            return doReturn(msg, true);
//        }
//        else {
//            return doReturn("非法操作" + sdf.format(new Date()), false);
//        }
//    }
//
//    @Override
//    public JSONObject refreshCharts() {
//        String findValue = systemIniService.findValue(SystemIniConstants.REDIS_ACTION_OPEN);
//        if ("1".equals(findValue)) {
//            chartsService.doRefreshCacheAll();
//            String msg = "刷新图表权限有关缓存成功" + sdf.format(new Date());
//            return doReturn(msg, true);
//        }
//        else {
//            return doReturn("非法操作" + sdf.format(new Date()), false);
//        }
//    }
//
//    @Override
//    public JSONObject refreshAction() {
//        systemIniService.doRefreshCache(SystemIniConstants.REDIS_ACTION_OPEN);
//        String msg = "刷新cacheAction开关缓存成功" + sdf.format(new Date());
//        return doReturn(msg, true);
//    }
//
//    @Override
//    public JSONObject closeAction() {
//        String findValue = systemIniService.findValue(SystemIniConstants.REDIS_ACTION_OPEN);
//        if ("1".equals(findValue)) {
//            SystemIni findOne = systemIniService.findOneByIniid(SystemIniConstants.REDIS_ACTION_OPEN);
//            if (findOne != null) {
//                findOne.setNowvalue("0");
//                systemIniService.saveOne(findOne);
//            }
//            String msg = "关闭cacheAction开关成功" + sdf.format(new Date());
//            return doReturn(msg, true);
//        }
//        else {
//            return doReturn("非法操作" + sdf.format(new Date()), false);
//        }
//    }

}
