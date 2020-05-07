/* 
 * @(#)ServerServiceImpl.java    Created on 2017年2月27日
 * Copyright (c) 2017 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package net.zdsoft.system.service.server.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.config.Evn;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.StringUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.keel.dao.DataExistsException;
import net.zdsoft.passport.remoting.PassportRemotingException;
import net.zdsoft.passport.remoting.system.ServerDto;
import net.zdsoft.passport.remoting.system.ServerTypeDto;
import net.zdsoft.system.config.PassportServerClient;
import net.zdsoft.system.constant.Constant;
import net.zdsoft.system.dao.WareDao;
import net.zdsoft.system.dao.server.ModelDao;
import net.zdsoft.system.dao.server.ServerDao;
import net.zdsoft.system.entity.Ware;
import net.zdsoft.system.entity.server.Model;
import net.zdsoft.system.entity.server.Server;
import net.zdsoft.system.enums.YesNoEnum;
import net.zdsoft.system.enums.server.AppStatusEnum;
import net.zdsoft.system.enums.unit.UnitClassEnum;
import net.zdsoft.system.enums.user.UserTypeEnum;
import net.zdsoft.system.service.config.SysOptionService;
import net.zdsoft.system.service.server.ServerService;

/**
 * @author cuimq
 * @version $Revision: 1.0 $, $Date: 2017年2月27日 上午11:13:49 $
 */
@Service("serverService")
public class ServerServiceImpl extends BaseServiceImpl<Server, Integer> implements ServerService {

    private Logger logger = LoggerFactory.getLogger(ServerServiceImpl.class);

    @Autowired
    private ServerDao serverDao;
    @Autowired
    private SysOptionService sysOptionService;
    @Autowired
    private ModelDao modelDao;
    @Autowired
    private PassportServerClient passportServerClient;
    @Autowired
    private WareDao wareDao;

    @Override
    protected BaseJpaRepositoryDao<Server, Integer> getJpaDao() {
        return serverDao;
    }

    @Override
    protected Class<Server> getEntityClass() {
        return Server.class;
    }

    
	@Override
	public Integer convertKey(Object key) {
		return Integer.valueOf(String.valueOf(key));
	}

	@Override
    public List<Server> findByOrderType(Integer[] orderTypes, int status) {
        return serverDao.findByOrderType(orderTypes, status);
    }

    @Override
    public Map<Integer, Server> getOnLineServerMap(Integer[] ids) {
        Map<Integer, Server> onlineServerMap = new HashMap<Integer, Server>();
        List<Server> serverList = serverDao.findServerList(ids, AppStatusEnum.ONLINE.getValue());
        if (CollectionUtils.isNotEmpty(serverList)) {
            for (Server server : serverList) {
                onlineServerMap.put(server.getId(), server);
            }
        }
        return onlineServerMap;
    }

    @Override
    public List<Server> findServerList(final Integer[] orderTypes, final Integer unitClass, final int status,
                                       final String sections, final int userType, final Integer[] serverIds, final Integer isVisible,
                                       final String subIdCondition) {

        Specification<Server> specification = new Specification<Server>() {

            @Override
            public Predicate toPredicate(Root<Server> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                List<Predicate> ps = new ArrayList<Predicate>();

                ps.add(cb.equal(root.get("isDeleted").as(Integer.class), 0));
                ps.add(root.get("orderType").as(Integer.class).in(orderTypes));
                ps.add(cb.equal(root.get("status").as(Integer.class), status));
                if (null != isVisible) {
                    ps.add(cb.equal(root.get("isVisible").as(Integer.class), isVisible));
                }
                if (null != subIdCondition) {
                    if (subIdCondition.endsWith("isNull")) {
                        ps.add(cb.isNull(root.get("subId").as(Integer.class)));
                    }
                    else if (subIdCondition.endsWith("isNotNull")) {
                        ps.add(cb.isNotNull(root.get("subId").as(Integer.class)));
                    }
                }

                if (null != unitClass) {
                    ps.add(cb.like(root.get("unittype").as(String.class), "%" + unitClass + "%"));
                }

                if (StringUtils.isNotEmpty(sections)) {
                    String[] sectionAry = sections.split(",");
                    List<Predicate> sectionsPs = new ArrayList<Predicate>();
                    for (String section : sectionAry) {
                        sectionsPs.add(cb.like(root.get("sections").as(String.class), "%" + section + "%"));
                    }
                    ps.add(cb.or(sectionsPs.toArray(new Predicate[sectionsPs.size()])));
                }

                if (null != serverIds && serverIds.length > 0) {
                    ps.add(root.get("id").as(Integer.class).in(serverIds));
                }

                if (userType != 0) {
                    ps.add(cb.like(root.get("usertype").as(String.class), "%" + userType + "%"));
                }

                List<Order> orderList = new ArrayList<Order>();
                orderList.add(cb.asc(root.get("name").as(String.class)));

                cq.where(ps.toArray(new Predicate[0])).orderBy(orderList);
                return cq.getRestriction();
            }

        };

        return serverDao.findAll(specification);
    }

    @Override
    public List<Server> getAllApps(final String appName, final Integer status, final Integer source,
                                   final Date startTime, final Date endTime, Pagination page) {
        Specification<Server> specification = new Specification<Server>() {
            @Override
            public Predicate toPredicate(Root<Server> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                List<Predicate> ps = new ArrayList<Predicate>();

                ps.add(cb.equal(root.get("isDeleted").as(Integer.class), 0));
                ps.add(cb.equal(root.get("isVisible").as(Integer.class), 1));
                if (status != null && status > -1) {
                    ps.add(cb.equal(root.get("status").as(Integer.class), status));
                }

                if (source != null && source > -1) {
                    ps.add(cb.equal(root.get("serverClass").as(Integer.class), source));
                }

                if (StringUtils.isNotEmpty(appName)) {
                    ps.add(cb.like(root.get("name").as(String.class), "%" + appName + "%"));
                }

                // if (startTime != null && endTime != null) {
                // ps.add(cb.between(root.get("modifyTime").as(Date.class), startTime, endTime));
                // }

                if (startTime != null) {
                    ps.add(cb.greaterThanOrEqualTo(root.get("modifyTime").as(Date.class), startTime));
                }
                if (endTime != null) {
                    ps.add(cb.lessThanOrEqualTo(root.get("modifyTime").as(Date.class), endTime));
                }

                List<Order> orderList = new ArrayList<Order>();
                orderList.add(cb.desc(root.get("status").as(Integer.class)));
                orderList.add(cb.desc(root.get("orderId").as(Integer.class)));

                cq.where(ps.toArray(new Predicate[0])).orderBy(orderList);
                return cq.getRestriction();
            }
        };

        if (page != null) {
            Pageable pageable = Pagination.toPageable(page);
            Page<Server> findAll = serverDao.findAll(specification, pageable);
            page.setMaxRowCount((int) findAll.getTotalElements());

            return findAll.getContent();
        }
        else {
            return serverDao.findAll(specification);
        }
    }

    @Override
    public List<Server> findByOrderTypeAndStatus(Integer[] orderTypes, int status) {
        return serverDao.findByOrderTypeAndStatus(orderTypes, status);
    }

    @Override
    public Map<String, List<String>> getAppNamesByDevIds(String[] devIds) {
        List<Server> apps = serverDao.findAppsByDevIds(devIds);
        if (CollectionUtils.isEmpty(apps)) {
            return Collections.emptyMap();
        }

        Map<String, List<String>> resultMap = new HashMap<String, List<String>>();
        for (Server app : apps) {
            String devId = app.getDevId();
            List<String> appNames = resultMap.get(devId);
            if (appNames == null) {
                appNames = new ArrayList<String>();
            }
            appNames.add(app.getName());
            resultMap.put(devId, appNames);
        }

        return resultMap;
    }

    @Override
    public List<Server> getAppsByDevId(String devId) {
        List<Server> servers = serverDao.findServersByDevId(devId);
        String serverName = Evn.getRequest() == null ? null : Evn.getRequest().getServerName();
        String fileUrl = sysOptionService.getFileUrl(serverName);
        // String fileUrl = "http://www.file.dev/file";
        for (Server server : servers) {
            server.setFullIcon(fixDoamin(server.getIconUrl(), fileUrl));
        }
        return serverDao.findServersByDevId(devId);
    }

    private String fixDoamin(String url, String fileUrl) {
        if (StringUtils.isEmpty(url)) {
            return "";
        }

        if (!url.startsWith("http")) {
            url = fileUrl + url;
        }
        return url;
    }

    @Override
    public void removeApp(String systemId) {
        serverDao.deleteApp(systemId);
    }

    @Override
    public void removeAppByAppId(int appId) {
        serverDao.deleteAppByAppId(appId);
    }

    @Override
    public Server getAppBySystemId(String systemId) {
        return serverDao.findAppById(systemId);
    }

    @Override
    public Server getAppByAppId(int appId) {
        return serverDao.findById(appId).orElse(null);
    }

    @Override
    public void updateAppStatusBySystemId(int status, String systemId) {
        if (status == AppStatusEnum.AUDIT.getValue()) {
            serverDao.updateAppStatusAndModifyTimeAndApplyTime(status, new Date(), new Date(), systemId);
        }
        else {
            serverDao.updateAppStatus(status, systemId);
        }
    }

    @Override
    public void updateAppStatusByAppId(int status, int appId) {
        if (status == AppStatusEnum.AUDIT.getValue()) {
            serverDao.updateAppStatusAndModifyTimeAndApplyTime(status, new Date(), new Date(), appId);
        }
        else if (status == AppStatusEnum.ONLINE.getValue()) {
            serverDao.updateAppStatusAndOnlineTime(status, new Date(), appId);
        }
        else if (status == AppStatusEnum.PASS.getValue()) {// 通过即为下线状态
            serverDao.updateAppStatusAndAuditTime(AppStatusEnum.OFFLINE.getValue(), new Date(), appId);
        }
        else if (status == AppStatusEnum.START.getValue()) {// 启用即为上线状态
            serverDao.updateAppStatusAndModifyTime(AppStatusEnum.ONLINE.getValue(), new Date(), appId);
        }
        else {
            serverDao.updateAppStatus(status, appId);
        }
    }

    /*
     * @Override public void updateAppPassStatus(Server server) { this.updateAppInfoByAppId(server);
     * this.updateAppStatusByAppId(AppStatusEnum.PASS.getValue(), server.getId()); }
     */

    @Override
    public int getAppCountByName(String appName) {
        return serverDao.countAppByName(appName);
    }

    @Override
    public Server addApp(Server server) {
        int serverTypeId = 0;
        synchronized (this) {
            serverTypeId = serverDao.getMaxServerTypeId() + 1;
        }
        String regionCode = sysOptionService.findValueByOptionCode(Constant.REGION_CODE);
        if (StringUtils.isEmpty(regionCode)) {
            regionCode = "000000";
        }
        if (server.getId() == null) {
            int id = Integer.valueOf(serverTypeId + regionCode.substring(0, 2) + "01");
            server.setId(id);
            // server.setSubId(id);
        }
        server.setServerTypeId(serverTypeId);
        server.setServerKey(UuidUtils.generateUuid());
        server.setIsDeleted(YesNoEnum.NO.getValue());
        server.setEventSource(YesNoEnum.NO.getValue());
        server.setIsPassport(YesNoEnum.YES.getValue());
        server.setIsVisible(YesNoEnum.YES.getValue());
        server.setModifyTime(new Date());
        return serverDao.save(server);
    }

    @Override
    public void updateAppInfoForOpenapi(Server server) {
        if (server.getStatus() == AppStatusEnum.OFFLINE.getValue()) {// 开发者如果是未上线状态修改，则需要重新变更为审核中
            serverDao.updateAppStatus(AppStatusEnum.AUDIT.getValue(), server.getSystemId());
        }
        serverDao.updateAppInfo(server.getName(), server.getDescription(), server.getIcon(), server.getIconUrl(),
                server.getIndexUrl(), server.getVerifyUrl(), server.getInvalidateUrl(), server.getUnittype(),
                server.getUsertype(), server.getSections(), server.getModifyTime(), server.getProtocol(),
                server.getDomain(), server.getPort(), server.getContext(), server.getSystemId());

    }

    @Override
    public void updateAppInfoByAppId(Server server) {
        if(server.getEventSource() == null)
            server.setEventSource(1);
        if (server.getOrderType() == null) {
            serverDao.updateAppInfo(server.getName(), server.getDescription(), server.getIcon(), server.getIconUrl(),
                    server.getIndexUrl(), server.getVerifyUrl(), server.getInvalidateUrl(), server.getUnittype(),
                    server.getUsertype(), server.getSections(), server.getModifyTime(), server.getProtocol(),
                    server.getDomain(), server.getPort(), server.getContext(), server.getStatus(),
                    server.getOpenType(), server.getId());
        }
        else {
            serverDao.updateAppInfo(server.getName(), server.getDescription(), server.getIcon(), server.getIconUrl(),
                    server.getIndexUrl(), server.getVerifyUrl(), server.getInvalidateUrl(), server.getUnittype(),
                    server.getUsertype(), server.getSections(), server.getModifyTime(), server.getOrderType(),
                    server.getProtocol(), server.getDomain(), server.getPort(), server.getContext(),
                    server.getStatus(), server.getOpenType(), server.getId());
        }
    }

    @Override
    public void updateEnableServer(String[] codes) {
        // select * from
        //停用内部应用
        serverDao.updateAllInnerServerStop();
        modelDao.updateAllModelStop();

        //过滤非内部子系统
        List<Server> allServer = findListByIn("code", codes);
        final Set<Integer> allSubIdSet = EntityUtils.getSet(allServer, "subId");
        Integer[] subIdArray = allSubIdSet.toArray(new Integer[allSubIdSet.size()]);
        subIdArray = EntityUtils.removeEmptyElement(subIdArray, Integer.class);
        List<Model> allModels = modelDao.findBySubSystemIn(subIdArray);
        Set<Integer> subSystemSet = EntityUtils.getSet(allModels, "subSystem");
        allSubIdSet.removeAll(subSystemSet); //剩下172.16.14.5:8073

        if (Evn.isPassport()) {
            //初始化passport
            init2Passport(allServer);
        }

        List<Server> innerServerList = EntityUtils.filter(allServer, new EntityUtils.Filter<Server>() {
            @Override
            public boolean doFilter(Server server) {
                return allSubIdSet.contains(server.getSubId());
            }
        });
        //更新类型为内部应用
//        serverDao.updateServerClassToInner(EntityUtils.toArray(EntityUtils.<String, Server>getList(innerServerList, "code"), String.class));
        serverDao.updateServerClassToInner(innerServerList.stream().map(Server::getCode).toArray(String[]::new));
        //激活子系统
        serverDao.updateInnerServerActiveByCodes(codes);
        //激活模块
        modelDao.updateModelActiveBySubIds(EntityUtils.toArray(subSystemSet, Integer.class));


    }


    private void init2Passport(List<Server> allServer) {

        //初始化sys_server_type
        List<ServerTypeDto> serverTypeList = Lists.newArrayList();
        //初始化passport sys_server
        List<ServerDto> sysServerList = Lists.newArrayList();
        int cooperatorId = NumberUtils.toInt(sysOptionService.findValueByOptionCode(Constant.COOPERATOR_ID));
        for (Server server : allServer) {
            ServerDto serverDto = convertToSysServer(server);
            serverDto.setCooperatorId(cooperatorId);
            if (server.getSubId() != null) {
                sysServerList.add(serverDto);
                serverTypeList.add(initServerType(server));
            }
        }
        net.zdsoft.passport.remoting.system.ServerService passportServerService = passportServerClient.getPassportServerService();

        try {
            passportServerService.addServerTypes(EntityUtils.toArray(serverTypeList, ServerTypeDto.class));
            passportServerService.addServers(EntityUtils.toArray(sysServerList, ServerDto.class));
        } catch (Exception e){
            addOneByOne(serverTypeList, sysServerList);
        }

    }

    private void addOneByOne(List<ServerTypeDto> serverTypeList, List<ServerDto> sysServerList) {
        net.zdsoft.passport.remoting.system.ServerService passportServerService = passportServerClient.getPassportServerService();
        for (ServerTypeDto serverTypeDto : serverTypeList) {
            try {
                passportServerService.addServerTypes(serverTypeDto);
            } catch (Exception e) {
                if ( e instanceof DataExistsException) {
                    if (org.apache.commons.lang3.StringUtils.isNotBlank(serverTypeDto.getInvalidatePath())
                            && org.apache.commons.lang3.StringUtils.isNotBlank(serverTypeDto.getVerifyPath())) {
                        passportServerService.modifyServerTypes(serverTypeDto);
                        logger.error("serverType {}, verifyUrl {}", serverTypeDto.getId(), serverTypeDto.getVerifyPath());
                    }
                    System.out.println(serverTypeDto.getInvalidatePath() + "_" + serverTypeDto.getVerifyPath() + "---" + serverTypeDto.getId());
                } else {
                    throw new PassportRemotingException(e.getMessage());
                }
            }
        }
        for (ServerDto serverDto : sysServerList) {
            try {
                passportServerService.addServer(serverDto);
            } catch (Exception e){
                if ( e instanceof DataExistsException ) {
                    passportServerService.modifyServer(serverDto);
                } else {
                    throw new PassportRemotingException(e.getMessage());
                }
            }
        }
    }

    private static final String VERIFY_PATH = "/fpf/login/verify.action";
    private static final String INVALIDATE_PATH = "/fpf/login/invalidate.action";
    private ServerTypeDto initServerType (Server server) {
        ServerTypeDto serverType = new ServerTypeDto();
        serverType.setCharsetName("UTF-8");
        serverType.setId(server.getServerTypeId());
        serverType.setName(server.getName());
        serverType.setFreeLoginAllowed(Boolean.TRUE);
        if (server.getSubId() != null) {
            String invalidatePath = org.apache.commons.lang3.StringUtils.isBlank(server.getInvalidateUrl()) ? INVALIDATE_PATH : server.getInvalidateUrl();
            String verifyPath = org.apache.commons.lang3.StringUtils.isBlank(server.getVerifyUrl()) ? VERIFY_PATH : server.getVerifyUrl();
            serverType.setInvalidatePath(invalidatePath);
            serverType.setVerifyPath(verifyPath);
        }
        else {
            if (org.apache.commons.lang3.StringUtils.isNotBlank(server.getInvalidateUrl())
                    && org.apache.commons.lang3.StringUtils.isNotBlank(server.getVerifyUrl())) {
                serverType.setInvalidatePath(server.getInvalidateUrl());
                serverType.setVerifyPath(server.getVerifyUrl());
                System.out.println(server.getVerifyUrl() + "_" + server.getInvalidateUrl() + "---" + server.getServerTypeId());
            }
        }
        return  serverType;
    }


    @Override
    public void updateAppAndRegisterPassPort(Server server) {
        this.updateAppInfoByAppId(server);
        addWare(server);
        registerPassPort(server);

        // if (server.getServerClass() == ServerClassEnum.INNER_PRODUCT.getValue()) {
        // return;
        // }
        // String unitType = server.getUnittype();// 适用单位
        // String[] unitTypeArry = StringUtils.split(unitType, ',');
        //
        // boolean isContainsEducation = ArrayUtils.contains(unitTypeArry,
        // String.valueOf(UnitClassEnum.EDUCATION.getValue()));
        // boolean isContainsSchool = ArrayUtils.contains(unitTypeArry,
        // String.valueOf(UnitClassEnum.SCHOOL.getValue()));
        // List<Model> educationModels = modelDao.findModelBySubSystemAndUnitClass(server.getId(),
        // UnitClassEnum.EDUCATION.getValue());
        // List<Model> schoolModels = modelDao.findModelBySubSystemAndUnitClass(server.getId(),
        // UnitClassEnum.SCHOOL.getValue());
        //
        // updateEducationDefaultModel(educationModels, isContainsEducation, server);
        // updateSchoolDefaultModel(schoolModels, isContainsSchool, server);
    }

    @Override
    public Server addAppAndRegisterPassPort(Server server) {
        Server resultServer = this.addApp(server);
        addWare(resultServer);
        registerPassPort(resultServer);
        // String unitType = server.getUnittype();// 适用单位
        // String[] unitTypeArry = StringUtils.split(unitType, ',');
        // boolean isContainsEducation = ArrayUtils.contains(unitTypeArry,
        // String.valueOf(UnitClassEnum.EDUCATION.getValue()));
        // boolean isContainsSchool = ArrayUtils.contains(unitTypeArry,
        // String.valueOf(UnitClassEnum.SCHOOL.getValue()));
        // updateEducationDefaultModel(null, isContainsEducation, resultServer);
        // updateSchoolDefaultModel(null, isContainsSchool, resultServer);
        return resultServer;
    }

    private void addWare(Server server) {
        // String isAddBaseWare = sysOptionService.findValueByOptionCode(Constant.IS_ADD_BASE_WARE);
        // if (StringUtils.isNotEmpty(isAddBaseWare) && isAddBaseWare.equals("1")) {
        Ware ware = wareDao.findByServerId(server.getId());
        if (null == ware) {
            wareDao.save(packageWare(server));
        }
        // }
    }

    private Ware packageWare(Server server) {
        Ware ware = new Ware();
        ware.setId(UuidUtils.generateUuid());
        ware.setCode(server.getCode());
        ware.setName(server.getName());
        ware.setState(YesNoEnum.YES.getValue());
        ware.setWareFee(0);
        ware.setServerId(server.getId());
        ware.setServerTypeId(server.getServerTypeId());
        ware.setSubscriberType(1);
        ware.setNums(0);
        ware.setOrderType(4);// 默认免费
        ware.setUnitClass(server.getUnittype());
        ware.setRole(getWareRole(server));
        ware.setExperienceMonth(0);
        ware.setIsFee(0);
        ware.setTeacherRule(getRule(UserTypeEnum.TEACHER.getValue(), server.getUsertype()));
        ware.setStudentRule(getRule(UserTypeEnum.STUDENT.getValue(), server.getUsertype()));
        ware.setFamilyRule(getRule(UserTypeEnum.PARENT.getValue(), server.getUsertype()));
        ware.setAdminRule(4);
        ware.setServerCode(server.getServerCode());
        ware.setIsDeleted(0);
        ware.setEventSource(0);
        return ware;
    }

    private String getWareRole(Server server) {
        String userType = server.getUsertype();
        boolean isContainStudent = userType.contains(String.valueOf(UserTypeEnum.STUDENT.getValue()));
        boolean isContainParent = userType.contains(String.valueOf(UserTypeEnum.PARENT.getValue()));
        String role = "2";
        if (isContainStudent) {
            role = "1," + role;
        }

        if (isContainParent) {
            role = role + ",3";
        }

        role = role + ",4,5";
        return role;
    }

    private int getRule(int userType, String userTypeStr) {
        boolean isContain = userTypeStr.contains(String.valueOf(userType));
        if (isContain) {
            return 4;
        }

        return 0;
    }

    private void updateEducationDefaultModel(List<Model> educationModels, boolean isContainsEducation, Server server) {
        if (CollectionUtils.isEmpty(educationModels)) {
            if (isContainsEducation) {
                List<Model> model = educationDefaultModel(server);
                modelDao.saveAll(model);
            }
        }
        else {
            modelDao.updateMark((isContainsEducation ? 1 : 0), server.getId(), UnitClassEnum.EDUCATION.getValue());
        }
    }

    private void updateSchoolDefaultModel(List<Model> schoolModels, boolean isContainsSchool, Server server) {
        if (CollectionUtils.isEmpty(schoolModels)) {
            if (isContainsSchool) {
                List<Model> model = schoolDefaultModel(server);
                modelDao.saveAll(model);
            }
        }
        else {
            modelDao.updateMark((isContainsSchool ? 1 : 0), server.getId(), UnitClassEnum.SCHOOL.getValue());
        }
    }

    /**
     * 教育局默认模块
     *
     * @return
     */
    private List<Model> educationDefaultModel(Server server) {
        List<Model> result = new ArrayList<Model>();
        Model root = new Model();
        int id = Integer.valueOf(server.getId() + String.valueOf(UnitClassEnum.EDUCATION.getValue()));
        root.setId(id);
        root.setMid(String.valueOf(id));
        root.setParentId(-1);
        root.setName(server.getName());
        root.setSubSystem(server.getId());
        root.setMark(YesNoEnum.YES.getValue());
        root.setUnitClass(UnitClassEnum.EDUCATION.getValue());
        root.setDisplayOrder(0);
        Model defaultModel = new Model();
        defaultModel.setId(Integer.valueOf(root.getId() + "1"));
        defaultModel.setMid(String.valueOf(defaultModel.getId()));
        defaultModel.setParentId(root.getId());
        defaultModel.setName(server.getName());
        defaultModel.setSubSystem(server.getId());
        defaultModel.setMark(YesNoEnum.YES.getValue());
        defaultModel.setUnitClass(UnitClassEnum.EDUCATION.getValue());
        defaultModel.setDisplayOrder(1);
        result.add(root);
        result.add(defaultModel);
        return result;
    }

    /**
     * 学校默认模块
     *
     * @return
     */
    private List<Model> schoolDefaultModel(Server server) {
        List<Model> result = new ArrayList<Model>();
        Model root = new Model();
        int id = Integer.valueOf(server.getId() + String.valueOf(UnitClassEnum.SCHOOL.getValue()));
        root.setId(id);
        root.setMid(String.valueOf(id));
        root.setParentId(-1);
        root.setName(server.getName());
        root.setSubSystem(server.getId());
        root.setMark(YesNoEnum.YES.getValue());
        root.setUnitClass(UnitClassEnum.SCHOOL.getValue());
        root.setDisplayOrder(0);
        Model defaultModel = new Model();
        defaultModel.setId(Integer.valueOf(root.getId() + "1"));
        defaultModel.setMid(String.valueOf(defaultModel.getId()));
        defaultModel.setParentId(root.getId());
        defaultModel.setName(server.getName());
        defaultModel.setSubSystem(server.getId());
        defaultModel.setMark(YesNoEnum.YES.getValue());
        defaultModel.setUnitClass(UnitClassEnum.SCHOOL.getValue());
        defaultModel.setDisplayOrder(1);
        result.add(root);
        result.add(defaultModel);
        return result;
    }

    private void registerPassPort(Server server) {
        net.zdsoft.passport.remoting.system.ServerService client = passportServerClient.getPassportServerService();
        int cooperatorId = NumberUtils.toInt(sysOptionService.findValueByOptionCode(Constant.COOPERATOR_ID));
        try {

            client.addServerTypes(new ServerTypeDto[] { getServerTypeDto(server) });
            ServerDto serverDto = getServerDto(server);
            serverDto.setCooperatorId(cooperatorId);
            client.addServer(serverDto);
        } catch (Exception e) {
            //serverTypeId和passport冲突
            if ( e instanceof DataExistsException ) {
                //更新passport
                server.setServerTypeId(server.getServerTypeId() + 1);
                registerPassPort(server);
                updateAppInfoByAppId(server);
            }
        }
    }

    private ServerTypeDto getServerTypeDto(Server server) {
        ServerTypeDto stDto = new ServerTypeDto();
        stDto.setId(server.getServerTypeId());
        stDto.setName(server.getName());
        stDto.setDescription(server.getDescription());
        stDto.setInvalidatePath(server.getInvalidateUrl());
        stDto.setVerifyPath(server.getVerifyUrl());
        stDto.setCharsetName("utf-8");
        stDto.setFreeLoginAllowed(true);
        return stDto;
    }

    private ServerDto getServerDto(Server server) {
        ServerDto sDto = new ServerDto();
        sDto.setId(server.getId());
        sDto.setVerifyKey(server.getServerKey());
        sDto.setName(server.getName());
        sDto.setDescription(server.getDescription());
        sDto.setDomainName(server.getProtocol() + "://" + server.getDomain() + ":" + server.getPort());
        sDto.setTypeId(server.getServerTypeId());
        return sDto;
    }

    @Override
    public void updateInnerAp(Server server) {
        save(server);
        ServerDto serverDto = convertToSysServer(server);
        int cooperatorId = NumberUtils.toInt(sysOptionService.findValueByOptionCode(Constant.COOPERATOR_ID));
        serverDto.setCooperatorId(cooperatorId);
        passportServerClient.getPassportServerService().modifyServer(serverDto);
    }

    private net.zdsoft.passport.remoting.system.ServerDto convertToSysServer(Server server) {
        net.zdsoft.passport.remoting.system.ServerDto serverDto = new net.zdsoft.passport.remoting.system.ServerDto();
        serverDto.setName(server.getName());
        serverDto.setTypeId(server.getServerTypeId());
        serverDto.setDomainName(server.getUrl());
        //默认启用passport
        serverDto.setId(server.getId());
        serverDto.setVerifyKey(server.getServerKey());
        return serverDto;
    }

    public static void main(String[] args) {
        System.out.println(JSONObject.toJSONString(StringUtils.split("1,2,3", ',')));
        String[] array = StringUtils.split("1,2,3", ',');
        System.out.println(ArrayUtils.contains(array, "5"));
    }

	@Override
	public Server findBySubId(Integer subId) {
		return serverDao.findBySubId(subId);
	}

	@Override
	public Server findByIndexUrl(String url) {
		return serverDao.findByIndexUrl(url);
	}
}
