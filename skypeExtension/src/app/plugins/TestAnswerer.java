package app.plugins;

import com.skype.ChatMessage;
import com.skype.SkypeException;

/* This is a basic plugin. It makes a simple menu item in the system tray name 'test plugin'
 * It can switch on and switch off.
 * Initial state is switched off. ( chatMessageAdapter not enabled )
 * If you click on the menu item, it will enable autoanswering machine and checks the menuItem. 
 */
public class TestAnswerer implements Plugin {
	
	private static final String COMMAND_TEST = "test plugin";
	
	public String getTrayCommandName() {
		return TestAnswerer.COMMAND_TEST;
	}
	
	public boolean init() {
		// do nothing
		return true;
	}
	
	public void dispose() {
		// do nothing
	}
	
	public void enable() {
		// do nothing
	}
	
	public void disable() {
		// do nothing
	}

		
	public boolean isSwitchable(){
		return true;
	}
	
	public boolean isMenuEnabledOnStartup(){
		return false;
	}

	public void click() {}


	public void chatMessageSent(ChatMessage received) throws SkypeException {
	}

	public void chatMessageReceived(ChatMessage received) throws SkypeException {
		received.getSender().send("I'm working. Please, wait a moment. :)");
	}
	
}
	
