package net.zdsoft.api.openapi.remote.openapi.dto;

import net.zdsoft.api.base.entity.eis.OpenApiApp;
import net.zdsoft.bigdata.data.vo.Response;
import net.zdsoft.framework.entity.Constant;
import net.zdsoft.framework.utils.FileUtils;
import net.zdsoft.framework.utils.UuidUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @author shenke
 * @since 2019/5/21 下午3:19
 */
public final class OpenApiAppInput implements Convert<OpenApiApp> {

    private String id;
    private String name;
    private MultipartFile file;
    private String indexUrl;
    private String invalidateUrl;
    private String description;
    private String verifyUrl;
    private List<String> unitClasses;
    private List<String> userTypes;
    private List<String> sections;
    private Integer status;
    private List<String> appTypes;


    @Override
    public OpenApiApp convert() {
        OpenApiApp app = new OpenApiApp();
        BeanUtils.copyProperties(this, app, "icon", "unitClasses", "userTypes", "sections", "status", "appTypes");
        if (CollectionUtils.isNotEmpty(unitClasses)) {
            app.setUnitClasses(String.join(OpenApiApp.SEPARATOR, unitClasses));
        }
        if (CollectionUtils.isNotEmpty(userTypes)) {
            app.setUserTypes(String.join(OpenApiApp.SEPARATOR, userTypes));
        }
        if (CollectionUtils.isNotEmpty(sections)) {
            app.setSections(String.join(OpenApiApp.SEPARATOR, sections));
        }
        if (CollectionUtils.isNotEmpty(appTypes)) {
            app.setAppTypes(String.join(OpenApiApp.SEPARATOR, appTypes));
        }
        if (status != null) {
            app.setStatus(status);
        }
        return app;
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

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
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

    public List<String> getUnitClasses() {
        return unitClasses;
    }

    public void setUnitClasses(List<String> unitClasses) {
        this.unitClasses = unitClasses;
    }

    public List<String> getUserTypes() {
        return userTypes;
    }

    public void setUserTypes(List<String> userTypes) {
        this.userTypes = userTypes;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getSections() {
        return sections;
    }

    public void setSections(List<String> sections) {
        this.sections = sections;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<String> getAppTypes() {
		return appTypes;
	}

	public void setAppTypes(List<String> appTypes) {
		this.appTypes = appTypes;
	}

	public String transferIconFile(String filePath, String id) throws IOException {
        String iconUrl = null;
        if (getFile() != null && getFile().getSize() != 0L) {
            String iconDirectoryPath = filePath + File.separator + OpenApiApp.getIconPath();
            File targetDirectory = new File(iconDirectoryPath);
            if (!targetDirectory.exists()) {
                targetDirectory.mkdirs();
                //create transfer error
            }
            iconUrl = iconDirectoryPath + id + "." + FileUtils.getExtension(getFile().getOriginalFilename());
            String bakName = id + "_bak_" + UuidUtils.generateUuid() + FileUtils.getExtension(getFile().getOriginalFilename());
            File icon = new File(iconUrl);
            if (icon.exists()) {
                boolean rename = icon.renameTo(new File(iconDirectoryPath + File.separator + "bak" +File.separator + bakName));
                if (!rename) {
                    icon.delete();
                }
            }
            getFile().transferTo(icon);
            return iconUrl.replace(filePath, "") + "?time=" + System.currentTimeMillis();
        }
        return null;
    }
}
