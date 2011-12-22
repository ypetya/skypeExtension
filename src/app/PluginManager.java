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
import java.util.Set;
import java.util.TreeSet;

import app.console.ErrorCodes;
import app.plugins.DefaultPlugin;
import app.plugins.base.Plugin;

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
public class PluginManager implements PluginEventInteface {

	private List<Plugin> plugins = null;

	private DefaultPlugin systray;

	private final boolean useAwt;

	private PluginManager() {
		useAwt = true;
		init();
	}

	private PluginManager(boolean useAwt) {
		this.useAwt = useAwt;
		init();
	}

	private void init() {
		plugins = new ArrayList<Plugin>(10);

		if (useAwt) {
			// creating the popup menu & handle it as a special action handler
			// of
			// plugin manager...
			systray = new DefaultPlugin(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					String action = e.getActionCommand();
					eventActionPerformed(action);
				}
			}, new ItemListener() {

				public void itemStateChanged(ItemEvent e) {
					String action = (String) e.getItem();

					eventItemStateChanged(action, e.getStateChange() == ItemEvent.SELECTED);
				}

			});
		} else {
			// we do not need default plugin to handle awt events
			// no awt mode does not uses awt events
		}

		addPlugin(systray);
	}

	// non awt events
	@Override
	public synchronized void eventActionPerformed(String action) {
		if (action.equals(DefaultPlugin.COMMAND_EXIT)) {
			dispose();
		} else if (action != null) {
			// search for click
			for (Plugin p : plugins) {
				if (action.equals(p.getTrayCommandName())) {
					// switchPluginState(p);
					p.click();
					break;
				}
			}
		}
	}

	@Override
	public synchronized void eventItemStateChanged(String action, boolean enable) {
		for (Plugin p : plugins) {
			if (action.equals(p.getTrayCommandName())) {
				if (enable) {
					enablePlugin(p);
				} else {
					disablePlugin(p);
				}

				break;
			}
		}
	}
	
	public Set<String> getPlugins() {
		Set<String> pluginNames = new TreeSet<String>();
		for(Plugin o: plugins) {
			pluginNames.add(o.getTrayCommandName());
		}

		return pluginNames;
	}
	
	public boolean isPluginEnabled(String plugin) {
		for (Plugin p : plugins) {
			if (plugin.equals(p.getTrayCommandName())) {
				return p.isEnabled();
			}
		}
		return false;
	}

	public void addPlugin(Plugin p) {
		if (p == null)
			throw new IllegalArgumentException("plugin can not be null");

		// initialize plugin
		p.init();
		plugins.add(p);

		MenuItem mi = useAwt ? addMenuItem(p) : null;

		if (p.isSwitchable()) {
			if (p.isMenuEnabledOnStartup()) {
				enablePlugin(p);
				if(useAwt) {
					((CheckboxMenuItem) mi).setState(true);
				}
			} else
				if(useAwt) {
					((CheckboxMenuItem) mi).setState(false);
				}
		}
	}

	private void checkPluginExists(Plugin p) {
		if (plugins.indexOf(p) < 0) {
			throw new NoSuchElementException("plugin not found");
		}
	}

	public void removePlugin(Plugin p) {
		checkPluginExists(p);
		if(useAwt) {
			removeMenuItem(p.getTrayCommandName());
		}
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
		return getInstance(true);
	}

	public static PluginManager getInstance(boolean useAwt) {

		if (theManager == null) {
			theManager = new PluginManager(useAwt);
		}
		return theManager;
	}

	private void removeMenuItem(String commandName) {
		if (systray.getPopup() == null)
			return;
		int ic = systray.getPopup().getItemCount();
		for (int i = 0; i < ic; i++) {
			if (systray.getPopup().getItem(i).getActionCommand().equals(commandName)) {
				systray.getPopup().remove(i);
				break;
			}
		}
	}

	private MenuItem addMenuItem(Plugin p) {

		if (p.isSwitchable()) {
			CheckboxMenuItem item = new CheckboxMenuItem(p.getTrayCommandName());
			item.addItemListener(systray);
			systray.getPopup().add(item);
			return item;
		} else {
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

		System.exit(ErrorCodes.NONE.getValue());
	}

	private void addSkypeListener(ChatMessageListener chatListener) {
		try {
			Skype.addChatMessageListener(chatListener);
		} catch (SkypeException se) {
			System.out.println("Can not add listener.");
			System.exit(ErrorCodes.CAN_NOT_ADD_LISTENER.getValue());
		}
	}

	private void removeSkypeListener(ChatMessageListener chatListener) {
		if (chatListener != null) {
			Skype.removeChatMessageListener(chatListener);
		}
	}
}
