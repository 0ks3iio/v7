package net.zdsoft.desktop.dto;

import net.zdsoft.system.remote.dto.UnitServerClassify;

import java.util.List;

/**
 * @author shenke
 * @since 2019/3/13 下午1:55
 */
public final class  Classify {

    private UnitServerClassify unitServerClassify;
    private List<DServerDto> serverDtos;
    private Boolean system;

    public UnitServerClassify getUnitServerClassify() {
        return unitServerClassify;
    }

    public void setUnitServerClassify(UnitServerClassify unitServerClassify) {
        this.unitServerClassify = unitServerClassify;
    }

    public List<DServerDto> getServerDtos() {
        return serverDtos;
    }

    public void setServerDtos(List<DServerDto> serverDtos) {
        this.serverDtos = serverDtos;
    }

    public Boolean getSystem() {
        return system;
    }

    public void setSystem(Boolean system) {
        this.system = system;
    }
}
