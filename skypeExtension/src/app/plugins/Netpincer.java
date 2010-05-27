package app.plugins;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.skype.ChatMessage;
import com.skype.SkypeException;

public class Netpincer implements Plugin {

	public void click() {
		// TODO Auto-generated method stub
	}

	public void disable() {
		// TODO Auto-generated method stub

	}

	public void dispose() {
		// TODO Auto-generated method stub
	}

	public void enable() {
		// TODO Auto-generated method stub
	}

	public String getTrayCommandName() {
		return "netpincer";
	}

	public boolean init() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isMenuEnabledOnStartup() {
		return true;
	}

	public boolean isSwitchable() {
		return true;
	}

	public void chatMessageReceived(ChatMessage received) throws SkypeException {
		String msg = received.getContent();
			
		Pattern pattern = Pattern.compile("http://(www\\.){0,1}netpincer.*NPsession.*");
		Matcher matcher = pattern.matcher(msg);
		
		if(matcher.find()){
			received.getChat().send("Netpincer session detected: Looking for hawaii pizzas... (bandit)");
		}
		
	}

	public void chatMessageSent(ChatMessage arg0) throws SkypeException {
		// TODO Auto-generated method stub
	}

}
