package app.plugins.base;


import com.skype.ChatMessage;
import com.skype.SkypeException;

public abstract class PluginBase implements Plugin {
	
	boolean enabled;
	
	@Override
	public void disable() {
		enabled = false;
	}

	@Override
	public void dispose() {
		enabled = false;
	}

	@Override
	public void enable() {
		enabled = true;		
	}

	public void click() {
	}
	
	public boolean init() {
		return true;
	}

	public boolean isMenuEnabledOnStartup() {
		return true;
	}

	public boolean isSwitchable() {
		return true;
	}

	public abstract String getTrayCommandName();


	public void chatMessageReceived(ChatMessage arg0) throws SkypeException {}


	public void chatMessageSent(ChatMessage sent) throws SkypeException {}
	
	public boolean isEnabled() {
		return enabled;
	}
}
