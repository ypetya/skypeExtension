package app;

public interface PluginEventInteface {
	/** 
	 * You can fire an event on the Plugin Manager
	 * It is like you click the awt menu item. Every plugin has a special name. That is event name..
	 * */
	void eventActionPerformed(String action);
	/**
	 * You can enable / disable plugins which are running in the background via this method
	 * */
	void eventItemStateChanged(String action, boolean enable);
}
