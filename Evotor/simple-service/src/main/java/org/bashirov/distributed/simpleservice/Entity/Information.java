package org.bashirov.distributed.simpleservice.Entity;

import java.time.LocalDateTime;

public class Information {
	
	public Information(int id, String name, LocalDateTime dt) {
		this.id = id;
		this.name = name;
		this.dt = dt;
	}
	
	private int id;
	private String name;
	private LocalDateTime dt;
	
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public LocalDateTime getDt() {
		return dt;
	}
	
	public void setDt(LocalDateTime dt) {
		this.dt = dt;
	}
	
	@Override
	public String toString() {
		return "Information [id=" + id + ", name=" + name + ", dt=" + dt + "]";
	}
	
	public String toStringValToRow() {
		return id + "\n" + name + "\n" + dt;
	}
	
	
	
}
	

