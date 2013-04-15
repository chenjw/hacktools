package weibo4j.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class WeiboConfig {
	private static ThreadLocal<ClientInfo> clientInfo = new ThreadLocal<ClientInfo>();

	public static void setClientInfo(ClientInfo clientInfo) {
		WeiboConfig.clientInfo.set(clientInfo);
	}

	public WeiboConfig() {
	}

	private static Properties props = new Properties();

	static {
		try {
			props.load(Thread.currentThread().getContextClassLoader()
					.getResourceAsStream("weibo_dev.properties"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void setProductMode() {
		try {
			props.load(Thread.currentThread().getContextClassLoader()
					.getResourceAsStream("weibo_product.properties"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String getValue(String key) {
		ClientInfo clientInfo = WeiboConfig.clientInfo.get();
		if (clientInfo != null) {
			if ("client_ID".equals(key)) {
				return clientInfo.getClientId();
			} else if ("client_SECRET".equals(key)) {
				return clientInfo.getClientSecret();
			}
		}
		return props.getProperty(key);
	}

	public static void updateProperties(String key, String value) {
		props.setProperty(key, value);
	}
}
