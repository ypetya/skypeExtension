package app.console;

import java.util.Set;

import app.PluginManager;

public enum ConsoleCommand {
	EXIT() {
		@Override
		public void action() {
			System.out.println("bye");
			System.exit(0);
		}		
	},
	QUIT() {
		@Override
		public void action() {
			System.out.println("bye");
			System.exit(0);
		}
	},
	HELP() {
		@Override
		public void action() {
			PluginManager pm = PluginManager.getInstance();
			
			Set<String> plugins = pm.getPlugins();
			
			System.out.println("Available plugins. (Loaded at start) :");
			
			for(String p : plugins) {
				System.out.format(" %1s %2s\n", p, pm.isPluginEnabled(p));
			}
			
			System.out.println("To fire an event write the plugin name and one of the following : ");
			
			for(PluginCommand command : PluginCommand.values()) {
				System.out.println(" " + command.name());
			}
		}
	};
	ConsoleCommand(){};

	
	
	public abstract void action();
}
