package app.plugins;

import java.awt.event.ActionListener;

import com.skype.ChatMessageAdapter;

public interface Plugin {

	// this will called on plugin initialization
	public void init();
	
	// this will called on program exit
	public void dispose();

	// this is an interface for answer messages
	public ChatMessageAdapter getChatMessageListener();

	// this is an interface to react on menu System tray menu actions
	public ActionListener getTrayListener();

	// this is the default item String representation in SystemTray menu
	public String getTrayCommandName();
	
	public boolean isMenuEnabledOnStartup();
	
	public boolean isSwitchable();
}
