package ru.transinfocom.nbd2.entity.response;

import java.util.List;

import ru.transinfocom.nbd2.entity.ChangesHistory;

public class GetChangesHistoryResponse {
	private List<ChangesHistory> listChangesHistory;
	private String message;
	
	
	
	public GetChangesHistoryResponse(List<ChangesHistory> listChangesHistory, String message) {
		super();
		this.listChangesHistory = listChangesHistory;
		this.message = message;
	}
	public List<ChangesHistory> getListChangesHistory() {
		return listChangesHistory;
	}
	public void setListChangesHistory(List<ChangesHistory> listChangesHistory) {
		this.listChangesHistory = listChangesHistory;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
}
