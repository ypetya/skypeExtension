package app.plugins;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import app.plugins.base.PluginBase;

import com.skype.ChatMessage;
import com.skype.SkypeException;

public class Netpincer extends PluginBase {

	private static final String TITLE = "netpincer";

	@Override
	public String getTrayCommandName() {
		return TITLE;
	}
	
	private static final String[] things = { "hawai pizzas", "cola zero", "darálthús", "vegetables" };
	
	public String getRandomThing() {
		Random r = new Random();
		return things[ r.nextInt(things.length) ];
	}

	@Override
	public void chatMessageReceived(ChatMessage received) throws SkypeException {
		String msg = received.getContent();
			
		Pattern pattern = Pattern.compile("http://(www\\.){0,1}netpincer.*NPsession.*");
		Matcher matcher = pattern.matcher(msg);
		
		if(matcher.find()){
			received.getChat().send("Netpincer session detected: Looking for " + getRandomThing() + "... (bandit)");
		}
		
	}
}
