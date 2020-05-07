package net.zdsoft.system.dto.common;

import org.springframework.web.multipart.MultipartFile;

public class LoginPageDto {

    private String pageTitle;
    private MultipartFile pageLogoImage;
    private String pageLogoName;
    private MultipartFile loginBgImage;
    private String copyRight;
    private boolean standard;
    private MultipartFile loginPageTemplate;
    private String commonPageTitle;
    private boolean phoneAsUserName;
    private boolean palyer;
    private String domainName;  //域名
    private boolean forgetPassword;
    private boolean favicon;

    private boolean enablePageLogoImage; //是否开启logo图片
    private boolean enablePageLogoName; //是否开启logo名称
    public String getDomainName() {
		return domainName;
	}

	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}

	public String getPageTitle() {
        return pageTitle;
    }

    public void setPageTitle(String pageTitle) {
        this.pageTitle = pageTitle;
    }

    public MultipartFile getPageLogoImage() {
        return pageLogoImage;
    }

    public void setPageLogoImage(MultipartFile pageLogoImage) {
        this.pageLogoImage = pageLogoImage;
    }

    public String getPageLogoName() {
        return pageLogoName;
    }

    public void setPageLogoName(String pageLogoName) {
        this.pageLogoName = pageLogoName;
    }

    public MultipartFile getLoginBgImage() {
        return loginBgImage;
    }

    public void setLoginBgImage(MultipartFile loginBgImage) {
        this.loginBgImage = loginBgImage;
    }

    public String getCopyRight() {
        return copyRight;
    }

    public void setCopyRight(String copyRight) {
        this.copyRight = copyRight;
    }

    public boolean isStandard() {
        return standard;
    }

    public void setStandard(boolean standard) {
        this.standard = standard;
    }

    public MultipartFile getLoginPageTemplate() {
        return loginPageTemplate;
    }

    public void setLoginPageTemplate(MultipartFile loginPageTemplate) {
        this.loginPageTemplate = loginPageTemplate;
    }

    public String getCommonPageTitle() {
        return commonPageTitle;
    }

    public void setCommonPageTitle(String commonPageTitle) {
        this.commonPageTitle = commonPageTitle;
    }

    public boolean isPhoneAsUserName() {
        return phoneAsUserName;
    }

    public void setPhoneAsUserName(boolean phoneAsUserName) {
        this.phoneAsUserName = phoneAsUserName;
    }

    public boolean isPalyer() {
        return palyer;
    }

    public void setPalyer(boolean palyer) {
        this.palyer = palyer;
    }

	public boolean isEnablePageLogoImage() {
		return enablePageLogoImage;
	}

	public void setEnablePageLogoImage(boolean enablePageLogoImage) {
		this.enablePageLogoImage = enablePageLogoImage;
	}

	public boolean isEnablePageLogoName() {
		return enablePageLogoName;
	}

	public void setEnablePageLogoName(boolean enablePageLogoName) {
		this.enablePageLogoName = enablePageLogoName;
	}

    public boolean isForgetPassword() {
        return forgetPassword;
    }

    public void setForgetPassword(boolean forgetPassword) {
        this.forgetPassword = forgetPassword;
    }

    public boolean isFavicon() {
        return favicon;
    }

    public void setFavicon(boolean favicon) {
        this.favicon = favicon;
    }
}
