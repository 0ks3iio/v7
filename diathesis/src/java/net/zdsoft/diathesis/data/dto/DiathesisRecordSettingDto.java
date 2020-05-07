package net.zdsoft.diathesis.data.dto;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author: panlf
 * @Date: 2019/4/3 9:48
 */
public class DiathesisRecordSettingDto {
    @NotBlank(message = "id不能为空")
    private String id;
    private String unitId;
    private String projectName;
    @NotEmpty(message = "录入人不能为空")
    private List<String> inputTypes;
    @NotEmpty(message = "审核人不能为空")
    private List<String> auditorTypes;
    private Map<String,String> peopleTypesMap;
    @NotBlank(message = "统计类型不能为空")
    @Pattern(regexp = "[01]",message = "没有这种统计类型")
    private String countType;

    @Valid
    private List<DiathesisStructureSettingDto> structure=new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public List<String> getInputTypes() {
        return inputTypes;
    }

    public void setInputTypes(List<String> inputTypes) {
        this.inputTypes = inputTypes;
    }

    public List<String> getAuditorTypes() {
        return auditorTypes;
    }

    public void setAuditorTypes(List<String> auditorTypes) {
        this.auditorTypes = auditorTypes;
    }

    public Map<String, String> getPeopleTypesMap() {
        return peopleTypesMap;
    }

    public void setPeopleTypesMap(Map<String, String> peopleTypesMap) {
        this.peopleTypesMap = peopleTypesMap;
    }

    public List<DiathesisStructureSettingDto> getStructure() {
        return structure;
    }

    public void setStructure(List<DiathesisStructureSettingDto> structure) {
        this.structure = structure;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public String getCountType() {
        return countType;
    }

    public void setCountType(String countType) {
        this.countType = countType;
    }
}
