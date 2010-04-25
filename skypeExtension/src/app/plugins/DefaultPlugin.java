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

import app.SkypeTray;

import com.skype.ChatMessage;
import com.skype.SkypeException;

// This is a special plugin
// It will create the SystemTray popup and implements exit function
public class DefaultPlugin implements Plugin, ActionListener, ItemListener {
	
	public static final String COMMAND_EXIT = "exit";
	private static final String ICON_TRAY = "icon.png";
	
	private TrayIcon trayIcon = null;	
	private PopupMenu popup = null;
	
	private final ActionListener defaultTrayListener;
	private final ItemListener defaultCheckedTrayListener;
	
	
	public DefaultPlugin(ActionListener systrayClick, ItemListener systrayCheck ){
		defaultTrayListener = systrayClick;
		defaultCheckedTrayListener = systrayCheck;
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
		trayIcon.addActionListener(defaultTrayListener);
/*
		// this will show up popup
		trayIcon.addMouseListener(new MouseListener(){

			public void mouseClicked(MouseEvent arg0) {}

			public void mouseEntered(MouseEvent arg0) {}

			public void mouseExited(MouseEvent arg0) {}

			 public void mousePressed(MouseEvent e) {
			        maybeShowPopup(e);
			    }

			    public void mouseReleased(MouseEvent e) {
			        maybeShowPopup(e);
			    }

			    private void maybeShowPopup(MouseEvent e) {
			        if (e.isPopupTrigger()) {
			            popup.show(e.getComponent(),
			                       e.getX(), e.getY());
			        }
			    }
			}
		);
*/
		
		try {
			SystemTray tray = SystemTray.getSystemTray();
			tray.add(trayIcon);
		} catch (AWTException e) {
			System.err.println(e);
		}

	}
	
	public void dispose() {
		trayIcon = null;
		popup = null;
	}

	public void enable() {
		// do nothing
	}
	
	public void disable() {
		// do nothing
	}
		
	public PopupMenu getPopup(){
		return popup;
	}

	public String getTrayCommandName() {
		return COMMAND_EXIT;
	}
	
	public boolean isMenuEnabledOnStartup(){
		return true;
	}
	
	public boolean isSwitchable(){
		return false;
	}

	public void click() {
		
	}

	public void actionPerformed(ActionEvent e) {
		defaultTrayListener.actionPerformed(e);		
	}

	public void chatMessageReceived(ChatMessage arg0) throws SkypeException {
		// nothing to do		
	}

	public void chatMessageSent(ChatMessage arg0) throws SkypeException {
		// nothing to do		
	}

	public void itemStateChanged(ItemEvent e) {
		defaultCheckedTrayListener.itemStateChanged(e);
	}
}
