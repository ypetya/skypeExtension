package app.plugins;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;

import com.skype.ChatMessage;
import com.skype.SkypeException;
import com.skype.User;

public class ChatHistory implements Plugin {

	private final SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
	boolean enabled;
	
	@Override
	public void click() {
	}

	@Override
	public void disable() {
		enabled = false;
	}

	@Override
	public void dispose() {
		enabled = false;
	}

	@Override
	public void enable() {
		enabled = true;		
	}

	@Override
	public String getTrayCommandName() {
		return "history";
	}

	@Override
	public boolean init() {
		enabled = true;
		return true;
	}

	@Override
	public boolean isMenuEnabledOnStartup() {
		return true;
	}

	@Override
	public boolean isSwitchable() {
		return true;
	}
	
	private String dateForLog(Date dt) {
		return dateFormatter.format(dt);
	}
	
	private void expandUserInfo(StringBuilder sb, User user) throws SkypeException {
		
		sb.append("display name : ");
		sb.append(user.getDisplayName());
		sb.append("\n");
		
		sb.append("full name : ");
		sb.append(user.getFullName());
		sb.append("\n");
		
		sb.append("mood message : ");
		sb.append(user.getMoodMessage());
		sb.append("\n");
		
		sb.append("birthday : ");
		sb.append(user.getBirthDay());
		sb.append("\n");
		
		sb.append("about : ");
		sb.append(user.getAbout());
		sb.append("\n");
		
		sb.append("sex : ");
		sb.append(user.getSex());
		sb.append("\n");
		
		sb.append("language : ");
		sb.append(user.getLauguage());
		sb.append("\n");
		
		sb.append("country : ");
		sb.append(user.getCountry());
		sb.append("\n");
		
		sb.append("province : ");
		sb.append(user.getProvince());
		sb.append("\n");
		
		sb.append("city : ");
		sb.append(user.getCity());
		sb.append("\n");
		
		sb.append("home page : ");
		sb.append(user.getHomePageAddress());
		sb.append("\n");
		
		sb.append("home phone : ");
		sb.append(user.getHomePhone());
		sb.append("\n");

		sb.append("mobile phone : ");
		sb.append(user.getMobilePhone());
		sb.append("\n");
		
		sb.append("office phone : ");
		sb.append(user.getOfficePhone());
		sb.append("\n");
	}
	
	private void logMessageToFile(File f, ChatMessage m) {
		if(f.exists() && f.canWrite()) {
			try {
				PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(f)));
				
				try {
					StringBuilder sb = new StringBuilder();
					sb.append(dateForLog(m.getTime()));
					sb.append(" ");
					sb.append(m.getSenderDisplayName());
					sb.append(" :\t");
					sb.append(m.getContent());
					
					out.println( sb.toString());
				}
				finally {
					out.close();
				}
						
			} catch (SkypeException e) {
			} catch (IOException e) {
			}
		} else if(!f.exists()) {
			// creating file and info about the user
			try {
				PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(f)));
				
				try {
					StringBuilder sb = new StringBuilder();
					sb.append("created : ");
					sb.append(dateForLog(m.getTime()));
					sb.append("\n");
					User[] users = m.getAllUsers();
					for(User u: users) {
						expandUserInfo(sb, u);
					}
					
					sb.append("\n==\n\n");
					
					out.println( sb.toString());
				}
				finally {
					out.close();
				}
						
			} catch (SkypeException e) {
			} catch (IOException e) {
			}		
		}	
	}
	
	private String generateFileName(ChatMessage m) {
		StringBuilder sb = new StringBuilder();
		try {
			User[] users = m.getAllUsers();
			Iterator<User> it = Arrays.asList(users).iterator();
			while(it.hasNext()) {
				User u = it.next();
				sb.append(u.getId());
				if(it.hasNext()) {
					sb.append("_");
				}
			}
			
		} catch (SkypeException e) {
			return "Exception.txt";
		}
		sb.append(".txt");
		return sb.toString();
	}

	@Override
	public void chatMessageReceived(ChatMessage receivedChatMessage) throws SkypeException {
		File f = new File( generateFileName(receivedChatMessage));
		
		logMessageToFile(f, receivedChatMessage);
	}

	@Override
	public void chatMessageSent(ChatMessage sentChatMessage) throws SkypeException {
		File f = new File( generateFileName( sentChatMessage ));
		
		logMessageToFile(f, sentChatMessage);		
	}

}
