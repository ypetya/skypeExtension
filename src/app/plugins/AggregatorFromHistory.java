package app.plugins;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import app.plugins.base.PluginBase;

import com.skype.ChatMessage;
import com.skype.ContactList;
import com.skype.Friend;
import com.skype.Skype;
import com.skype.SkypeException;

public class AggregatorFromHistory extends PluginBase{

	private static final String COMMAND_IMPORT_HISTORY = "aggregator import history (1 month)";
	private static Thread historyHandler = null;
	private static boolean stop = false;

	@Override
	public String getTrayCommandName() {
		return COMMAND_IMPORT_HISTORY;
	}

	/** when click arrives start an importer thread unless it is running */
	@Override
	public void click() {
		if (historyHandler == null) {
			historyHandler = new Thread(new Runnable() {

				@Override
				public void run() {
					try {
						ContactList c = Skype.getContactList();
						int counter = 0;
						List<ChatMessage> allcm = new ArrayList<ChatMessage>();
						
						Calendar cal = Calendar.getInstance();
						cal.add(Calendar.MONTH, -1);
						Date oneMonthago = cal.getTime();
						
						for (Friend f : c.getAllFriends()) {
							for (ChatMessage cm : f.getAllChatMessages()) {
								if (stop)
									return;
								// dismiss this if you want full history scan
								if( cm.getTime().compareTo(oneMonthago) > 0 ) {
									allcm.add(cm);
								}
							}
						}
						System.out.println("got " + allcm.size() + " chatmessages from history.");

						Collections.sort(allcm, new Comparator<ChatMessage>() {

							@Override
							public int compare(ChatMessage cm1, ChatMessage cm2) {
								try {
									return cm1.getTime().compareTo(cm2.getTime());
								} catch (SkypeException e) {
									// unbelievebla ;)
								}
								return 0;
							}
						});

						for (ChatMessage cm : allcm) {
							if (stop)
								return;
							try {
								// NOTE: server side will keep a track of duplicates
								Aggregator.handleMessage(cm);
								if (counter++ % 5 == 0) {
									Thread.sleep(1000);
									System.out.println("handled " + counter + " messages");
								}
							} catch (Exception e) {

							}
						}
					} catch (SkypeException e) {

						e.printStackTrace();
					}
					System.out.println("done.");

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
	public boolean isMenuEnabledOnStartup() {
		return false;
	}

	@Override
	public boolean isSwitchable() {
		return false;
	}
}
