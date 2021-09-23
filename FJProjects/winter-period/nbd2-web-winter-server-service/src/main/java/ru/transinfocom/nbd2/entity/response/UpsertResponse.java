package ru.transinfocom.nbd2.entity.response;

public class UpsertResponse {
	private String message;
	
	

	public UpsertResponse(String message) {
		super();
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
}
