package net.zdsoft.basedata.extension.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.extension.dao.ServerExtemsionDao;
import net.zdsoft.basedata.extension.entity.ServerExtension;
import net.zdsoft.basedata.extension.remote.service.ServerExtensionRemoteService;

/**
 * @author shenke
 * @since 2019/3/26 上午11:21
 */
@Service
public class ServerExtensionRemoteServiceImpl implements ServerExtensionRemoteService {

    @Resource
    private ServerExtemsionDao serverExtemsionDao;

    @Override
    public Set<String> getEnableServerCodesByUnitId(String unitId) {
        if (StringUtils.isBlank(unitId)) {
            return null;
        }
        ServerExtension extension = new ServerExtension();
        extension.setUnitId(unitId);
        boolean exists = serverExtemsionDao.exists(Example.of(extension, ExampleMatcher.matchingAll().withIgnoreNullValues()));
        if (!exists) {
            return null;
        }

        List<ServerExtension> serverExtensions = serverExtemsionDao.getEnableServerCodesByUnitId(unitId);
        final Date now = new Date();
        return serverExtensions.stream().filter(e -> {
            //正常
            if (ServerExtension.NATURE_OFFICIAL.equals(e.getUsingNature())) {
                if (ServerExtension.STATE_NORMAL.equals(e.getUsingState())) {
                    if (e.getExpireTime() == null) {
                        return true;
                    }
                    return now.before(e.getExpireTime());
                }
                return false;
            }
            //试用
            else {
                if (ServerExtension.STATE_NORMAL.equals(e.getUsingState())) {
                    if (e.getExpireTime() == null) {
                        return true;
                    }
                    return now.before(e.getExpireTime());
                }
                return false;
            }
        }).map(ServerExtension::getServerCode).collect(Collectors.toSet());
    }

	@Override
	public void saveAll(List<ServerExtension> serverExtension) {
		serverExtension.forEach(c->{
			Integer usingState = c.getUsingState() == null ? 
					c.getExpireTime() == null ? ServerExtension.STATE_NORMAL : ServerExtension.STATE_EXPIRE  : c.getUsingState();
			Integer usingNature = c.getUsingNature() == null ? ServerExtension.NATURE_OFFICIAL : c.getUsingNature();
			c.setUsingState(usingState);
			c.setUsingNature(usingNature);
		});
		serverExtemsionDao.saveAll(serverExtension);
	}

	@Override
	public List<ServerExtension> findByUnitIdIn(String... unitIds) {
		return serverExtemsionDao.findByUnitIdIn(unitIds);
	}

	@Override
	public void deleteByUnitIdIn(String... unitIds) {
		// TODO Auto-generated method stub
		serverExtemsionDao.deleteByUnitIdIn(unitIds);
	}
}
