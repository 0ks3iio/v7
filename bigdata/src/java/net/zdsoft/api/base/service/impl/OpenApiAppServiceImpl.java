package net.zdsoft.api.base.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import net.zdsoft.api.base.dao.OpenApiAppDao;
import net.zdsoft.api.base.dto.OpenApiDeveloperAppCounter;
import net.zdsoft.api.base.entity.eis.OpenApiApp;
import net.zdsoft.api.base.enums.AppStatusEnum;
import net.zdsoft.api.base.exception.OpenApiAppDeveloperExistsException;
import net.zdsoft.api.base.service.OpenApiAppService;
import net.zdsoft.framework.entity.Constant;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.system.remote.service.SysOptionRemoteService;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/**
 * @author shenke
 * @since 2019/5/21 上午11:02
 */
@Service("openApiAppService")
public class OpenApiAppServiceImpl implements OpenApiAppService {

    @Resource
    private OpenApiAppDao openApiAppDao;
    @Resource
    private SysOptionRemoteService sysOptionRemoteService;

    @Override
    public void addApp(OpenApiApp openApiApp) {
        if (StringUtils.isBlank(openApiApp.getId())) {
            openApiApp.setId(UuidUtils.generateUuid());
        }
        openApiAppDao.save(openApiApp);
    }

    @Transactional(rollbackFor = Throwable.class)
    @Override
    public void modifyAppStatus(AppStatusEnum appStatus, String id) {
        openApiAppDao.modifyAppStatus(appStatus.getValue(), id);
    }

    @Override
    public void modifyAppVisible(Integer visible, String appId) {

    }

    @Override
    public void bindAppDeveloper(String developerId, String appId) throws OpenApiAppDeveloperExistsException {
        Assert.notNull(developerId, "developerId can't null");
        OpenApiApp app = openApiAppDao.findById(appId).orElseThrow(() -> new RuntimeException(String.format("No OpenApiApp for %s", appId)));
        if (StringUtils.isNotBlank(app.getDeveloperId())) {
            if (!developerId.equals(app.getDeveloperId())) {
                throw new OpenApiAppDeveloperExistsException(String.format("OpenApiApp developerId already bind %s", app.getDeveloperId()));
            }
            return;
        }
        //bind
        openApiAppDao.bindDeveloperId(developerId, appId);
    }

    @Override
    public void deleteApp(String id) {
        openApiAppDao.deleteById(id);
    }

    @Override
    public List<OpenApiApp> getApps() {
        return openApiAppDao.findAll((Specification<OpenApiApp>) (root, criteriaQuery, criteriaBuilder) -> criteriaQuery.where(criteriaBuilder.equal(root.get("deleted"), 0), criteriaBuilder.equal(root.get("status"), AppStatusEnum.ONLINE.getValue())).getRestriction());
    }

    @Override
    public Page<OpenApiApp> getApps(Pageable pageable) {
        return openApiAppDao.findAll((Specification<OpenApiApp>) (root, criteriaQuery, criteriaBuilder) ->
                criteriaQuery.where(
                        criteriaBuilder.equal(root.get("deleted"), 0),
                        criteriaBuilder.notEqual(root.get("status"), AppStatusEnum.UNCOMMITTED.getValue())
                ).orderBy(criteriaBuilder.asc(root.get("name"))).getRestriction(), pageable);
    }

    @Override
	public Page<OpenApiApp> getAppsByApptype(Pageable pageable, String applyType) {
    	return openApiAppDao.findAll((Specification<OpenApiApp>) (root, criteriaQuery, criteriaBuilder) ->
        criteriaQuery.where(
                criteriaBuilder.equal(root.get("deleted"), 0),
                criteriaBuilder.notEqual(root.get("status"), AppStatusEnum.UNCOMMITTED.getValue()),
                criteriaBuilder.like(root.get("appTypes"), "%" + applyType + "%")
        ).orderBy(criteriaBuilder.asc(root.get("name"))).getRestriction(), pageable);
	}

    @Override
    public List<OpenApiApp> getAppsByDeveloperId(String developerId) {
        return openApiAppDao.getAppsByDeveloperId(developerId);
    }

    @Override
    public List<OpenApiDeveloperAppCounter> getDeveloperAppCounter(String[] developerIds) {
        if (ArrayUtils.isEmpty(developerIds)) {
            return new ArrayList<>();
        }
        return openApiAppDao.getDeveloperAppCounter(developerIds);
    }

    @Override
    public OpenApiApp getApp(String appId) {
        return openApiAppDao.findById(appId).orElse(null);
    }

    @Override
    public Boolean existsByAppName(String appName) {
        OpenApiApp app = new OpenApiApp();
        app.setName(appName);
        app.setDeleted(0);
        return openApiAppDao.exists(Example.of(app, ExampleMatcher.matchingAll().withIgnoreNullValues()));
    }


    @Override
    public String getAppHttpIconUrl(String id) {
        String url = openApiAppDao.getIconUrl(id);
        if (StringUtils.isBlank(url)) {
            return null;
        }
        return sysOptionRemoteService.findValue(Constant.FILE_URL) + "/" + url;
    }

	@Override
	public List<OpenApiApp> findByIds(String[] appIds) {
		return openApiAppDao.findByIds(appIds);
	}
}
