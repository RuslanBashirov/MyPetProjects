package ru.transinfocom.nbd2.entity;

public class PredTreeRow {

	private int predId;
	private int parentPredId;
	private String name;
	private int dorKod;
	
	public PredTreeRow() {
		
	}

	public PredTreeRow(int predId, int parentPredId, String name, int dorKod) {
		super();
		this.predId = predId;
		this.parentPredId = parentPredId;
		this.name = name;
		this.dorKod = dorKod;
	}

	public int getPredId() {
		return predId;
	}

	public void setPredId(int predId) {
		this.predId = predId;
	}

	public int getParentPredId() {
		return parentPredId;
	}

	public void setParentPredId(int parentPredId) {
		this.parentPredId = parentPredId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getDorKod() {
		return dorKod;
	}

	public void setDorKod(int dorKod) {
		this.dorKod = dorKod;
	}

}
