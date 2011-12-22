package app;

import java.awt.SystemTray;

import app.console.ErrorCodes;
import app.plugins.Aggregator;
import app.plugins.AggregatorFromHistory;
import app.plugins.ChatHistory;
import app.plugins.Netpincer;
import app.plugins.TestAnswerer;

import com.skype.Skype;

public class SkypeTray {

	/**
	 * This is the main entry point of the awt systray application
	 * 
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		if (!SystemTray.isSupported()) {
			// disable tray option in your application or
			// perform other actions
			System.out.println("System tray not supported");
			System.exit(ErrorCodes.TRAY_ICON_NOT_SUPPORTED.getValue());
		}
		if (Skype.isRunning()) {
			PluginManager m = PluginManager.getInstance();
			m.addPlugin(new Aggregator());
			m.addPlugin(new AggregatorFromHistory());
			m.addPlugin(new Netpincer());
			m.addPlugin(new TestAnswerer());
			m.addPlugin(new ChatHistory());
			//m.addPlugin(new ClickTest());
			//m.addPlugin(new CheckTest());
			Skype.setDeamon(false); // to prevent exiting from this program	
		} else {
			System.out.println("Please start a Skype client!");
			System.exit(ErrorCodes.CAN_NOT_FIND_SKYPE_INSTANCE.getValue());
		}
	}
	
}
