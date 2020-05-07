package net.zdsoft.api.openapi.remote.openapi.vo;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import net.zdsoft.api.base.entity.eis.OpenApiApp;
import net.zdsoft.basedata.enums.UnitClassCode;
import net.zdsoft.basedata.enums.UserOwnerTypeCode;

import org.apache.commons.lang3.StringUtils;

/**
 * @author shenke
 * @since 2019/5/21 下午7:37
 */
public final class OpenApiAppEdit {

    private String id;
    private String name;
    private String indexUrl;
    private String invalidateUrl;
    private String verifyUrl;
    private String iconUrl;
    private String description;
    private String developerId;
    
    private boolean education;
    private boolean school;

    private boolean teacher;
    private boolean student;
    private boolean family;

    private boolean section0;
    private boolean section1;
    private boolean section2;
    private boolean section3;
    
    private boolean docking;
    private boolean applyApi;

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIndexUrl() {
        return indexUrl;
    }

    public void setIndexUrl(String indexUrl) {
        this.indexUrl = indexUrl;
    }

    public String getInvalidateUrl() {
        return invalidateUrl;
    }

    public void setInvalidateUrl(String invalidateUrl) {
        this.invalidateUrl = invalidateUrl;
    }

    public String getVerifyUrl() {
        return verifyUrl;
    }

    public void setVerifyUrl(String verifyUrl) {
        this.verifyUrl = verifyUrl;
    }

    public boolean isEducation() {
        return education;
    }

    public void setEducation(boolean education) {
        this.education = education;
    }

    public boolean isSchool() {
        return school;
    }

    public void setSchool(boolean school) {
        this.school = school;
    }

    public boolean isTeacher() {
        return teacher;
    }

    public void setTeacher(boolean teacher) {
        this.teacher = teacher;
    }

    public boolean isStudent() {
        return student;
    }

    public void setStudent(boolean student) {
        this.student = student;
    }

    public boolean isFamily() {
        return family;
    }

    public void setFamily(boolean family) {
        this.family = family;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isSection0() {
        return section0;
    }

    public void setSection0(boolean section0) {
        this.section0 = section0;
    }

    public boolean isSection1() {
        return section1;
    }

    public void setSection1(boolean section1) {
        this.section1 = section1;
    }

    public boolean isSection2() {
        return section2;
    }

    public void setSection2(boolean section2) {
        this.section2 = section2;
    }

    public boolean isSection3() {
        return section3;
    }

    public void setSection3(boolean section3) {
        this.section3 = section3;
    }

    public boolean isDocking() {
		return docking;
	}

	public void setDocking(boolean docking) {
		this.docking = docking;
	}

	public boolean isApplyApi() {
		return applyApi;
	}

	public void setApplyApi(boolean applyApi) {
		this.applyApi = applyApi;
	}

	public String getDeveloperId() {
		return developerId;
	}

	public void setDeveloperId(String developerId) {
		this.developerId = developerId;
	}

	public static OpenApiAppEdit convert(OpenApiApp app, String fileUrl) {
        OpenApiAppEdit edit = new OpenApiAppEdit();
        edit.setId(app.getId());
        edit.setIndexUrl(app.getIndexUrl());
        edit.setInvalidateUrl(app.getInvalidateUrl());
        edit.setVerifyUrl(app.getVerifyUrl());
        edit.setName(app.getName());
        edit.setDescription(app.getDescription());
        edit.setIconUrl(fileUrl + app.getIconUrl());
        String[] unitClasses = StringUtils.split(app.getUnitClasses(), OpenApiApp.SEPARATOR);
        Arrays.stream(unitClasses).forEach(e -> {
            if (UnitClassCode.SCHOOL.toString().equals(e)) {
                edit.setSchool(true);
            }
            if (UnitClassCode.EDUCATION.toString().equals(e)) {
                edit.setEducation(true);
            }
        });
        if(StringUtils.isNotBlank(app.getAppTypes())){
        	String[] appTypes = StringUtils.split(app.getAppTypes(), OpenApiApp.SEPARATOR);
        	Arrays.stream(appTypes).forEach(e -> {
        		if (OpenApiApp.APP_DOCKING_NORMAL.equals(e)) {
        			edit.setDocking(Boolean.TRUE);
        		}
        		if (OpenApiApp.APP_APPLYAPI_NORMAL.equals(e)) {
        			edit.setApplyApi(Boolean.TRUE);
        		}
        	});
        }
        Set<String> userTypes = new HashSet<>(Arrays.asList(StringUtils.split(app.getUserTypes(), OpenApiApp.SEPARATOR)));
        edit.setStudent(userTypes.contains(UserOwnerTypeCode.STUDENT.toString()));
        edit.setFamily(userTypes.contains(UserOwnerTypeCode.FAMILY.toString()));
        edit.setTeacher(userTypes.contains(UserOwnerTypeCode.TEACHER.toString()));
        if (StringUtils.isNotBlank(app.getSections())) {
            Set<String> sections = new HashSet<>(Arrays.asList(StringUtils.split(app.getSections(), OpenApiApp.SEPARATOR)));
            edit.setSection0(sections.contains("0"));
            edit.setSection1(sections.contains("1"));
            edit.setSection2(sections.contains("2"));
            edit.setSection3(sections.contains("3"));
        }
        edit.setDeveloperId(app.getDeveloperId());
        return edit;
    }
}
