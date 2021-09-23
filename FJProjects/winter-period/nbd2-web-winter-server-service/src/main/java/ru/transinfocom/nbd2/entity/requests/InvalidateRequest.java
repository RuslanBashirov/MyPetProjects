package ru.transinfocom.nbd2.entity.requests;

public class InvalidateRequest {
	private int predId;
	private int seasonYear;
	public int getPredId() {
		return predId;
	}
	public void setPredId(int predId) {
		this.predId = predId;
	}
	public int getSeasonYear() {
		return seasonYear;
	}
	public void setSeasonYear(int seasonYear) {
		this.seasonYear = seasonYear;
	}
	
	
}
