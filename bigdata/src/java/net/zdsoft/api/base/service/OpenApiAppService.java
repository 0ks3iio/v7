package net.zdsoft.api.base.service;

import net.zdsoft.api.base.dto.OpenApiDeveloperAppCounter;
import net.zdsoft.api.base.entity.eis.OpenApiApp;
import net.zdsoft.api.base.enums.AppStatusEnum;
import net.zdsoft.api.base.exception.OpenApiAppDeveloperExistsException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author shenke
 * @since 2019/5/21 上午11:02
 */
public interface OpenApiAppService {

    /**
     * 新增ap
     */
    void addApp(OpenApiApp openApiApp);

    /**
     * 更新ap状态
     */
    void modifyAppStatus(AppStatusEnum appStatus, String id);

    /**
     * 将Ap和开发者绑定
     */
    void bindAppDeveloper(String developerId, String appId) throws OpenApiAppDeveloperExistsException;

    /**
     * 变更应用可见状态
     */
    void modifyAppVisible(Integer visible, String appId);

    /**
     * 删除Ap
     */
    void deleteApp(String id);

    //*******************************Query method*******************************\\

    /**
     * 查询所有Ap
     */
    List<OpenApiApp> getApps();

    /**
     * 分页查询所有Ap
     */
    Page<OpenApiApp> getApps(Pageable pageable);

    /**
     * 查询指定开发者的Ap
     */
    List<OpenApiApp> getAppsByDeveloperId(String developerId);

    /**
     * 获取指定开发者的应用数量信息
     */
    List<OpenApiDeveloperAppCounter> getDeveloperAppCounter(String[] developerIds);

    /**
     * 查询指定ID的Ap信息
     */
    OpenApiApp getApp(String appId);

    /**
     * 检查指定AppName是否存在
     */
    Boolean existsByAppName(String appName);

    /**
     * 获取指定Ap的图标地址 http
     * @param id  主键
     * @return
     */
    String getAppHttpIconUrl(String id);

    /**
     * 根据应用用途来获取数据
     * @param pageable
     * @param applyType
     * @return
     */
	Page<OpenApiApp> getAppsByApptype(Pageable pageable, String applyType);

	/**
	 * 查询指定IDs的Ap信息
	 * @param appIds
	 */
	List<OpenApiApp> findByIds(String[] appIds);
}
