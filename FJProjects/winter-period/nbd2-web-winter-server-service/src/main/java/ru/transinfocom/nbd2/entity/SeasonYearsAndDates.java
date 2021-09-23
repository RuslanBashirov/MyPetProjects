package ru.transinfocom.nbd2.entity;

public class SeasonYearsAndDates implements Comparable<SeasonYearsAndDates> {

	private int predId;
	private String name;
	private int seasonYear;
	private String startDate;
	private String finishDate;
	private String firstName;
	private String lastName;
	private String patrName;
	private String lastUpdate;

	public SeasonYearsAndDates() {

	}

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

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPatrName() {
		return patrName;
	}

	public void setPatrName(String patrName) {
		this.patrName = patrName;
	}

	public String getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(String lastUpdate) {
		this.lastUpdate = lastUpdate;
	}
	
	

	@Override
	public String toString() {
		return "SeasonYearsAndDates [predId=" + predId + ", name=" + name + ", seasonYear=" + seasonYear
				+ ", startDate=" + startDate + ", finishDate=" + finishDate + ", firstName=" + firstName + ", lastName="
				+ lastName + ", patrName=" + patrName + ", lastUpdate=" + lastUpdate + "]";
	}

	@Override
	public int compareTo(SeasonYearsAndDates o) {
		return o.getSeasonYear() - seasonYear;
	}

}
