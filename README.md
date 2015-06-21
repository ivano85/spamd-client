spamd-client - Simple Spamd protocol client
===========================================


Add to Your's Project
---------------------

If you use maven for dependency management, add following snippet to pom.xml:

```spamd-client
	<dependencies>
		...

		<dependency>
			<groupId>hr.sdautovic.spamd</groupId>
			<artifactId>spamd-client</artifactId>
			<version>1.0</version>
		</dependency>

	</dependencies>
```

Building
--------
To produce spamd-client-1.0.jar you will need apache maven installed. Run:

> mvn clean package

Usage
-----

* Simple example

```java
	  byte[] email = IOUtils.toByteArray(new FileInputStream("src/test/java/hr/sdautovic/spamd/client/email_message.eml"));
		SpamdClient spamd_client = new SpamdClient("127.0.0.1", 783, SpamdClient.ACTION.PROCESS, email);
		
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
```

Author Contact
--------------

For further information please contact
Sasa Dautovic <Sasa.Dautovic@gmail.com>
