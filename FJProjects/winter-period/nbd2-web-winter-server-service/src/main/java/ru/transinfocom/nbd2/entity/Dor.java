package ru.transinfocom.nbd2.entity;

import java.util.List;

public class Dor {
	
	private int predId;
	private String name;
	private int perStartDate;
	private int perFinishDate;
	private List<SeasonYearsAndDates> preds;
	
	public int getPredId() {
		return predId;
	}
	public void setPredId(int predId) {
		this.predId = predId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getPerStartDate() {
		return perStartDate;
	}
	public void setPerStartDate(int perStartDate) {
		this.perStartDate = perStartDate;
	}
	public int getPerFinishDate() {
		return perFinishDate;
	}
	public void setPerFinishDate(int perFinishDate) {
		this.perFinishDate = perFinishDate;
	}
	public List<SeasonYearsAndDates> getPreds() {
		return preds;
	}
	public void setPreds(List<SeasonYearsAndDates> preds) {
		this.preds = preds;
	}
	
	
}
