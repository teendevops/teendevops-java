package net.teendevops;

import com.google.gson.JsonObject;

public class User {
	
	private int id;
	private String username;
	private int rank;
	private boolean banned;
	private String description;
	private String languages;
	private String location;
	
	private User() {}
	
	public static User build(JsonObject object) {
		
		User built = new User();
		built.setId(object.get("id").getAsInt());
		built.setUsername(object.get("username").getAsString());
		built.setRank(object.get("rank").getAsInt());
		built.setBanned(object.get("banned").getAsBoolean());
		built.setDescription(object.get("description").getAsString());
		built.setLanguages(object.get("languages").getAsString());
		built.setLocation(object.get("location").getAsString());
		return built;
		
	}

	public int getId() {
		
		return id;
		
	}

	private void setId(int id) {
		
		this.id = id;
		
	}

	public String getUsername() {
		
		return username;
		
	}

	private void setUsername(String username) {
		
		this.username = username;
		
	}

	public int getRank() {
		
		return rank;
		
	}

	private void setRank(int rank) {
		
		this.rank = rank;
		
	}

	public boolean isBanned() {
		
		return banned;
		
	}

	private void setBanned(boolean banned) {
		
		this.banned = banned;
		
	}

	public String getLanguages() {
		
		return languages;
		
	}

	private void setLanguages(String languages) {
		
		this.languages = languages;
		
	}

	public String getDescription() {
		
		return description;
		
	}

	private void setDescription(String description) {
		
		this.description = description;
		
	}

	public String getLocation() {
		
		return location;
		
	}

	private void setLocation(String location) {
		
		this.location = location;
		
	}
	
	@Override
	public boolean equals(Object other) {
		
		if (!(other instanceof User)) {
			
			return false;
			
		}
		
		return ((User) other).getId() == getId();
		
	}
	
	@Override
	public String toString() {
		
		return "[" + id + "] " + username + ": " + description;
		
	}
	
}