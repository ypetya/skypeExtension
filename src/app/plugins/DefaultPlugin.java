package app.plugins;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import app.console.ErrorCodes;
import app.plugins.base.PluginBase;

// This is a special plugin
// It will create the SystemTray popup and implements exit function
public class DefaultPlugin extends PluginBase implements ActionListener, ItemListener {

	public static final String COMMAND_EXIT = "exit";
	private static final String ICON_TRAY = "icon.png";

	private TrayIcon trayIcon = null;
	private PopupMenu popup = null;

	private final ActionListener defaultTrayListener;
	private final ItemListener defaultCheckedTrayListener;

	public DefaultPlugin(ActionListener systrayClick, ItemListener systrayCheck) {
		defaultTrayListener = systrayClick;
		defaultCheckedTrayListener = systrayCheck;
	}

	@Override
	public boolean init() {

		// load an image
		File icon = new File(DefaultPlugin.ICON_TRAY);
		Image image = null;
		try {
			image = ImageIO.read(icon); // new Image
		} catch (TypeNotPresentException te) {
			System.out.println("Invalid tray icon.");
			System.exit(ErrorCodes.TRAY_ICON_ERROR.getValue());
		} catch (IOException e) {
			System.out.println("Invalid tray icon.");
			System.exit(ErrorCodes.TRAY_ICON_ERROR.getValue());
		}

		// create a popup menu
		popup = new PopupMenu();

		trayIcon = new TrayIcon(image, "Skype bot", popup);
		trayIcon.addActionListener(defaultTrayListener);

		try {
			SystemTray tray = SystemTray.getSystemTray();
			tray.add(trayIcon);
		} catch (AWTException e) {
			System.err.println(e);
		}

		return true;
	}

	@Override
	public void dispose() {
		trayIcon = null;
		popup = null;
	}

	public PopupMenu getPopup() {
		return popup;
	}

	@Override
	public String getTrayCommandName() {
		return COMMAND_EXIT;
	}

	@Override
	public boolean isSwitchable() {
		return false;
	}

	public void actionPerformed(ActionEvent e) {
		defaultTrayListener.actionPerformed(e);
	}

	public void itemStateChanged(ItemEvent e) {
		defaultCheckedTrayListener.itemStateChanged(e);
	}
}
