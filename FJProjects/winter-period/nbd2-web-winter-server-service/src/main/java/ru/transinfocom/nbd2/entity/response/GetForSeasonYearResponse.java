package ru.transinfocom.nbd2.entity.response;

import java.util.List;

import ru.transinfocom.nbd2.entity.PredIdsAndDates;

public class GetForSeasonYearResponse {
	private List<PredIdsAndDates> listPredIdsAndDates;
	private String message;
	
	
	
	public GetForSeasonYearResponse(List<PredIdsAndDates> listPredIdsAndDates, String message) {
		super();
		this.listPredIdsAndDates = listPredIdsAndDates;
		this.message = message;
	}
	public List<PredIdsAndDates> getListPredIdsAndDates() {
		return listPredIdsAndDates;
	}
	public void setListPredIdsAndDates(List<PredIdsAndDates> listPredIdsAndDates) {
		this.listPredIdsAndDates = listPredIdsAndDates;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
}
