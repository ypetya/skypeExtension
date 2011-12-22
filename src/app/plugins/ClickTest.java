package app.plugins;

import app.plugins.base.PluginBase;

import com.skype.ChatMessage;
import com.skype.SkypeException;

public class ClickTest extends PluginBase {

	private static final String COMMAND_CLICK_ME = "click me";

	@Override
	public String getTrayCommandName() {
		return COMMAND_CLICK_ME;
	}

	@Override
	public boolean init() {
		System.out.println("nothing to init");
		return true;
	}
	
	@Override
	public void dispose() {
		System.out.println("nothing to dispose");
		super.dispose();
	}
	
	@Override
	public void enable() {
		System.out.println("nothing to enable");
		super.enable();
	}
	
	@Override
	public void disable() {
		System.out.println("nothing to disable");
		super.disable();
	}

	@Override
	public boolean isSwitchable() {
		// it is a click on menuItem so switchable is no option
		return false;
	}
	
	@Override
	public boolean isMenuEnabledOnStartup() {
		// It has no meaning here
		return false;
	}	

	@Override
	public void click() {
		System.out.println("clicked event");
	}

	@Override
	public void chatMessageReceived(ChatMessage arg0) throws SkypeException {
		System.out.println("chatmessage received");
	}

	@Override
	public void chatMessageSent(ChatMessage arg0) throws SkypeException {
		System.out.println("chatmessage sent");
		
	}

}
