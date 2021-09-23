package ru.transinfocom.nbd2.entity.response;

import java.util.List;

import ru.transinfocom.nbd2.entity.PredTreeRow;

public class GetTreeResponse {
	private List<PredTreeRow> listPredTreeRow;
	private String message;
	
	
	
	public GetTreeResponse(List<PredTreeRow> listPredTreeRow, String message) {
		super();
		this.listPredTreeRow = listPredTreeRow;
		this.message = message;
	}
	public List<PredTreeRow> getListPredTreeRow() {
		return listPredTreeRow;
	}
	public void setListPredTreeRow(List<PredTreeRow> listPredTreeRow) {
		this.listPredTreeRow = listPredTreeRow;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	
}
