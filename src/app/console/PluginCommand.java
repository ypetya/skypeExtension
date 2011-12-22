package app.console;

import app.PluginManager;

public enum PluginCommand {
	ACTION {
		@Override
		public void action(String plugin) {
			pm.eventActionPerformed(plugin);
		}
	},
	ENABLE {
		@Override
		public void action(String plugin) {
			pm.eventItemStateChanged(plugin, true);
		}
	},
	DISABLE {
		@Override
		public void action(String plugin) {
			pm.eventItemStateChanged(plugin, false);
		}
	};
	
	PluginManager pm;
	
	PluginCommand() {
		pm = PluginManager.getInstance();
	}

	public abstract void action(String plugin);
}
