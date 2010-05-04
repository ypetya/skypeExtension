package app.plugins;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.skype.ChatMessage;
import com.skype.SkypeException;

public class NewlAggregator implements Plugin {

	private static final String COMMAND_NEWL_AGGREGATOR = "Newl aggregator";

	private String[] urlBlacklist = { "local", "http://[0-9.]+[/:]", "virgo",
			"ypetya", "admin", "sandbox", "szarka", "netpincer", "blackbox",
			"svn", "authkey=\\w+&", "iwiw", "zoldseg", "gtk", "eleventyone",
			"zanz.*itori", "^http[s]*://\\s*$" };

	public String getTrayCommandName() {
		return COMMAND_NEWL_AGGREGATOR;
	}

	public void init() {
		// TODO Auto-generated method stub
	}

	public void dispose() {
		// TODO Auto-generated method stub
	}

	public void click() {
		// TODO Auto-generated method stub

	}

	public void enable() {
		// TODO Auto-generated method stub
	}

	public void disable() {
		// TODO Auto-generated method stub
	}

	public boolean isMenuEnabledOnStartup() {
		return false;
	}

	public boolean isSwitchable() {
		return true;
	}

	public void chatMessageReceived(ChatMessage received) throws SkypeException {
		// Recognize Url
		String msg = received.getContent();

		Pattern pattern = Pattern.compile("http://\\S*");

		Matcher matcher = pattern.matcher(msg);

		for (int i = 0; matcher.find(i); i = matcher.end()) {
			storeUrl(msg.substring(matcher.start(), matcher.end()), received
					.getSenderDisplayName());
		}

	}

	// post url if it is not blacklisted
	private void storeUrl(String url, String userName) {
		// Blacklist check
		for (String black : this.urlBlacklist) {
			Pattern pattern = Pattern.compile(black);
			Matcher matcher = pattern.matcher(url);
			if (matcher.find())
				return;
		}
		
		// Create a simple HTTP Post
		OutputStreamWriter wr = null;
		BufferedReader rd = null;

		try {
			StringBuilder sb = new StringBuilder("magick=");
			sb.append(URLEncoder.encode(userName, "UTF-8"));
			sb.append("&");
			sb.append("text=");
			sb.append(URLEncoder.encode(url, "UTF-8"));
			sb.append("&");
			sb.append("channel=8");

			// Create a simple HTTP Post
			URL backend = new URL("http://91.120.21.19/update");
			URLConnection conn = backend.openConnection();
			conn.setDoOutput(true);
			wr = new OutputStreamWriter(conn.getOutputStream());
			wr.write(sb.toString());
			wr.flush();
			// Now copy bytes from the URL to the output stream
			String line;
			rd = new BufferedReader(
					new InputStreamReader(conn.getInputStream()));
			while ((line = rd.readLine()) != null)
				;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

			try {
				wr.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			try {
				rd.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void chatMessageSent(ChatMessage sent) throws SkypeException {
		// TODO Auto-generated method stub

	}

}
