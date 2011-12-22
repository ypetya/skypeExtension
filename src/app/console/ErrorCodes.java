package app.console;


public enum ErrorCodes {
	NONE,
	CAN_NOT_ADD_LISTENER,
	CAN_NOT_FIND_SKYPE_INSTANCE,
	TRAY_ICON_NOT_SUPPORTED,
	TRAY_ICON_ERROR;
	
	public int getValue() {
		return this.ordinal();
	}
}