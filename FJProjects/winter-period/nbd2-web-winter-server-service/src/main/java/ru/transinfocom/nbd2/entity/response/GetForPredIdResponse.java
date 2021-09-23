package ru.transinfocom.nbd2.entity.response;

import java.util.List;

import ru.transinfocom.nbd2.entity.SeasonYearsAndDates;

public class GetForPredIdResponse {
	private List<SeasonYearsAndDates> listSeasonYearsAndDates;
	private String message;
	
	
	
	
	public GetForPredIdResponse(List<SeasonYearsAndDates> listSeasonYearsAndDates, String message) {
		super();
		this.listSeasonYearsAndDates = listSeasonYearsAndDates;
		this.message = message;
	}
	public List<SeasonYearsAndDates> getListSeasonYearsAndDates() {
		return listSeasonYearsAndDates;
	}
	public void setListSeasonYearsAndDates(List<SeasonYearsAndDates> listSeasonYearsAndDates) {
		this.listSeasonYearsAndDates = listSeasonYearsAndDates;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
}
