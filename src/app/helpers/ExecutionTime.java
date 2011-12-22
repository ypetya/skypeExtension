package app.helpers;

public class ExecutionTime {

	private final long timeStamp = System.currentTimeMillis();

	@Override
	public String toString() {
		return String.valueOf( System.currentTimeMillis() - timeStamp + "ms");
	}

	public long getTimeStamp() {
		return timeStamp;
	}
	
	public boolean isElapsed(long millis) {
		return System.currentTimeMillis() - timeStamp > millis;
	}
}