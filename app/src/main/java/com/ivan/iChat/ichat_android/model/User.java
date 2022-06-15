package com.ivan.iChat.ichat_android.model;

public class User {

	private final int id;
	private String username;
	private String language;
	
	
	public User(int id, String username, String language) {
		this.id = id;
		this.username = username;
		this.language = language;
	}
	
	
	public int getId() { return id; }
	
	public String getUsername() { return username; }
	
	public String getLanguage() { return language; }
	
	
	
	
}
