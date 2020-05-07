package net.zdsoft.basedata.extension.service.impl;

import net.zdsoft.basedata.entity.Unit;
import net.zdsoft.basedata.extension.remote.service.UnitExtensionRemoteService;
import net.zdsoft.basedata.extension.dao.UnitExtensionDao;
import net.zdsoft.basedata.extension.entity.UnitExtension;
import net.zdsoft.basedata.extension.enums.UnitExtensionExpireType;
import net.zdsoft.basedata.extension.enums.UnitExtensionNature;
import net.zdsoft.basedata.extension.enums.UnitExtensionState;
import net.zdsoft.basedata.service.UnitService;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author shenke
 */
@Service("unitExtensionRemoteService")
public class UnitExtensionRemoteServiceImpl implements UnitExtensionRemoteService {

    @Resource
    private UnitExtensionDao unitExtensionDao;
    @Resource
    private UnitService unitService;

	@Override
	public void saveAll(List<UnitExtension> unitExtensions) {
		unitExtensions.forEach(c->{
			Integer usingNature = c.getUsingNature() == null ? UnitExtensionNature.OFFICIAL :  c.getUsingNature() ;
			Integer usingState  = c.getUsingState()  == null ? UnitExtensionState.NORMAL    :  c.getUsingState();
			Integer expireTimeType = c.getExpireTimeType() == null ? UnitExtensionExpireType.PERMANENT : c.getExpireTimeType();
			c.setUsingNature(usingNature);
			c.setUsingState(usingState);
			c.setExpireTimeType(expireTimeType);
		});
		unitExtensionDao.saveAll(unitExtensions);
	}
    
    @Override
    public net.zdsoft.basedata.extension.remote.service.UnitState isEnable(String unitId) {

        Unit unit = unitService.findOne(unitId);

        if (Objects.nonNull(unit)) {
            if (!Integer.valueOf(Unit.UNIT_MARK_NORAML).equals(unit.getUnitState())) {
                return new UnitState("单位不可用", false);
            }
        }


        UnitExtension extension = unitExtensionDao.getUnitExtensionByUnitId(unitId).orElse(null);
        if (Objects.isNull(extension)) {
            return UnitState.NOOP;
        }

        String message = null;

        if (UnitExtensionState.DISABLE.equals(extension.getUsingState())) {
            message = "单位已停用";
        }
        if (UnitExtensionExpireType.SPECIFY_TIME.equals(extension.getExpireTimeType()) && extension.getExpireTime() != null) {
            if (new Date().after(extension.getExpireTime())) {
                message = "单位已过期";
            }
        }

        return new UnitState(message, Objects.isNull(message));
    }

    /**
     * @author shenke
     */
    public static class UnitState implements net.zdsoft.basedata.extension.remote.service.UnitState {

        private String message;
        private boolean enable;

        public static final net.zdsoft.basedata.extension.remote.service.UnitState NOOP = new UnitState("", true);

        public UnitState(String message, boolean enable) {
            this.message = message;
            this.enable = enable;
        }

        @Override
        public boolean isEnable() {
            return enable;
        }

        @Override
        public String unavailableInformation() {
            return message;
        }
    }

	@Override
	public List<UnitExtension> findByUnitIdIn(String... unitIds) {
		return unitExtensionDao.findByUnitIdIn(unitIds);
	}
}
