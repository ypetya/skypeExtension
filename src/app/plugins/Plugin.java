package app.plugins;

import com.skype.ChatMessageListener;

public interface Plugin extends ChatMessageListener {

	// this will called on plugin initialization
	// returns true if successed
	public boolean init();
	
	// this will called on program exit
	public void dispose();

	// this is the default item String representation in SystemTray menu
	public String getTrayCommandName();
		
	// if
	public boolean isSwitchable();
	// then
	public boolean isMenuEnabledOnStartup();
	public void enable();
	public void disable();
	// else
	public void click();	
}
