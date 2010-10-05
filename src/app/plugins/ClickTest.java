package app.plugins;

import com.skype.ChatMessage;
import com.skype.SkypeException;

public class ClickTest implements Plugin {

	private static final String COMMAND_CLICK_ME = "click me";

	public String getTrayCommandName() {
		return COMMAND_CLICK_ME;
	}

	public boolean init() {
		System.out.println("nothing to init");
		return true;
	}
	
	public void dispose() {
		System.out.println("nothing to dispose");		
	}
	
	public void enable() {
		System.out.println("nothing to enable");		
	}
	
	public void disable() {
		System.out.println("nothing to disable");	
	}

	public boolean isSwitchable() {
		// it is a click on menuItem so switchable is no option
		return false;
	}
	
	public boolean isMenuEnabledOnStartup() {
		// It has no meaning here
		return false;
	}	

	public void click() {
		System.out.println("clicked event");
	}

	public void chatMessageReceived(ChatMessage arg0) throws SkypeException {
		System.out.println("chatmessage received");
	}

	public void chatMessageSent(ChatMessage arg0) throws SkypeException {
		System.out.println("chatmessage sent");
		
	}

}
