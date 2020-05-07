package net.zdsoft.eclasscard.data.websocket.dto;

import java.io.Serializable;
import java.util.List;

public class EClassCardDto implements Serializable{

	private static final long serialVersionUID = 6397116333817829967L;

	public List<String> cardList;
	
	public String message;

	public List<String> getCardList() {
		return cardList;
	}

	public void setCardList(List<String> cardList) {
		this.cardList = cardList;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
