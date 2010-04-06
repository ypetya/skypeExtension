package app;

import com.skype.ChatMessage;
import com.skype.ChatMessageAdapter;
import com.skype.Skype;
import com.skype.SkypeException;

public class Console {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		Skype.setDeamon(false); // to prevent exiting from this program
		Skype.addChatMessageListener(new ChatMessageAdapter() {
			public void chatMessageReceived(ChatMessage received) throws SkypeException {
				if (received.getType().equals(ChatMessage.Type.SAID)) {
					received.getSender().send("I'm working. Please, wait a moment.");
				}
			}
		});
	}

}
