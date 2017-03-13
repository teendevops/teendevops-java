package net.teendevops;

import java.util.Scanner;

public class Runner {

	public static void main(String[] args) throws Exception {
		
		Scanner in = new Scanner(System.in);
		
		System.out.println("teendevops.net CLI");
		System.out.println("Authorizing...");
		
		TeenDevOpsAPI api = new TeenDevOpsAPI("rory.eckel@gmail.com", "madmemez;");
		
		System.out.println("Loading channels...");
		
		Channel[] channels = api.getChannels();
		
		System.out.println("Please select a channel by ID:");
		for (Channel channel : channels) {
			
			System.out.println(channel);
			
		}
		Channel currentChannel = api.getChannel(in.nextInt(), channels);
		in.nextLine();
		System.out.println("Joined channel " + currentChannel.getTitle());
		
		new Thread() {
			
			@Override
			public void run() {
				
				
				
			}
			
		}.start();
		
		String input;
		while (!(input = in.nextLine()).equals("/exit")) {
			
			api.postMessage(new Message(input, currentChannel));
			
		}
		
		in.close();

	}

}