package net.teendevops;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.gson.JsonObject;

public class Message {
	
	private static final SimpleDateFormat formatter = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
	
	private String username;
	private Date timestamp;
	private int channel;
	private String message;
	private boolean deleted;
	private int message_id;
	
	public Message(String message, Channel to) {
		
		this.message = message;
		channel = to.getId();
		
	}
	
	public Message(String message, int toChannel) {
		
		this.message = message;
		channel = toChannel;
		
	}
	
	private Message() {}
	
	public static Message build(JsonObject object) throws ParseException {
		
		Message built = new Message();
		built.setUsername(object.get("username").getAsString());
		built.setTimestamp(formatter.parse(object.get("timestamp").getAsString()));
		built.setChannel(object.get("channel").getAsInt());
		built.setMessage(object.get("message").getAsString());
		built.setDeleted(object.get("deleted").getAsBoolean());
		built.setMessage_id(object.get("message_id").getAsInt());
		return built;
		
	}

	public String getUsername() {
		
		return username;
		
	}

	private void setUsername(String username) {
		
		this.username = username;
		
	}

	public Date getTimestamp() {
		
		return timestamp;
		
	}

	private void setTimestamp(Date timestamp) {
		
		this.timestamp = timestamp;
		
	}

	public int getChannel() {
		
		return channel;
		
	}

	private void setChannel(int channel) {
		
		this.channel = channel;
		
	}

	public String getMessage() {
		
		return message;
		
	}

	private void setMessage(String message) {
		
		this.message = message;
		
	}

	public boolean isDeleted() {
		
		return deleted;
		
	}

	private void setDeleted(boolean deleted) {
		
		this.deleted = deleted;
		
	}

	public int getMessage_id() {
		
		return message_id;
		
	}

	private void setMessage_id(int message_id) {
		
		this.message_id = message_id;
		
	}
	
	@Override
	public boolean equals(Object other) {
		
		if (!(other instanceof Message)) {
			
			return false;
			
		}
		
		return ((Message) other).getMessage_id() == getMessage_id();
		
	}
	
	@Override
	public String toString() {
		
		return getUsername() + ": " + getMessage();
		
	}
	
}