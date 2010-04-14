package app.plugins;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import app.SkypeTray;

import com.skype.ChatMessageAdapter;

// This is a special plugin
// It will create the SystemTray popup and implements exit function
public class DefaultPlugin implements Plugin {
	
	public static final String COMMAND_EXIT = "exit";
	private static final String ICON_TRAY = "icon.png";
	private TrayIcon trayIcon = null;
	private PopupMenu popup = null;
	
	private final ActionListener defaultTrayListener; /*= */
	
	
	public DefaultPlugin(ActionListener exitListener){
		defaultTrayListener = exitListener;
	}
	
	public void dispose() {
		// nothing to do
	}

	public void enable() {
		// do nothing
	}
	
	public void disable() {
		// do nothing
	}

	public ChatMessageAdapter getChatMessageListener() {
		// no chat message listener
		return null;
	}

	public ActionListener getTrayListener() {
		return defaultTrayListener;
	}

	public void init() {
		
		// load an image
		File icon = new File(DefaultPlugin.ICON_TRAY);
		Image image = null;
		try {
			image = ImageIO.read(icon); // new Image
		} catch (TypeNotPresentException te) {
			System.out.println("Invalid tray icon.");
			System.exit(SkypeTray.ErrorCodes.TRAY_ICON_ERROR);
		} catch (IOException e) {
			System.out.println("Invalid tray icon.");
			System.exit(SkypeTray.ErrorCodes.TRAY_ICON_ERROR);
		}
		
		// create a popup menu
		popup = new PopupMenu();
		
		trayIcon = new TrayIcon(image, "Skype bot", popup);
		trayIcon.addActionListener(getDefaultTrayListener());

		try {
			SystemTray tray = SystemTray.getSystemTray();
			tray.add(trayIcon);
		} catch (AWTException e) {
			System.err.println(e);
		}

	}
	
	public PopupMenu getPopup(){
		return popup;
	}

	public String getTrayCommandName() {
		return COMMAND_EXIT;
	}

	public ActionListener getDefaultTrayListener() {
		if(defaultTrayListener == null ) 
			throw new IllegalStateException("Can not find default System Tray Listener ...");
		return defaultTrayListener;
	}

	public boolean isMenuEnabledOnStartup(){
		return true;
	}
	
	public boolean isSwitchable(){
		return false;
	}
}
