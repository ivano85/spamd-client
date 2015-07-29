package hr.sdautovic.spamd.client;

import java.io.IOException;
import java.net.UnknownHostException;

public class PingExample {

	public static void main(String[] args) throws UnknownHostException, IOException {
		SpamdClient spamd_client = new SpamdClient("127.0.0.1", 783, SpamdClient.ACTION.PING, "".getBytes(), false);
		
		if (spamd_client.getResponse().OK) System.out.println("PING OK");
		else System.out.println("PING FAILED");
	}
}
