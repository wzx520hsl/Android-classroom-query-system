package com.wzx.buptschedule.bean;

import java.util.ArrayList;
import java.util.List;


public class FreeRooms {
	

	private String time ;

	private String status;
	
	private List<FreeRoom> rooms = new ArrayList<FreeRoom>();
	
	
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public List<FreeRoom> getRooms() {
		return rooms;
	}
	public void addRoom(FreeRoom room) {
		rooms.add(room);
	}
	
	public void setRooms(List<FreeRoom> rooms) {
		this.rooms = rooms;
	}
	
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	

}
