package app;

import java.awt.CheckboxMenuItem;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import app.plugins.DefaultPlugin;
import app.plugins.Plugin;

import com.skype.ChatMessageListener;
import com.skype.Skype;
import com.skype.SkypeException;

/* This class is the main program written in the mind of keep it as simple as possible.
 * It will manage the plugins. 
 * The main functionality:
 * 0. Load default plugin to create SystemTray, etc.
 * 1. Load other plugins at startup ( call init-s )
 * 2. Handle Actionhandlers for both of SystemTray and skype actions.
 * 3. Unload plugins on exit
 * */
public class PluginManager {

	private List<Plugin> plugins = null;
	
	private final DefaultPlugin systray;

	private PluginManager() {
		plugins = new ArrayList<Plugin>(10);
	
		// creating the popup menu & handle it as a special action handler of
		// plugin manager...
		systray = new DefaultPlugin(
			new ActionListener() {
	
				public void actionPerformed(ActionEvent e) {
					String action = e.getActionCommand();
					
					if (action.equals(DefaultPlugin.COMMAND_EXIT)) {
						dispose();
					} else if (action != null) {
						// search for click
						for (Plugin p : plugins) {
							if (action.equals(p.getTrayCommandName())) {
								//switchPluginState(p);
								p.click();
								break;
							}
						}
					}
				}
			},
			new ItemListener(){
	
				public void itemStateChanged(ItemEvent e) {
					String action = (String)e.getItem();

					for (Plugin p : plugins) {
						if (action.equals(p.getTrayCommandName())) {
							if(e.getStateChange() == ItemEvent.SELECTED ){
								enablePlugin(p);
							}
							else {
								disablePlugin(p);
							}
							
							break;
						}
					}
	
				}
				
			}
			);
	
			addPlugin(systray);
		}

	public void addPlugin(Plugin p) {
		if (p == null)
			throw new IllegalArgumentException("plugin can not be null");

		// initialize plugin
		p.init();
		plugins.add(p);

		MenuItem mi = addMenuItem(p);

		if (p.isSwitchable()) {
			if (p.isMenuEnabledOnStartup()){
				enablePlugin(p);
				((CheckboxMenuItem)mi).setState(true);
			}
			else
				((CheckboxMenuItem)mi).setState(false);
		}
	}

	private void checkPluginExists(Plugin p) {
		if (plugins.indexOf(p) < 0) {
			throw new NoSuchElementException("plugin not found");
		}
	}

	public void removePlugin(Plugin p) {
		checkPluginExists(p);
		removeMenuItem(p.getTrayCommandName());
		p.dispose();	
		plugins.remove(p);
	}

	public void enablePlugin(Plugin p) {
		checkPluginExists(p);
		addSkypeListener(p);
		p.enable();
	}

	public void disablePlugin(Plugin p) {
		checkPluginExists(p);
		removeSkypeListener(p);
		p.disable();
	}

	private static PluginManager theManager = null;

	public static PluginManager getInstance() {

		if (theManager == null) {
			theManager = new PluginManager();
		}
		return theManager;
	}

	private void removeMenuItem(String commandName) {
		if(systray.getPopup() == null) return;
		int ic = systray.getPopup().getItemCount();
		for(int i = 0; i < ic ; i++){
			if(systray.getPopup().getItem(i).getActionCommand().equals(commandName)){
				systray.getPopup().remove(i);
				break;
			}
		}
	}

	private MenuItem addMenuItem(Plugin p) {
		
		if (p.isSwitchable()){
			CheckboxMenuItem item = new CheckboxMenuItem(p.getTrayCommandName());
			item.addItemListener(systray);
			systray.getPopup().add(item);
			return item;
		}
		else{
			MenuItem item = new MenuItem(p.getTrayCommandName());
			item.addActionListener(systray);
			systray.getPopup().add(item);
			return item;
		}

	}

	public static void close() {
		theManager.dispose();
		theManager = null;
	}

	private void dispose() {
		while (plugins.size() > 0) {
			Plugin p = plugins.get(0);
			removePlugin(p);
		}

		System.exit(SkypeTray.ErrorCodes.NONE);
	}

	private void addSkypeListener(ChatMessageListener chatListener) {
		try {
			Skype.addChatMessageListener(chatListener);
		} catch (SkypeException se) {
			System.out.println("Can not add listener.");
			System.exit(SkypeTray.ErrorCodes.CAN_NOT_ADD_LISTENER);
		}
	}

	private void removeSkypeListener(ChatMessageListener chatListener) {
		if (chatListener != null) {
			Skype.removeChatMessageListener(chatListener);
		}
	}
}
