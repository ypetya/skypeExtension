package app.plugins;

import com.skype.ChatMessage;
import com.skype.ContactList;
import com.skype.Friend;
import com.skype.Skype;
import com.skype.SkypeException;

public class NewlAggregatorFromHistory implements Plugin {

	private static final String COMMAND_IMPORT_HISTORY = "import history";
	private static Thread historyHandler = null;
	private static boolean stop = false;

	@Override
	public String getTrayCommandName() {
		return COMMAND_IMPORT_HISTORY;
	}

	/** when click arrives start an importer thread if it is not running yet */
	@Override
	public void click() {
		if (historyHandler == null) {
			historyHandler = new Thread(new Runnable() {

				@Override
				public void run() {
					try {
						ContactList c = Skype.getContactList();
						int counter = 0;
						for (Friend f : c.getAllFriends()) {
							for (ChatMessage cm : f.getAllChatMessages()) {
								if (stop)
									return;
								try {
									NewlAggregator.handleMessage(cm);
									if (counter++ % 5 == 0) {
										Thread.sleep(1000);
										System.out.println("handled " + counter + " messages");
									}
								} catch (Exception e) {

								}
							}
						}
					} catch (SkypeException e) {

						e.printStackTrace();
					}

				}

			});
			
			historyHandler.start();
		}
	}

	/** if history importer thread exists we will kill it */
	@Override
	public void dispose() {
		stop = true;
	}

	@Override
	public void enable() {
	}

	@Override
	public void disable() {
	}

	/** We are doing here noting */
	@Override
	public boolean init() {
		return false;
	}

	@Override
	public boolean isMenuEnabledOnStartup() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isSwitchable() {
		return false;
	}

	@Override
	public void chatMessageReceived(ChatMessage arg0) throws SkypeException {
	}

	@Override
	public void chatMessageSent(ChatMessage arg0) throws SkypeException {
	}

}
