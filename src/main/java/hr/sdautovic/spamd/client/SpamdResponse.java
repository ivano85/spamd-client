package hr.sdautovic.spamd.client;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SpamdResponse {
	private static final String SPLIT_REGEX = "\r\n\r\n";
	private static final String SPAMD_REGEX = "SPAMD/(.*)\\s\\d\\s(PONG|EX_OK)";
	private static final String SPAM_HEADER_REGEX = "(True|False).*;(.*)/(.*)";
	
	private enum HEADERS { 
		ContentLength {
			@Override
			public String toString() { return "Content-length"; }
		}, 
		
		Spam 
	};
	public static Boolean OK = false;
	
	private String m_spamdResponse = "";
	private String m_spamdProtocolVersion = "";
	private Integer m_spamdResponseLength = 0;
	
	private Boolean m_isSpam = false;
	private Double m_spamScore = 0.0;
	private Double m_spamThreshold = 0.0;
	
	public SpamdResponse(String response) {
		String s[] = response.split(SPLIT_REGEX, 2);
		String headers = "";
		this.m_spamdResponse = "";
		
		if (s.length == 2) {
			headers = s[0];
			this.m_spamdResponse = s[1];
		} else {
			headers = s[0];
		}
		
		processHeaders(headers);
	}
	
	private void processHeaders(String headers) {
		String[] header = headers.split("\n");
		
		if (header.length > 0) {
			Pattern pattern = Pattern.compile(SPAMD_REGEX);
	        Matcher matcher = pattern.matcher(header[0]);
	        
	        if (matcher.find() && matcher.groupCount() == 2) {
	        	this.m_spamdProtocolVersion = matcher.group(1).trim();
	        	if (matcher.group(2).matches("PONG|EX_OK")) OK = true;
	        } else {
	        	OK = false;
	        }
	        
	        for (int i=1; i < header.length; i++) {
	        	String s[] = header[i].split(":\\s", 2);
	        	
	        	if (s.length == 2 && s[0].trim().equals(HEADERS.ContentLength.toString())) {
	        		this.m_spamdResponseLength = Integer.valueOf(s[1].trim());
	        	}
	        	
	        	if (s.length == 2 && s[0].trim().equals(HEADERS.Spam.toString())) {
	        		pattern = Pattern.compile(SPAM_HEADER_REGEX);
	    	        matcher = pattern.matcher(s[1].trim());
	    	        
	    	        if (matcher.find() && matcher.groupCount() == 3) {
	    	        	if (matcher.group(1).matches("True")) this.m_isSpam = true;
	    	        	this.m_spamScore = Double.valueOf(matcher.group(2).trim());
	    	        	this.m_spamThreshold = Double.valueOf(matcher.group(3).trim());
	    	        } else {
	    	        	OK = false;
	    	        }
	        	}
	        }
		}
	}
	
	public String spamdProtocolVersion() {
		return this.m_spamdProtocolVersion;
	}
	
	public Integer length() {
		return this.m_spamdResponseLength;
	}
	
	public String spamdResponse() {
		return this.m_spamdResponse;
	}
	
	public Boolean isSpam() {
		return this.m_isSpam;
	}
	
	public Double score() {
		return this.m_spamScore;
	}
	
	public Double threshold() {
		return this.m_spamThreshold;
	}
}
