package com.chenjw.spider.dt.env;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.commons.io.IOUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.chenjw.spider.dt.constants.EnvConstants;

public class CloudfoundryProvider implements EnvProvider {

	private static Properties CLOUDFOUNDRY_PROPERTIES;
	private static Properties PROPERTIES;
	private static final String VCAP_SERVICES_KEY = "VCAP_SERVICES";
	private static final String VMC_APP_INSTANCE_KEY = "VMC_APP_INSTANCE";
	private static int instanceIndex;

	private static void init() {
		if (PROPERTIES != null) {
			return;
		}
		try {
			PROPERTIES = new Properties();
			PROPERTIES.load(LocalProvider.class.getClassLoader()
					.getResourceAsStream("env/cloudfoundry/db.properties"));
		} catch (IOException e) {
		}
		for (Map.Entry<Object, Object> entry : PROPERTIES.entrySet()) {
			if (entry.getValue() != null) {
				Object v = CLOUDFOUNDRY_PROPERTIES.get(entry.getValue()
						.toString());
				if (v != null) {
					entry.setValue(v);
					// PROPERTIES.put(entry.getKey(), v);
				}
			}

		}
	}

	@SuppressWarnings("unchecked")
	private static void initCloudfoundry() {
		if (CLOUDFOUNDRY_PROPERTIES != null) {
			return;
		}
		CLOUDFOUNDRY_PROPERTIES = new Properties();
		String services = System.getenv(VCAP_SERVICES_KEY);
		if (services == null) {
			try {
				services = IOUtils.toString(EnvConstants.class.getClassLoader()
						.getResourceAsStream(
								"env/cloudfoundry/VCAP_SERVICES_REMOTE"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		Map<String, Object> map = JSON.parseObject(services, Map.class);
		String key = "";
		for (Entry<String, Object> e : map.entrySet()) {
			key = e.getKey();
			JSONArray array = (JSONArray) e.getValue();
			for (Object obj : array) {

				Map<String, Object> map1 = (Map<String, Object>) JSON
						.toJavaObject((JSONObject) obj, Map.class);
				String name = (String) map1.get("name");
				String key1 = key + "_" + name;
				Map<String, Object> map2 = JSON.toJavaObject(
						(JSONObject) map1.get("credentials"), Map.class);
				for (Entry<String, Object> entry : map2.entrySet()) {
					String key2 = key1 + "_" + entry.getKey();
					CLOUDFOUNDRY_PROPERTIES.put(key2, entry.getValue()
							.toString());
				}
			}
		}
		//

		String appInstanceInfo = System.getenv(VMC_APP_INSTANCE_KEY);
		Map<String, Object> appInstanceInfoMap = JSON.parseObject(
				appInstanceInfo, Map.class);
		instanceIndex = (Integer) appInstanceInfoMap.get("instance_index");
	}

	public Properties getProperties() {
		initCloudfoundry();
		init();
		return PROPERTIES;
	}

	public boolean isEnable() {
		String services = System.getenv(VCAP_SERVICES_KEY);
		if (services != null) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public String getName() {
		return "cloudfoundry";
	}

	@Override
	public int getInstanceCount() {
		return 2;
	}

	@Override
	public int getInstanceIndex() {
		return instanceIndex;
	}
}
