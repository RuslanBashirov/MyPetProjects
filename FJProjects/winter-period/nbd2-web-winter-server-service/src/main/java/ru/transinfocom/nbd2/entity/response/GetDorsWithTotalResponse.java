package ru.transinfocom.nbd2.entity.response;

import ru.transinfocom.nbd2.entity.DorsWithTotal;

public class GetDorsWithTotalResponse {
	private DorsWithTotal dorsWithTotal;
	private String message;
	
	
	public GetDorsWithTotalResponse(DorsWithTotal dorsWithTotal, String message) {
		super();
		this.dorsWithTotal = dorsWithTotal;
		this.message = message;
	}
	public DorsWithTotal getDorsWithTotal() {
		return dorsWithTotal;
	}
	public void setDorsWithTotal(DorsWithTotal dorsWithTotal) {
		this.dorsWithTotal = dorsWithTotal;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
}