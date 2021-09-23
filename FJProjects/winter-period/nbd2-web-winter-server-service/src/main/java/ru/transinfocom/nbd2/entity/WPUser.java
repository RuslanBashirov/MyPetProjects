package ru.transinfocom.nbd2.entity;

public class WPUser {

	private int sessionId;
	private String firstName;
	private String lastName;
	private String patrName;
	private String userName;
	private int asutUserId;
	private int predId;
	private String predName;
	private String passwordHash;
	private String role = "ROLE_DEFAULT";

	public WPUser() {
	}

	public WPUser(int sessionId, String firstName, String lastName, String patrName, String userName, int asutUserId, int predId,
			String predName, String passwordHash) {
		super();
		this.sessionId = sessionId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.patrName = patrName;
		this.userName = userName;
		this.asutUserId = asutUserId;
		this.predId = predId;
		this.predName = predName;
		this.passwordHash = passwordHash;
	}
	
	

	public int getSessionId() {
		return sessionId;
	}

	public void setSessionId(int sessionId) {
		this.sessionId = sessionId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String username) {
		this.userName = username;
	}

	public int getAsutUserId() {
		return asutUserId;
	}

	public void setAsutUserId(int asutUserId) {
		this.asutUserId = asutUserId; 
	}
  
	public int getPredId() {
		return predId;
	}

	public void setPredId(int predId) {
		this.predId = predId;
	}
	
	
	
	public String getPredName() {
		return predName;
	}

	public void setPredName(String predName) {
		this.predName = predName;
	}

	public String getPasswordHash() {
		return passwordHash;
	}

	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}

	public String getRole() {
		return role;
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

	public void setRole(String role) {
		this.role = role;
	}

	@Override
	public String toString() {
		return "WPUser [firstName=" + firstName + ", lastName=" + lastName + ", userName=" + userName + ", asutUserId="
				+ asutUserId + ", predId=" + predId + "]";
	}
	
	
}
