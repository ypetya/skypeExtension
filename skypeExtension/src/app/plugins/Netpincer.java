package app.plugins;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.skype.ChatMessage;
import com.skype.SkypeException;

public class Netpincer implements Plugin {

	private static final String TITLE = "netpincer";

	public void click() {
	}

	public void disable() {
	}

	public void dispose() {
	}

	public void enable() {
	}

	public String getTrayCommandName() {
		return TITLE;
	}

	public boolean init() {
		return false;
	}

	public boolean isMenuEnabledOnStartup() {
		return true;
	}

	public boolean isSwitchable() {
		return true;
	}
	
	private static final String[] things = { "hawai pizzas", "cola zero", "darálthús", "vegetables" };
	
	public String getRandomThing() {
		Random r = new Random();
		return things[ r.nextInt(things.length) ];
	}

	public void chatMessageReceived(ChatMessage received) throws SkypeException {
		String msg = received.getContent();
			
		Pattern pattern = Pattern.compile("http://(www\\.){0,1}netpincer.*NPsession.*");
		Matcher matcher = pattern.matcher(msg);
		
		if(matcher.find()){
			received.getChat().send("Netpincer session detected: Looking for " + getRandomThing() + "... (bandit)");
		}
		
	}

	public void chatMessageSent(ChatMessage arg0) throws SkypeException {
	}

}
