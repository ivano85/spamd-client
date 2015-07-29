package hr.sdautovic.spamd.client;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.commons.io.IOUtils;

public class TellExample {

	public static void main(String[] args) throws FileNotFoundException, IOException {
		byte[] email = IOUtils.toByteArray(new FileInputStream("src/test/java/hr/sdautovic/spamd/client/email_message.eml"));
		SpamdClient spamd_client = new SpamdClient("127.0.0.1", 783, SpamdClient.ACTION.TELL_HAM, email, false);
		
		if (spamd_client.getResponse().OK) {
			System.out.println("spamd_client OK=" + spamd_client.getResponse().OK);
			System.out.println("spamd_client length=" + spamd_client.getResponse().length());
			System.out.println("spamd_client spamd protocol version=" + spamd_client.getResponse().spamdProtocolVersion());
			
			System.out.println("");
			System.out.println("spamd isSpam=" + spamd_client.getResponse().isSpam());
			System.out.println("spamd score=" + spamd_client.getResponse().score());
			System.out.println("spamd threshold=" + spamd_client.getResponse().threshold());
			
			System.out.println("spamd_client spamd response\n" + spamd_client.getResponse().spamdResponse());
		}
	}
}
