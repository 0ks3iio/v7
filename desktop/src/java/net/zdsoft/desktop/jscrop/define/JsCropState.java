package net.zdsoft.desktop.jscrop.define;

import java.io.Serializable;

import net.zdsoft.framework.dto.ResultDto;

/**
 * @author shenke
 */
public class JsCropState extends ResultDto implements Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JsCropFile jsCropFile;

	private String originFilePath;
	private String bigFilePath;
	private String smallerFilePath;

    public JsCropFile getJsCropFile() {
        return jsCropFile;
    }

    public void setJsCropFile(JsCropFile jsCropFile) {
        this.jsCropFile = jsCropFile;
    }

	public String getOriginFilePath() {
		return originFilePath;
	}

	public void setOriginFilePath(String originFilePath) {
		this.originFilePath = originFilePath;
	}

	public String getBigFilePath() {
		return bigFilePath;
	}

	public void setBigFilePath(String bigFilePath) {
		this.bigFilePath = bigFilePath;
	}

	public String getSmallerFilePath() {
		return smallerFilePath;
	}

	public void setSmallerFilePath(String smallerFilePath) {
		this.smallerFilePath = smallerFilePath;
	}
}
