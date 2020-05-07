package net.zdsoft.szxy.base.service;

import net.zdsoft.szxy.base.api.ServerRemoteService;
import net.zdsoft.szxy.base.dao.ServerDao;
import net.zdsoft.szxy.base.dao.ServerExtensionDao;
import net.zdsoft.szxy.base.entity.Server;
import net.zdsoft.szxy.base.entity.ServerExtension;
import net.zdsoft.szxy.base.enu.ServerExtensionState;
import net.zdsoft.szxy.monitor.Record;
import net.zdsoft.szxy.monitor.RecordType;
import net.zdsoft.szxy.utils.AssertUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author shenke
 * @since 2019/3/22 下午1:41
 */
@Service("serverRemoteService")
public class ServerServiceImpl implements ServerRemoteService {

    @Resource
    private ServerExtensionDao serverExtensionDao;
    @Resource
    private ServerDao serverDao;

    @Override
    public List<Server> getEnableServersForPlatform() {
        return serverDao.getServersForPlatform();
    }

    @Record(type = RecordType.Call)
    @Override
    public List<Server> getEnableServersByUnitId(String unitId) {
        List<ServerExtension> extensions = serverExtensionDao.getExtensionsByUnitId(unitId);
        //兼容老版本的情况，之前没有按照运营平台的理念走的话则不会有扩展表的数据，这个时候仍然按照原有的逻辑走
        List<Server> servers = serverDao.getServersForPlatform();
        if (extensions.isEmpty()) {
            return servers;
        }

        final Date currentTime = new Date();
        Set<String> extensionCodes = extensions.stream().filter(e -> {
            //非正常状态
            if (!ServerExtensionState.NORMAL.equals(e.getUsingState())) {
                return false;
            }
            //正常状态
            if (e.getExpireTime() == null) {
                return true;
            }
            return e.getExpireTime().after(currentTime);
        }).map(ServerExtension::getServerCode).collect(Collectors.toSet());

        return servers.stream().filter(e -> extensionCodes.contains(e.getCode())).collect(Collectors.toList());
    }

    @Record(type = RecordType.Call)
    @Override
    public List<Server> getServersBySubId(Integer[] subIds) {
        AssertUtils.notNull(subIds, "subIds can't null");
        AssertUtils.isTrue(subIds.length > 0, "subIds can't empty");
        return serverDao.getServersBySubId(subIds);
    }
}
