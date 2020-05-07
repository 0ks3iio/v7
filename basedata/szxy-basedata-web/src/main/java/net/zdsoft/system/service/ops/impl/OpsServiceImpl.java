package net.zdsoft.system.service.ops.impl;

import com.google.common.collect.Lists;
import net.zdsoft.basedata.entity.SysProductParam;
import net.zdsoft.basedata.entity.Unit;
import net.zdsoft.basedata.entity.User;
import net.zdsoft.basedata.remote.service.CourseRemoteService;
import net.zdsoft.basedata.remote.service.SysProductParamRemoteService;
import net.zdsoft.basedata.remote.service.UnitRemoteService;
import net.zdsoft.basedata.remote.service.UserRemoteService;
import net.zdsoft.framework.config.Evn;
import net.zdsoft.framework.utils.PassportClientUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.license.entity.LicenseInfo;
import net.zdsoft.license.service.LicenseService;
import net.zdsoft.passport.entity.Account;
import net.zdsoft.passport.exception.PassportException;
import net.zdsoft.system.constant.Constant;
import net.zdsoft.system.entity.user.Role;
import net.zdsoft.system.service.config.SysOptionService;
import net.zdsoft.system.service.config.SystemIniService;
import net.zdsoft.system.service.ops.OpsService;
import net.zdsoft.system.service.server.ServerService;
import net.zdsoft.system.service.user.RoleService;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * @author shenke
 * @since 2017.07.26
 */
@Service
public class OpsServiceImpl implements OpsService {

    private Logger logger = Logger.getLogger(OpsServiceImpl.class);

    @Autowired private SystemIniService systemIniService;
    @Autowired private SysProductParamRemoteService sysProductParamRemoteService;
    @Autowired private SysOptionService sysOptionService;
    @Autowired private UnitRemoteService unitRemoteService;
    @Autowired private ServerService serverService;
    @Autowired private LicenseService licenseService;
    @Autowired private UserRemoteService userRemoteService;
    @Autowired private RoleService roleService;
    @Autowired private CourseRemoteService courseRemoteService;

    @Override
    public void modifyLoginSetParameters(Map<String,Object> paraMap) {
        String copyRight = (String)paraMap.get(SysProductParam.COMPANY_COPYRIGHT);
        sysProductParamRemoteService.updateParamValue(copyRight, SysProductParam.COMPANY_COPYRIGHT);
        paraMap.remove(SysProductParam.COMPANY_COPYRIGHT);

        systemIniService.update(paraMap,"iniid","nowvalue");
    }


    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @Override
    public void activeLicense(String unitName, String licenseTxt, String regionCode) {

        LicenseInfo licenseInfo = licenseService.decodeLicense(licenseTxt);

        sysOptionService.updateNowValueByCode(regionCode, Constant.REGION_CODE);

        Unit topUnit = SUtils.dc(unitRemoteService.findTopUnit(), net.zdsoft.basedata.entity.Unit.class);
        if (topUnit == null) {
            unitRemoteService.initTopUnit(unitName, regionCode);
        } else {
            int regionLevel = regionCode.equals("000000") ? 1 : regionCode.endsWith("0000") ? 2 : regionCode.endsWith("00") ? 3 : 4;
            topUnit.setRegionCode(regionCode);
            topUnit.setUnitName(licenseInfo.getUnitName());
            topUnit.setRegionLevel(regionLevel);
            topUnit.setUnitState(1);
            topUnit.setUnitClass(1);
            topUnit.setUnitType(1);
            int subLength = regionCode.equals("000000") ? 0 : regionCode.endsWith("0000") ? 2 : regionCode.endsWith("00") ? 4 : 6;
            topUnit.setUnionCode(StringUtils.substring(regionCode, 0, subLength));
            sysProductParamRemoteService.updateParamValue(licenseInfo.getProductName(), SysProductParam.COMPANY_CHINESE_NAME);
            unitRemoteService.update(topUnit, topUnit.getId(), new String[]{"unitName","regionCode","regionLevel","unitState"});
        }
        if (topUnit == null) {
            topUnit = SUtils.dc(unitRemoteService.findTopUnit(), net.zdsoft.basedata.entity.Unit.class);
        }

        if (topUnit != null) {
            //初始化课程
            if ("eis".equals(systemIniService.findValue("TIMETABLE.DEPLOY.SCHTYPE"))) {
                courseRemoteService.updateInitCourse(topUnit.getId());
            }
            //新增顶级单位增加默认角色
            List<Role> topUnitDefaultRole  = roleService.findListBy(new String[]{"unitId", "identifier"}, new String[]{topUnit.getId(), "default"});
            if ( topUnitDefaultRole.isEmpty()) {

                List<Role> systemDefaultRoles = roleService.findListBy("unitId", "00000000000000000000000000000000");
                if (systemDefaultRoles != null) {
                    List<Role> unitDefaultRole = Lists.newArrayList();
                    for (Role defaultRole : systemDefaultRoles) {
                        if (StringUtils.isBlank(defaultRole.getIdentifier())) {
                            continue;
                        }
                        defaultRole.setId(UuidUtils.generateUuid());
                        defaultRole.setUnitId(topUnit.getId());
                        roleService.save(defaultRole);
                        unitDefaultRole.add(defaultRole);
                    }
                    roleService.saveAll(unitDefaultRole.toArray(new Role[0]));
                    if (logger.isDebugEnabled()) {
                        logger.debug("顶级单位默认角色已初始化");
                    }
                } else {
                    logger.error("数据库中不存在系统默认角色（unitId为32位0的role）默认角色未初始化【请检查是否存在未执行语法】");
                }
            }
        } else {
            throw new RuntimeException("顶级单位未创建，无法初始化默认角色");
        }

        // 调用本地方法修改应用状态，在序列号内（第三方应用除外）的启用，不在的关闭
        Set<String> systemCodes = licenseInfo.getAvailableSubsystems();
        serverService.updateEnableServer(systemCodes.toArray(new String[systemCodes.size()]));
        // 保存序列号激活记录
        licenseService.saveLicense(unitName, licenseTxt);

        // 激活超管前台登录权限
        User superAdmin = SUtils.dc(
                userRemoteService.findOneBy(new String[] { "isDeleted", "ownerType" }, new Object[] { 0,
                        User.OWNER_TYPE_SUPER }), User.class);
        if (Optional.ofNullable(superAdmin.getUserState()).orElse(-1) != 1) {
            // 更新base_user状态
            userRemoteService.updateStateByUsername(1, superAdmin.getUsername());
            if (Evn.isPassport()) {
                // 更新sys_account状态
                Account account = new Account();
                account.setId(superAdmin.getAccountId());
                account.setState(1);
                try {
                    PassportClientUtils.getPassportClient().modifyAccount(account, new String[]{"state"});
                } catch (PassportException e) {
                    throw new IllegalStateException("invoker net.zdsoft.passport.service.client.PassportClient.modifyAccount" +
                            "(net.zdsoft.passport.entity.Account, java.lang.String[]) error");
                }
            }
        }
    }

}
