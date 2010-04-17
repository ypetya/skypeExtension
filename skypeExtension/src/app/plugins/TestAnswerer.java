package app.plugins;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.skype.ChatMessage;
import com.skype.ChatMessageAdapter;
import com.skype.SkypeException;

/* This is a basic plugin. It makes a simple menu item in the system tray name 'test plugin'
 * It can switch on and switch off.
 * Initial state is switched off. ( chatMessageAdapter not enabled )
 * If you click on the menu item, it will enable autoanswering machine and checkes the menuItem. 
 */
public class TestAnswerer implements Plugin {
	
	private static String COMMAND_TEST = "test plugin";
	
	private final ChatMessageAdapter chatMessageAdapter = new ChatMessageAdapter() {
		@Override
		public void chatMessageReceived(ChatMessage received)
				throws SkypeException {
			if (received.getType().equals(ChatMessage.Type.SAID)) {

				received.getSender().send(
						"I'm working. Please, wait a moment. :)");
			}
		}
	};
		
	// Leave it on null if you won't make anything on menu click. 
	// (enable/disable chatMessageAdapter is a default behaviour here...)
	private final ActionListener menuActionListener = new ActionListener() {

		public void actionPerformed(ActionEvent e) {
			if (e.getActionCommand() == TestAnswerer.COMMAND_TEST){
				//we could do some interesting things here
			}
				
		}
	};
	
	public ChatMessageAdapter getChatMessageListener() {
		return chatMessageAdapter;		
	}

	public ActionListener getTrayListener() {
		return menuActionListener;
	}

	public void init() {
		// do nothing
	}
	
	public void dispose() {
		// do nothing
	}
	
	public void enable() {
		// do nothing
	}
	
	public void disable() {
		// do nothing
	}

	public String getTrayCommandName() {
		return TestAnswerer.COMMAND_TEST;
	}
	
	public boolean isSwitchable(){
		return true;
	}
	
	public boolean isMenuEnabledOnStartup(){
		return false;
	}
	
}
	