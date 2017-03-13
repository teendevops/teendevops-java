package net.teendevops;

import com.google.gson.JsonObject;

public class Channel {
	
	private int id;
	private String title;
	private String description;
	private String creator;
	
	private Channel() {}
	
	public static Channel build(JsonObject object) {
		
		Channel built = new Channel();
		built.setId(object.get("id").getAsInt());
		built.setTitle(object.get("title").getAsString());
		built.setDescription(object.get("description").getAsString());
		built.setCreator(object.get("creator").getAsString());
		return built;
		
	}

	public int getId() {
		
		return id;
		
	}

	private void setId(int id) {
		
		this.id = id;
		
	}

	public String getTitle() {
		
		return title;
		
	}

	private void setTitle(String title) {
		
		this.title = title;
		
	}

	public String getDescription() {
		
		return description;
		
	}

	private void setDescription(String description) {
		
		this.description = description;
		
	}

	public String getCreator() {
		
		return creator;
		
	}

	private void setCreator(String creator) {
		
		this.creator = creator;
		
	}
	
	@Override
	public boolean equals(Object other) {
		
		if (!(other instanceof Channel)) {
			
			return false;
			
		}
		
		return ((Channel) other).getId() == getId();
		
	}
	
	@Override
	public String toString() {
		
		return "[" + getId() + "] " + getTitle() + ": " + getDescription() + " (by " + getCreator() + ')';
		
	}
	
}
