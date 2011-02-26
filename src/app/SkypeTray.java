package app;

import java.awt.SystemTray;

import app.plugins.Netpincer;
import app.plugins.NewlAggregator;
import app.plugins.NewlAggregatorFromHistory;
import app.plugins.TestAnswerer;

import com.skype.Skype;

public class SkypeTray {

	
	public static enum ErrorCodes {
		NONE,
		CAN_NOT_ADD_LISTENER,
		CAN_NOT_FIND_SKYPE_INSTANCE,
		TRAY_ICON_NOT_SUPPORTED,
		TRAY_ICON_ERROR;
		
		public int getValue() {
			return this.ordinal();
		}
	}

	/**
	 * This is the main entry point
	 * It contains some environmental checks and default error codes
	 * 
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		if (!SystemTray.isSupported()) {
			// disable tray option in your application or
			// perform other actions
			System.out.println("System tray not supported");
			System.exit(SkypeTray.ErrorCodes.TRAY_ICON_NOT_SUPPORTED.getValue());
		}
		if (Skype.isRunning()) {
			PluginManager m = PluginManager.getInstance();
			m.addPlugin(new NewlAggregator());
			m.addPlugin(new NewlAggregatorFromHistory());
			m.addPlugin(new Netpincer());
			m.addPlugin(new TestAnswerer());
			//m.addPlugin(new ClickTest());
			//m.addPlugin(new CheckTest());
			Skype.setDeamon(false); // to prevent exiting from this program	
		} else {
			System.out.println("Please start a Skype client!");
			System.exit(SkypeTray.ErrorCodes.CAN_NOT_FIND_SKYPE_INSTANCE.getValue());
		}		
	}
	
}
