package app;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.skype.ChatMessage;
import com.skype.ChatMessageAdapter;
import com.skype.Skype;
import com.skype.SkypeException;

public class SkypeTray {

	private static final String ICON_TRAY = "icon.png";

	private static final class ErrorCodes {
		private ErrorCodes() {
		}

		private static final int NONE = 0;
		private static final int CAN_NOT_ADD_LISTENER = 1;
		private static final int CAN_NOT_FOUND_SKYPE_INSTANCE = 2;
		private static final int TRAY_ICON_NOT_SUPPORTED = 3;
		private static final int TRAY_ICON_ERROR = 4;
	}

	private ChatMessageAdapter chatListener = null;
	
	private ActionListener trayListener = null; 
	
	private PopupMenu popup = null;

	private SkypeTray(ChatMessageAdapter listener) {
		
		// create a action listener to listen for default action executed on
		// the tray icon
		trayListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// execute default action of the application
				String actionCommand = e.getActionCommand();
				if (actionCommand.equals("exit")) {
					dispose();
					System.exit(SkypeTray.ErrorCodes.NONE);
				} else if (actionCommand.equals("history")){
					
				}
			}
		};
		
		chatListener = listener;
		
		try {
			Skype.addChatMessageListener(chatListener);
		} catch (SkypeException se) {
			System.out.println("Can not add listener.");
			System.exit(SkypeTray.ErrorCodes.CAN_NOT_ADD_LISTENER);
		}
	}

	private synchronized void dispose() {
		if (chatListener != null) {
			Skype.removeChatMessageListener(chatListener);
		}
	}

	public static SkypeTray init() {
		return new SkypeTray(new ChatMessageAdapter() {
			@Override
			public void chatMessageReceived(ChatMessage received)
					throws SkypeException {
				if (received.getType().equals(ChatMessage.Type.SAID)) {

					received.getSender().send(
							"I'm working. Please, wait a moment. :)");
				}
			}
		});
	}

	/**
	 * This is the main entry point
	 * 
	 * @param args
	 */
	public static void main(String[] args) throws Exception {

		if (Skype.isRunning()) {
			SkypeTray st = SkypeTray.init();
			st.startTray();
		} else {
			System.out.println("Please start a Skype client!");
			System.exit(SkypeTray.ErrorCodes.CAN_NOT_FOUND_SKYPE_INSTANCE);
		}

		Skype.setDeamon(false); // to prevent exiting from this program
	}
	

	public void startTray() {
		if (SystemTray.isSupported()) {
			TrayIcon trayIcon = null;
			// get the SystemTray instance
			SystemTray tray = SystemTray.getSystemTray();
			// load an image
			File icon = new File(ICON_TRAY);
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
			
			// add plugins :)
			addMenuItem("history");
			addMenuItem("exit");

			trayIcon = new TrayIcon(image, "Skype bot", popup);
			trayIcon.addActionListener(trayListener);

			try {
				tray.add(trayIcon);
			} catch (AWTException e) {
				System.err.println(e);
			}
		} else {
			// disable tray option in your application or
			// perform other actions
			System.out.println("System tray not supported");
			System.exit(SkypeTray.ErrorCodes.TRAY_ICON_NOT_SUPPORTED);
		}
	}

	private void addMenuItem(String commandName) {
		MenuItem item = new MenuItem(commandName);
		item.addActionListener( trayListener);
		popup.add(item);
	}
}
