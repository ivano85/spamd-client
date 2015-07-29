package hr.sdautovic.spamd.client;

import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;

import org.apache.commons.io.IOUtils;

public class SpamdClient {
	
	public enum ACTION { CHECK, SYMBOLS, REPORT, REPORT_IFSPAM, SKIP, PING, PROCESS, TELL_SPAM, TELL_HAM, REMOVE_SPAM, REMOVE_HAM };

	private static String DEFAULT_HOST = "localhost";
	private static int DEFAULT_PORT = 783;
	
	private static String PROTOCOL_VERSION = "1.3";
	private static String REQUEST_HEADERS = "{ACTION} SPAMC/{PROTOCOL_VERSION}".concat("\n").
			concat("Content-length: {SIZE}").concat("\n").concat("User: {USERNAME}").concat("\n").concat("\r\n");
	
	private byte[] m_email = "".getBytes();
	private SpamdResponse m_response = null;
	
	private ACTION m_action = ACTION.PING;
	private String m_protocolVersion = PROTOCOL_VERSION;
	private String m_username = "";
	
	private Socket m_socket = null;
	private boolean m_use_remote_bayesian = false;
	
	/**
	 * @author sdautovic
	 * @param  action  Spamd action 
	 * @param  email   email message to check
	 * @throws ConnectException if any error connecting to Spamd occurs
	 * @throws IOException if any IO error occurs
	 * @throws UnknownHostException if unable to resolve hostname
	 */
	public SpamdClient(ACTION action, byte[] email, boolean use_remote_bayesian) throws ConnectException, UnknownHostException, IOException {	
		this.m_socket = new Socket(DEFAULT_HOST, DEFAULT_PORT);
		
		if (email == null) throw new NullPointerException("Email message must be supplied (can not be NULL)");
		this.m_action = action;
		this.m_use_remote_bayesian = use_remote_bayesian;
		this.m_email = email;
		
		doCheckAndRequest();
		this.m_socket.close();
	}
	
	/**
	 * @author sdautovic
	 * @param  hostname IP address or hostname to connect to Spamassassin daemon
	 * @param  action  Spamd action 
	 * @param  email   email message to check
	 * @throws ConnectException if any error connecting to Spamd occurs
	 * @throws IOException if any IO error occurs
	 * @throws UnknownHostException if unable to resolve hostname
	 */
	public SpamdClient(String hostname, ACTION action, byte[] email, boolean use_remote_bayesian) throws ConnectException, UnknownHostException, IOException {	
		if (hostname == null) throw new NullPointerException("Hostname can't be NULL");
		this.m_socket = new Socket(hostname, DEFAULT_PORT);
		
		if (email == null) throw new NullPointerException("Email message can't be NULL");
		this.m_action = action;
		this.m_use_remote_bayesian = use_remote_bayesian;
		this.m_email = email;
		
		doCheckAndRequest();
		this.m_socket.close();
	}
	
	/**
	 * @author sdautovic
	 * @param  hostname IP address or hostname to connect to Spamassassin daemon
	 * @param  port network port to use when connecting to Spamassassin daemon
	 * @param  action  Spamd action 
	 * @param  email   email message to check
	 * @throws ConnectException if any error connecting to Spamd occurs
	 * @throws IOException if any IO error occurs
	 * @throws UnknownHostException if unable to resolve hostname
	 */
	public SpamdClient(String hostname, int port, ACTION action, byte[] email, boolean use_remote_bayesian) throws ConnectException, UnknownHostException, IOException {	
		if (hostname == null) throw new NullPointerException("Hostname can't be NULL");
		this.m_socket = new Socket(hostname, port);
		
		if (email == null) throw new NullPointerException("Email message can't be NULL");
		this.m_action = action;
		this.m_use_remote_bayesian = use_remote_bayesian;
		this.m_email = email;
		
		doCheckAndRequest();
		this.m_socket.close();
	}
	
	/**
	 * @author sdautovic
	 * @param  action  Spamd action 
	 * @param  email   email message to check
	 * @param  username username which Spamd will check for specific rules
	 * @throws ConnectException if any error connecting to Spamd occurs
	 * @throws IOException if any IO error occurs
	 * @throws UnknownHostException if unable to resolve hostname
	 */
	public SpamdClient(ACTION action, byte[] email, String username, boolean use_remote_bayesian) throws ConnectException, UnknownHostException, IOException {	
		this.m_socket = new Socket(DEFAULT_HOST, DEFAULT_PORT);
		
		if (email == null) throw new NullPointerException("Email message can't be NULL");
		if (username == null) throw new NullPointerException("Username can't be NULL");
		this.m_action = action;
		this.m_username = username;
		this.m_use_remote_bayesian = use_remote_bayesian;
		this.m_email = email;
		
		doCheckAndRequest();
		this.m_socket.close();
	}
	
	/**
	 * @author sdautovic
	 * @param  hostname IP address or hostname to connect to Spamassassin daemon
	 * @param  action  Spamd action 
	 * @param  email   email message to check
	 * @param  username username which Spamd will check for specific rules
	 * @throws ConnectException if any error connecting to Spamd occurs
	 * @throws IOException if any IO error occurs
	 * @throws UnknownHostException if unable to resolve hostname
	 */
	public SpamdClient(String hostname, ACTION action, byte[] email, String username, boolean use_remote_bayesian) throws ConnectException, UnknownHostException, IOException {	
		if (hostname == null) throw new NullPointerException("Hostname can't be NULL");
		this.m_socket = new Socket(hostname, DEFAULT_PORT);
		
		if (email == null) throw new NullPointerException("Email message can't be NULL");
		if (username == null) throw new NullPointerException("Username can't be NULL");
		this.m_action = action;
		this.m_username = username;
		this.m_use_remote_bayesian = use_remote_bayesian;
		this.m_email = email;
		
		doCheckAndRequest();
		this.m_socket.close();
	}
	
	/**
	 * @author sdautovic
	 * @param  hostname IP address or hostname to connect to Spamassassin daemon
	 * @param  port network port to use when connecting to Spamassassin daemon
	 * @param  action  Spamd action 
	 * @param  email   email message to check
	 * @param  username username which Spamd will check for specific rules
	 * @throws ConnectException if any error connecting to Spamd occurs
	 * @throws IOException if any IO error occurs
	 * @throws UnknownHostException if unable to resolve hostname
	 */
	public SpamdClient(String hostname, int port, ACTION action, byte[] email, String username, boolean use_remote_bayesian) throws ConnectException, UnknownHostException, IOException {	
		if (hostname == null) throw new NullPointerException("Hostname can't be NULL");
		this.m_socket = new Socket(hostname, port);
		
		if (email == null) throw new NullPointerException("Email message can't be NULL");
		if (username == null) throw new NullPointerException("Username can't be NULL");
		this.m_action = action;
		this.m_username = username;
		this.m_use_remote_bayesian = use_remote_bayesian;
		this.m_email = email;
		
		doCheckAndRequest();
		this.m_socket.close();
	}
	
	private String setHeaders() {
		String headers = REQUEST_HEADERS;
		String location = "local";
		
		switch (this.m_action) {
			case PING:
					headers = "PING SPAMC/{PROTOCOL_VERSION}";
					headers = headers.replace("{PROTOCOL_VERSION}", PROTOCOL_VERSION);
					return headers;
			
			case TELL_SPAM:
					headers = "TELL SPAMC/{PROTOCOL_VERSION}";
					headers = headers.concat("\n").concat("Message-class: spam").concat("\n").concat("Set: {BAYESIAN_LOCATION}").
							concat("\n").concat("Content-length: {SIZE}").concat("\n").concat("User: {USERNAME}").concat("\n").concat("\r\n");
					
					location = "local";
					if (this.m_use_remote_bayesian) location = "local, remote";
					
					headers = headers.replace("{PROTOCOL_VERSION}", PROTOCOL_VERSION);
					headers = headers.replace("{BAYESIAN_LOCATION}", location);
					headers = headers.replace("{SIZE}", String.valueOf(this.m_email.length + 2));
					headers = headers.replace("{USERNAME}", this.m_username);
					return headers;
					
			case TELL_HAM:
				headers = "TELL SPAMC/{PROTOCOL_VERSION}";
				headers = headers.concat("\n").concat("Message-class: ham").concat("\n").concat("Set: {BAYESIAN_LOCATION}").
						concat("\n").concat("Content-length: {SIZE}").concat("\n").concat("User: {USERNAME}").concat("\n").concat("\r\n");
				
				location = "local";
				if (this.m_use_remote_bayesian) location = "local, remote";
				
				headers = headers.replace("{PROTOCOL_VERSION}", PROTOCOL_VERSION);
				headers = headers.replace("{BAYESIAN_LOCATION}", location);
				headers = headers.replace("{SIZE}", String.valueOf(this.m_email.length + 2));
				headers = headers.replace("{USERNAME}", this.m_username);
				return headers;
			
			case REMOVE_SPAM:
				headers = "TELL SPAMC/{PROTOCOL_VERSION}";
				headers = headers.concat("\n").concat("Message-class: spam").concat("\n").concat("Remove: {BAYESIAN_LOCATION}").
						concat("\n").concat("Content-length: {SIZE}").concat("\n").concat("User: {USERNAME}").concat("\n").concat("\r\n");
				
				location = "local";
				if (this.m_use_remote_bayesian) location = "local, remote";
				
				headers = headers.replace("{PROTOCOL_VERSION}", PROTOCOL_VERSION);
				headers = headers.replace("{BAYESIAN_LOCATION}", location);
				headers = headers.replace("{SIZE}", String.valueOf(this.m_email.length + 2));
				headers = headers.replace("{USERNAME}", this.m_username);
				return headers;
				
			case REMOVE_HAM:
				headers = "TELL SPAMC/{PROTOCOL_VERSION}";
				headers = headers.concat("\n").concat("Message-class: ham").concat("\n").concat("Remove: {BAYESIAN_LOCATION}").
						concat("\n").concat("\n").concat("Content-length: {SIZE}").concat("\n").concat("User: {USERNAME}").concat("\n").concat("\r\n");
				
				location = "local";
				if (this.m_use_remote_bayesian) location = "local, remote";
				
				headers = headers.replace("{PROTOCOL_VERSION}", PROTOCOL_VERSION);
				headers = headers.replace("{BAYESIAN_LOCATION}", location);
				headers = headers.replace("{SIZE}", String.valueOf(this.m_email.length + 2));
				headers = headers.replace("{USERNAME}", this.m_username);
				return headers;
				
			default:
				break;
		}
		
		/** add default headers **/
		headers = headers.replace("{ACTION}", m_action.toString());
		headers = headers.replace("{PROTOCOL_VERSION}", this.m_protocolVersion);
		headers = headers.replace("{SIZE}", String.valueOf(this.m_email.length + 2));
		headers = headers.replace("{USERNAME}", this.m_username);
		
		return headers; 
	}
	
	/**
	 * Gets response received from Spamd
	 * 
	 * @author sdautovic
	 * @return SpamdResponse
	 * @see SpamdResponse
	 */
	public SpamdResponse getResponse() {
		return this.m_response;
	}
	
	private void doCheckAndRequest() {
		try {			
			String headers = setHeaders();
			
			this.m_socket.getOutputStream().write(headers.getBytes());
			this.m_socket.getOutputStream().write(this.m_email);
			this.m_socket.getOutputStream().write("\r\n".getBytes());
			this.m_socket.getOutputStream().flush();
			
			String response = IOUtils.toString(this.m_socket.getInputStream());
			this.m_response = new SpamdResponse(response);
			
		} catch (IOException e) {
			e.printStackTrace();
			this.m_response = new SpamdResponse("");
		} 
	}
}