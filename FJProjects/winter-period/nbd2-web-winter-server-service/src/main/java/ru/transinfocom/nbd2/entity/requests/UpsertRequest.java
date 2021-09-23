package ru.transinfocom.nbd2.entity.requests;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UpsertRequest {

	private int predId;
	private int seasonYear;
	@JsonProperty(required = false)
	private String startDate;
	@JsonProperty(required = false)
	private String finishDate;
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
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getFinishDate() {
		return finishDate;
	}
	public void setFinishDate(String finishDate) {
		this.finishDate = finishDate;
	}
	
	
}
