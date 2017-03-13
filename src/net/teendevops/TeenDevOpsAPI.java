package net.teendevops;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidParameterException;
import java.text.ParseException;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class TeenDevOpsAPI {
	
	public static final String BASE_URL = "http://teendevops.net/api/v1/";
	public static final int VERSION = 1;
	
	public static final String GET_CHANNELS_URL = "channels/get/";
	public static final String GET_CHAT_URL = "chat/get/";
	public static final String GET_USER_URL = "users/get/";
	public static final String GET_SIMILAR_USERS_URL = "users/findsimilar/";
	public static final String GET_SESSION_INFO = "auth/csrf/";
	
	public static final String POST_CHAT_URL = "chat/send/";
	public static final String POST_AUTH_URL = "auth/login/";
	
	private JsonParser json;
	private Session session;
	
	public TeenDevOpsAPI() throws IOException {
		
		json = new JsonParser();
		
		URL url = new URL(BASE_URL + GET_SESSION_INFO);
		JsonObject response = checkResponse(json.parse(GET(url)));
		session = new Session(response);
		
	}
	
	public TeenDevOpsAPI(String username, String password) throws IOException {
		
		this();
		
		URL url = new URL(BASE_URL + POST_AUTH_URL);
		String response = POST(url, "username=" + URLEncoder.encode(username, "UTF-8") + "&password=" + URLEncoder.encode(password, "UTF-8"));
		session = new Session(username, password, checkResponse(json.parse(response)));
		session.setUsername(username);
		session.setPassword(password);
		
	}
	
	public void postMessage(Message message) throws IOException {
		
		URL url = new URL(BASE_URL + POST_CHAT_URL);
		checkResponse(json.parse(POST(url, "channel=" + message.getChannel() + "&message=" + URLEncoder.encode(message.getMessage(), "UTF-8"))));
		
	}
	
	public Session getSession() {
		
		return session;
		
	}
	
	public Message[] getChannelMessages(Channel channel) throws IOException, ParseException {
		
		URL url = new URL(BASE_URL + GET_CHAT_URL + "?channel=" + channel.getId());
		JsonObject response = checkResponse(json.parse(GET(url)));
		JsonArray messagesJson = response.get("chat").getAsJsonArray();
		Message[] messages = new Message[messagesJson.size()];
		for (int i = 0; i < messages.length; i++) {
			
			JsonObject messageJson = messagesJson.get(i).getAsJsonObject();
			messages[i] = Message.build(messageJson);
			
		}
		return messages;
		
	}
	
	public Channel[] getChannels() throws IOException {
		
		URL url = new URL(BASE_URL + GET_CHANNELS_URL);
		JsonObject response = checkResponse(json.parse(GET(url)));
		JsonArray channelsJson = response.get("channels").getAsJsonArray();
		Channel[] channels = new Channel[channelsJson.size()];
		for (int i = 0; i < channels.length; i++) {
			
			JsonObject channelJson = channelsJson.get(i).getAsJsonObject();
			channels[i] = Channel.build(channelJson);
			
		}
		return channels;
		
	}
	
	public Channel getChannel(String title, Channel[] channels) {
		
		for (Channel channel : channels) {
			
			if (channel.getTitle().equals(title)) {
				
				return channel;
				
			}
			
		}
		return null;
		
	}
	
	public Channel getChannel(int id, Channel[] channels) {
		
		for (Channel channel : channels) {
			
			if (channel.getId() == id) {
				
				return channel;
				
			}
			
		}
		return null;
		
	}
	
	public User getUser(String username) throws IOException {
		
		URL url = new URL(BASE_URL + GET_USER_URL + "?username=" + username);
		JsonObject response = checkResponse(json.parse(GET(url)));
		return User.build(response.get("user").getAsJsonObject());
		
	}
	
	public User getUser(int id) throws IOException {
		
		URL url = new URL(BASE_URL + GET_USER_URL + "?id=" + id);
		JsonObject response = checkResponse(json.parse(GET(url)));
		return User.build(response.get("user").getAsJsonObject());
		
	}
	
	public User[] findSimilarUsers(String language) throws IOException {
		
		URL url = new URL(BASE_URL + GET_SIMILAR_USERS_URL + "?language=" + language);
		JsonObject response = checkResponse(json.parse(GET(url)));
		JsonArray usersJson = response.get("users").getAsJsonArray();
		User[] users = new User[usersJson.size()];
		for (int i = 0; i < usersJson.size(); i++) {
			
			JsonObject userJson = usersJson.get(i).getAsJsonObject();
			users[i] = User.build(userJson);
			
		}
		return users;
		
	}
	
	private String GET(URL url) throws IOException {
		
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");
		return readStream(connection.getInputStream());
		
	}
	
	private String POST(URL url, String body) throws IOException {
		
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestProperty("Cookie", "teendevops_session=" + session.getSessionid());
		connection.setDoOutput(true);
		connection.setDoInput(true);
		connection.setRequestMethod("POST");
		connection.getOutputStream().write((body + "&csrf=" + session.getCsrf()).getBytes(StandardCharsets.UTF_8));
		String response = readStream(connection.getInputStream());
		connection.disconnect();
		return response;
		
	}
	
	private static JsonObject checkResponse(JsonElement object) throws IOException {
		
		if (!object.isJsonObject()) {
			
			throw new InvalidParameterException("Element is not an Object");
			
		}
		JsonObject obj = object.getAsJsonObject();
		if (!obj.get("success").getAsBoolean()) {
			
			throw new IOException(obj.get("error").getAsString());
			
		}
		return obj;
		
	}
	
	private static String readStream(InputStream in) throws IOException {
		
		StringBuilder sb = new StringBuilder();
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String line;
		while ((line = br.readLine()) != null) {
			
			sb.append(line);
			
		}
		in.close();
		return sb.toString();
		
	}
	
	public static class Session {
		
		private String sessionid;
		private String csrf;
		private String username;
		private char[] password;
		
		private Session(JsonObject object) {
			
			this(null, null, object);
			
		}
		
		private Session(String username, String password, JsonObject object) {
			
			setSessionid(object.get("sessionid").getAsString());
			setCsrf(object.get("csrf").getAsString());
			setUsername(username);
			setPassword(password);
			
		}

		public String getSessionid() {
			
			return sessionid;
			
		}

		private void setSessionid(String sessionid) {
			
			this.sessionid = sessionid;
			
		}

		public String getCsrf() {
			
			return csrf;
			
		}

		private void setCsrf(String csrf) {
			
			this.csrf = csrf;
			
		}
		
		@Override
		public String toString() {
			
			return "sessionid: " + getSessionid() + ", csrf: " + getCsrf();
			
		}

		public String getUsername() {
			
			return username;
			
		}

		private void setUsername(String username) {
			
			this.username = username;
			
		}

		public char[] getPassword() {
			
			return password;
			
		}
		
		private void setPassword(String password) {
			
			this.password = password == null ? null : password.toCharArray();
			
		}
		
	}

}
