package net.zdsoft.system.dto.server;

import java.util.ArrayList;
import java.util.List;

import net.zdsoft.basedata.dto.BaseDto;
import net.zdsoft.framework.utils.SortUtils;
import net.zdsoft.system.entity.server.SubSystem;

public class SubSystemDto extends BaseDto {

    private static final long serialVersionUID = 1899579023655956211L;
    private SubSystem subSystem;
    private List<SubSystemDto> subSystemDtos = new ArrayList<SubSystemDto>();
    private List<ModelDto> modelDtos = new ArrayList<ModelDto>();

    private String imageUrl;        //子系统图标完整URL

    public void addSubSystemDto(SubSystemDto subSystemDto) {
        subSystemDtos.add(subSystemDto);
    }

    public void addModelDto(ModelDto model) {
        modelDtos.add(model);
    }

    public List<SubSystemDto> getSubSystemDtos() {
        return subSystemDtos;
    }

    public List<ModelDto> getModelDtos() {
        SortUtils.ASC(modelDtos, "model.displayOrder");
        return modelDtos;
    }

    public void setSubSystem(SubSystem subSystem) {
        this.subSystem = subSystem;
    }

    public SubSystem getSubSystem() {
        return subSystem;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
