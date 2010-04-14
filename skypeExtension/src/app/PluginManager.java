package app;

import java.awt.CheckboxMenuItem;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import app.plugins.DefaultPlugin;
import app.plugins.Plugin;

import com.skype.ChatMessageAdapter;
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
	private List<ChatMessageAdapter> chatListeners = null;
	private List<ActionListener> trayListeners = null;

	private final DefaultPlugin systray;

	private PluginManager() {
		plugins = new ArrayList<Plugin>(10);
		chatListeners = new ArrayList<ChatMessageAdapter>(10);
		trayListeners = new ArrayList<ActionListener>(10);

		// creating the popup menu & handle it as a special action handler of
		// plugin manager...
		systray = new DefaultPlugin(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				String action = e.getActionCommand();
				if( action == null){
					systray.getPopup().setEnabled(true);
				} else if (action.equals(DefaultPlugin.COMMAND_EXIT)){
					dispose();
				} else if( action != null ){
					for(Plugin p: plugins){
						if(action.equals(p.getTrayCommandName())){
							switchPluginState(p);
							break;
						}
					}
				}
			}
		});

		addPlugin(systray);
	}

	public void addPlugin(Plugin p) {
		if (p == null)
			throw new IllegalArgumentException("plugin can not be null");

		// initialize plugin
		p.init();
		plugins.add(p);
		
		addMenuItem(p.getTrayCommandName(), p.getTrayListener(), p.isSwitchable());
		
		if (p.isSwitchable()) {
			if (p.isMenuEnabledOnStartup())
				enablePlugin(p);
			else
				setMenuEnabledState(p, false);
		}
	}

	private void checkPluginExists(Plugin p) {
		if (plugins.indexOf(p) < 0) {
			throw new NoSuchElementException("plugin not found");
		}
	}

	public void removePlugin(Plugin p) {
		checkPluginExists(p);
		removeTrayListener(p.getTrayListener());
		removeChatListener(p.getChatMessageListener());
		plugins.remove(p);
	}
	
	// TODO: do not use popup loop here
	private boolean getPluginState(Plugin p){
		int items = systray.getPopup().getItemCount();
		for (int i = 0; i < items; i++) {
			Object o = systray.getPopup().getItem(i);
			if (o instanceof CheckboxMenuItem) {
				CheckboxMenuItem mi = (CheckboxMenuItem) o;
				if (mi.getActionCommand() == p.getTrayCommandName()) {
					return mi.getState();
				}
			}
		}
		return true;
	}
	
	private void switchPluginState(Plugin p){
		if(!p.isSwitchable()) return;
		if(getPluginState(p)) disablePlugin(p);
		else enablePlugin(p);
	}

	// TODO: do not use popup loop here
	private void setMenuEnabledState(Plugin p, boolean newState) {
		int items = systray.getPopup().getItemCount();
		for (int i = 0; i < items; i++) {
			Object o = systray.getPopup().getItem(i);
			if (o instanceof CheckboxMenuItem) {
				CheckboxMenuItem mi = (CheckboxMenuItem) o;
				if (mi.getActionCommand() == p.getTrayCommandName()) {
					mi.setState(newState);
					break;
				}
			}
		}
	}

	public void enablePlugin(Plugin p) {
		checkPluginExists(p);
		addChatListener(p.getChatMessageListener());
		setMenuEnabledState(p, true);
		p.enable();
	}

	public void disablePlugin(Plugin p) {
		checkPluginExists(p);
		removeChatListener(p.getChatMessageListener());
		setMenuEnabledState(p, false);
		p.disable();
	}

	private void addChatListener(ChatMessageAdapter adapter) {
		if (adapter != null) {
			addSkypeListener(adapter);
			chatListeners.add(adapter);
		}
	}

	private void removeChatListener(ChatMessageAdapter adapter) {
		if (adapter != null) {
			if (chatListeners.remove(adapter))
				removeSkypeListener(adapter);
		}
	}

	private void addTrayListener(ActionListener listener) {
		if (listener != null) {
			trayListeners.add(listener);
		}
	}

	private void removeTrayListener(ActionListener listener) {
		if (listener != null) {
			this.trayListeners.remove(listener);
		}
	}

	private static PluginManager theManager = null;

	public static PluginManager getInstance() {

		if (theManager == null) {
			theManager = new PluginManager();
		}
		return theManager;
	}

	private MenuItem addMenuItem(String commandName,
			ActionListener trayListener, boolean checkboxEnabled) {
		MenuItem item = null;
		if (checkboxEnabled)
			item = new CheckboxMenuItem(commandName);
		else
			item = new MenuItem(commandName);

		item.addActionListener(trayListener);
		systray.getPopup().add(item);
		if(trayListener != null)
			addTrayListener(trayListener);
		return item;
	}

	public static void close() {
		theManager.dispose();
		theManager = null;
	}

	private void dispose() {
		while (plugins.size() > 0) {
			Plugin p = plugins.get(0);
			removePlugin(p);
			p.dispose();
		}

		System.out.println("bye!");
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
