package app;

import java.util.Scanner;

import app.console.ConsoleCommand;
import app.console.ErrorCodes;
import app.console.PluginCommand;
import app.helpers.ExecutionTime;
import app.plugins.Aggregator;
import app.plugins.AggregatorFromHistory;
import app.plugins.ChatHistory;
import app.plugins.Netpincer;
import app.plugins.TestAnswerer;

import com.skype.Skype;
import com.skype.SkypeException;

public class SkypeConsole {

	private String readCommand() {
			System.out.println("Please enter command! (help, quit, exit)");
			System.out.print(">");
			Scanner s = new Scanner(System.in);
			String line1 = s.nextLine();

			while (!"".equals(line1)) {
				line1 = s.nextLine();
			}

			return line1;
	}
	
	private void runConsole() {

		String command = null;
		while ((command = readCommand()) != null) {
			System.out.println();
			ExecutionTime ex = new ExecutionTime();
			
			try {
				ConsoleCommand cmd = ConsoleCommand.valueOf(command.toUpperCase());
				cmd.action();
			} catch(Exception e) {
			}
			
			try {
				String [] cmds = command.split(" ");
				PluginCommand pc = PluginCommand.valueOf(cmds[1].toUpperCase());
				pc.action(cmds[0]);
			} catch(Exception e) {
				System.out.println("Error");
			}
			
			System.out.println(" done. " + ex);
		}
	}
	
	
	/**
	 * @param args
	 * @throws SkypeException 
	 */
	public static void main(String[] args) throws SkypeException {
		
		System.out.println( System.getProperty("java.version") );
		
		if (Skype.isRunning()) {
			PluginManager m = PluginManager.getInstance();
			m.addPlugin(new Aggregator());
			m.addPlugin(new AggregatorFromHistory());
			m.addPlugin(new Netpincer());
			m.addPlugin(new TestAnswerer());
			m.addPlugin(new ChatHistory());
			//m.addPlugin(new ClickTest());
			//m.addPlugin(new CheckTest());
			Skype.setDeamon(true);
			
			SkypeConsole sc = new SkypeConsole();
			sc.runConsole();
		} else {
			System.out.println("Please start a Skype client!");
			System.exit(ErrorCodes.CAN_NOT_FIND_SKYPE_INSTANCE.getValue());
		}
	}

}
