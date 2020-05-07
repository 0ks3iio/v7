package net.zdsoft.newgkelective.data.optaplanner.dto;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("NKRoom")
public class NKRoom  implements Serializable{

    private String roomId;
    private String roomName;
    //辅助
    private String hasNull;
    
    //辅助
    private int capacity;//容纳人数
    
	public String getHasNull() {
		return hasNull;
	}
	public void setHasNull(String hasNull) {
		this.hasNull = hasNull;
	}
	public String getRoomId() {
		return roomId;
	}
	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}
	public String getRoomName() {
		return roomName;
	}
	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}
	public int getCapacity() {
		return capacity;
	}
	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}
	
	
}
