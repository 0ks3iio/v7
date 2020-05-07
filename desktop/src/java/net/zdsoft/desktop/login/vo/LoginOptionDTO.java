package net.zdsoft.desktop.login.vo;

/**
 * @author ke_shen@126.com
 * @since 2018/1/11 下午2:20
 */
public class LoginOptionDTO {


	private String headerName;
	private String logoName;
	private String footer;
	private String logoUrl;
	private String loginBgUrl;
	private boolean player;
	private String commonHeader;
	
	private boolean enablePageLogoImage; //是否开启logo图片
    private boolean enablePageLogoName; //是否开启logo名称

	//passport 参数
	private String passportUrl;
	private String verifyKey;
	private String serverId;
	private boolean connectPassport;
	private String root;
	private String indexUrl;
	private boolean verifyCode;

	private boolean initLicense;

	private boolean showQQ;
	private boolean showRegister;

	private boolean preview;
	private String warn;
	private boolean showForgetPassword;

	public String getCommonHeader() {
		return commonHeader;
	}

	public LoginOptionDTO setCommonHeader(String commonHeader) {
		this.commonHeader = commonHeader;
		return this;
	}

	public LoginOptionDTO() {
	}

	public LoginOptionDTO(Builder builder) {
		headerName = builder.headerName;
		logoName = builder.logoName;
		footer = builder.footer;
		logoUrl = builder.logoUrl;
		loginBgUrl = builder.loginBgUrl;
		passportUrl = builder.passportUrl;
		verifyKey = builder.verifyKey;
		serverId = builder.serverId;
		connectPassport = builder.connectPassport;
		showQQ = builder.showQQ;
		showRegister = builder.showRegister;
		root = builder.root;
		this.indexUrl = builder.indexUrl;
		this.verifyCode = builder.verifyCode;
		this.initLicense = builder.initLicense;
		this.commonHeader = builder.commonHeader;
		this.warn = builder.warn;
		
		this.enablePageLogoImage = builder.enablePageLogoImage;
		this.enablePageLogoName = builder.enablePageLogoName;
		this.player = builder.player;
		this.showForgetPassword = builder.showForgetPassowrd;
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

	public String getHeaderName() {
		return headerName;
	}

	public void setHeaderName(String headerName) {
		this.headerName = headerName;
	}

	public String getLogoName() {
		return logoName;
	}

	public void setLogoName(String logoName) {
		this.logoName = logoName;
	}

	public String getFooter() {
		return footer;
	}

	public void setFooter(String footer) {
		this.footer = footer;
	}

	public String getLogoUrl() {
		return logoUrl;
	}

	public void setLogoUrl(String logoUrl) {
		this.logoUrl = logoUrl;
	}

	public String getLoginBgUrl() {
		return loginBgUrl;
	}

	public void setLoginBgUrl(String loginBgUrl) {
		this.loginBgUrl = loginBgUrl;
	}

	public String getPassportUrl() {
		return passportUrl;
	}

	public void setPassportUrl(String passportUrl) {
		this.passportUrl = passportUrl;
	}

	public String getVerifyKey() {
		return verifyKey;
	}

	public void setVerifyKey(String verifyKey) {
		this.verifyKey = verifyKey;
	}

	public String getServerId() {
		return serverId;
	}

	public void setServerId(String serverId) {
		this.serverId = serverId;
	}

	public boolean isConnectPassport() {
		return connectPassport;
	}

	public void setConnectPassport(boolean connectPassport) {
		this.connectPassport = connectPassport;
	}

	public String getRoot() {
		return root;
	}

	public void setRoot(String root) {
		this.root = root;
	}

	public String getIndexUrl() {
		return indexUrl;
	}

	public void setIndexUrl(String indexUrl) {
		this.indexUrl = indexUrl;
	}

	public boolean isVerifyCode() {
		return verifyCode;
	}

	public void setVerifyCode(boolean verifyCode) {
		this.verifyCode = verifyCode;
	}

	public boolean isInitLicense() {
		return initLicense;
	}

	public void setInitLicense(boolean initLicense) {
		this.initLicense = initLicense;
	}

	public boolean getShowQQ() {
		return showQQ;
	}

	public void setShowQQ(boolean showQQ) {
		this.showQQ = showQQ;
	}

	public boolean getShowRegister() {
		return showRegister;
	}

	public void setShowRegister(boolean showRegister) {
		this.showRegister = showRegister;
	}

	public boolean isPlayer() {
		return player;
	}

	public void setPlayer(boolean player) {
		this.player = player;
	}

	public static Builder newBuilder() {
		return new Builder();
	}

	public boolean isPreview() {
		return preview;
	}

	public void setPreview(boolean preview) {
		this.preview = preview;
	}

	public String getWarn() {
		return warn;
	}

	public void setWarn(String warn) {
		this.warn = warn;
	}

	public boolean isShowForgetPassword() {
		return showForgetPassword;
	}

	public void setShowForgetPassword(boolean showForgetPassword) {
		this.showForgetPassword = showForgetPassword;
	}

	public static final class Builder {
		private String headerName;
		private String logoName;
		private String footer;
		private String logoUrl;
		private String loginBgUrl;
		private String passportUrl;
		private String verifyKey;
		private String serverId;
		private boolean connectPassport;
		private boolean showQQ;
		private boolean showRegister;
		private String root;
		private String indexUrl;
		private boolean verifyCode;
		private boolean initLicense;
		private boolean player;
		private String commonHeader;
		private String warn;
		private boolean showForgetPassowrd;
		
		private boolean enablePageLogoImage; //是否开启logo图片
	    private boolean enablePageLogoName; //是否开启logo名称
		

		private Builder() {
		}

		public Builder commonHeader(String val) {
			commonHeader = val;
			return this;
		}

		public Builder headerName(String val) {
			headerName = val;
			return this;
		}

		public Builder logoName(String val) {
			logoName = val;
			return this;
		}

		public Builder footer(String val) {
			footer = val;
			return this;
		}

		public Builder logoUrl(String val) {
			logoUrl = val;
			return this;
		}

		public Builder loginBgUrl(String val) {
			loginBgUrl = val;
			return this;
		}

		public Builder passportUrl(String val) {
			passportUrl = val;
			return this;
		}

		public Builder verifyKey(String val) {
			verifyKey = val;
			return this;
		}

		public Builder serverId(String val) {
			serverId = val;
			return this;
		}

		public Builder connectPassport(boolean val) {
			connectPassport = val;
			return this;
		}

		public Builder showQQ(boolean val) {
			showQQ = val;
			return this;
		}

		public Builder showRegister(boolean val) {
			showRegister = val;
			return this;
		}

		public Builder root(String root) {
			this.root = root;
			return this;
		}

		public Builder indexUrl(String val) {
			indexUrl = val;
			return this;
		}

		public Builder verifyCode(boolean verifyCode) {
			this.verifyCode = verifyCode;
			return this;
		}

		public Builder initLicense(boolean val) {
			initLicense = val;
			return this;
		}
		public Builder player(boolean val) {
			player = val;
			return this;
		}
		public Builder warn(String val) {
			warn = val;
			return this;
		}

		public Builder showForgetPassword(boolean val) {
			showForgetPassowrd = val;
			return this;
		}

		public LoginOptionDTO build() {
			return new LoginOptionDTO(this);
		}
		
		public Builder enablePageLogoImage(boolean val) {
			enablePageLogoImage = val;
			return this;
		}
		public Builder enablePageLogoName(boolean val) {
			enablePageLogoName = val;
			return this;
		}
		
	}
}
