package net.zdsoft.syncdata.custom.zy.entity;

import net.zdsoft.framework.entity.BaseEntity;

public class ZyCard extends BaseEntity<String>{

	private static final long serialVersionUID = -6371030897916427346L;

	@Override
	public String fetchCacheEntitName() {
		return "zyCard";
	}
	
	private String cardNumber;
	
	private String identityCard;
	
	private String cardOwner;
	
	private String type;

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public String getIdentityCard() {
		return identityCard;
	}

	public void setIdentityCard(String identityCard) {
		this.identityCard = identityCard;
	}

	public String getCardOwner() {
		return cardOwner;
	}

	public void setCardOwner(String cardOwner) {
		this.cardOwner = cardOwner;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
