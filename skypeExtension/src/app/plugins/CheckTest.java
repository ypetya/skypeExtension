package app.plugins;

import com.skype.ChatMessage;
import com.skype.SkypeException;

public class CheckTest implements Plugin {

	private static final String COMMAND_CHECK_TEST = "check test";

	public String getTrayCommandName() {
		return COMMAND_CHECK_TEST;
	}

	public void init() {
		System.out.println("nothing to init");
	}
	
	public void dispose() {
		System.out.println("nothing to dispose");
	}

	public void enable() {
		System.out.println("enable");
	}
	
	public void disable() {
		System.out.println("disable");
	}

	public void click() {
		System.out.println("click is a no option at checkbox");
	}	

	public boolean isMenuEnabledOnStartup() {
		return false;
	}

	public boolean isSwitchable() {
		return true;
	}

	public void chatMessageReceived(ChatMessage arg0) throws SkypeException {
		System.out.print("chat message received");
	}

	public void chatMessageSent(ChatMessage arg0) throws SkypeException {
		System.out.println("chat message sent");
	}

}
