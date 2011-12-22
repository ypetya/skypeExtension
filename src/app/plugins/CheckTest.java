package app.plugins;

import app.plugins.base.PluginBase;

import com.skype.ChatMessage;
import com.skype.SkypeException;

public class CheckTest extends PluginBase {

	private static final String COMMAND_CHECK_TEST = "check test";

	@Override
	public String getTrayCommandName() {
		return COMMAND_CHECK_TEST;
	}

	@Override
	public boolean init() {
		System.out.println("nothing to init");
		super.init();
		return true;
	}
	
	@Override
	public void dispose() {
		System.out.println("nothing to dispose");
	}

	@Override
	public void enable() {
		System.out.println("enable");
		super.enable();
	}
	
	@Override
	public void disable() {
		System.out.println("disable");
		super.disable();
	}

	@Override
	public void click() {
		System.out.println("click is a no option at checkbox");
		super.click();
	}	

	@Override
	public boolean isMenuEnabledOnStartup() {
		return false;
	}

	@Override
	public boolean isSwitchable() {
		return true;
	}

	@Override
	public void chatMessageReceived(ChatMessage arg0) throws SkypeException {
		System.out.print("chat message received");
	}

	@Override
	public void chatMessageSent(ChatMessage arg0) throws SkypeException {
		System.out.println("chat message sent");
	}

}
