package net.zdsoft.gkelective.data.dto;

public class Move implements Cloneable{
	private Room room;
    
    private int from;
    
    private int to;
    
    public Move() {
		super();
	}

	public Move(Room room, int from, int to) {
		super();
		this.room = room;
		this.from = from;
		this.to = to;
	}

    @Override
	protected Move clone() throws CloneNotSupportedException {
    	Move move = (Move)super.clone();
    	if (this.room != null) {
    		move.setRoom(this.room.clone());
    	}
		return move;
	}
    
    public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}

	/**
     * 获取from
     * @return from
     */
    public int getFrom() {
        return from;
    }

    /**
     * 设置from
     * @param from from
     */
    public void setFrom(int from) {
        this.from = from;
    }

    /**
     * 获取to
     * @return to
     */
    public int getTo() {
        return to;
    }

    /**
     * 设置to
     * @param to to
     */
    public void setTo(int to) {
        this.to = to;
    }

}
