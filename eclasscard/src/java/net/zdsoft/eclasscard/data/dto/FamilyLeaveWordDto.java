package net.zdsoft.eclasscard.data.dto;

public class FamilyLeaveWordDto {
	private String id;//唯一标识
	private String name;//显名称
	private String username;
	private String lastWord;//最后的一句
	private String picUrl;//头像
	private long unReadNum;//未读数

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

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getLastWord() {
		return lastWord;
	}

	public void setLastWord(String lastWord) {
		this.lastWord = lastWord;
	}

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	public long getUnReadNum() {
		return unReadNum;
	}

	public void setUnReadNum(long unReadNum) {
		this.unReadNum = unReadNum;
	}

}
