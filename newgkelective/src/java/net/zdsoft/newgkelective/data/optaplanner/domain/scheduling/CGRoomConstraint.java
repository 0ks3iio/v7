package net.zdsoft.newgkelective.data.optaplanner.domain.scheduling;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

//some lectures have to be done in some selected rooms

@XStreamAlias("CGRoomConstraint")
public class CGRoomConstraint implements Serializable{
	private CGSectionLecture lecture;
	private CGRoom   room;
	
	public CGSectionLecture getLecture() {
		return lecture;
	}
	public void setLecture(CGSectionLecture lecture) {
		this.lecture = lecture;
	}
	public CGRoom getRoom() {
		return room;
	}
	public void setRoom(CGRoom room) {
		this.room = room;
	}

}
