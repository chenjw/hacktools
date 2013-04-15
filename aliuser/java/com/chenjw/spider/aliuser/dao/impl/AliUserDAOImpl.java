package com.chenjw.spider.aliuser.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import redis.clients.jedis.Jedis;

import com.chenjw.spider.aliuser.dao.AliUserDAO;
import com.chenjw.spider.aliuser.model.AliUserInfo;
import com.chenjw.spider.aliuser.util.NotBlankHashMap;
import com.chenjw.tools.BeanCopyUtils;

public class AliUserDAOImpl implements AliUserDAO {
	private Jedis jedis = new Jedis("localhost");
	private static final String KEY_USER_INFO = "user_info";
	private static final String MAX_WORK_BUM = "MAX_WORK_BUM";

	@Override
	public AliUserInfo getLastUserInfo() {
		return null;
	}

	@Override
	public void save(AliUserInfo userInfo) {
		int workNum = userInfo.getWorkNum();
		if (workNum != 0) {
			Map<String, String> map = new NotBlankHashMap<String, String>();
			BeanCopyUtils.copyProperties(map, userInfo, String.class);
			jedis.hmset(String.valueOf(workNum), map);
			String maxWorkNum = jedis.get(MAX_WORK_BUM);
			if (StringUtils.isBlank(maxWorkNum)
					|| workNum > Integer.parseInt(maxWorkNum)) {
				jedis.set(MAX_WORK_BUM, String.valueOf(workNum));
			}
			printUser1(userInfo);
		}
	}

	public List<AliUserInfo> getAll() {
		List<AliUserInfo> r = new ArrayList<AliUserInfo>();
		String maxWorkNumStr = jedis.get(MAX_WORK_BUM);
		if (!StringUtils.isBlank(maxWorkNumStr)) {
			int maxWorkNum = Integer.parseInt(maxWorkNumStr);
			for (int i = 1; i <= maxWorkNum; i++) {
				Map<String, String> map = jedis.hgetAll(String.valueOf(i));
				if (map != null && map.size() > 0) {
					AliUserInfo userInfo = new AliUserInfo();
					BeanCopyUtils.copyProperties(userInfo, map);
					r.add(userInfo);
				}

			}
		}
		return r;
	}

	private static void printUser1(AliUserInfo user) {
		System.out.println(ReflectionToStringBuilder.toString(user,
				ToStringStyle.MULTI_LINE_STYLE));
	}

	private static void printUser(AliUserInfo user) {
		System.out.println("aliwayId=" + user.getAliwayId() + "&name="
				+ user.getName() + "&email=" + user.getEmail());
	}

	private void saveUser(AliUserInfo user) {
		int num = user.getWorkNum();
		if (num != 0) {
			Map<String, String> map = new NotBlankHashMap<String, String>();
			BeanCopyUtils.copyProperties(map, user, String.class);
			for (Entry<String, String> entry : map.entrySet()) {
				if (entry.getValue() != null) {
					jedis.hset(String.valueOf(num), entry.getKey(),
							entry.getValue());
				}

			}
		}

	}

	public static void main(String[] args) {
		AliUserDAOImpl dao = new AliUserDAOImpl();
		AliUserInfo u = new AliUserInfo();
		u.setName("aaa");
		u.setWorkNum(2);
		dao.save(u);
		u.setName("bbb");
		u.setWorkNum(3);
		dao.save(u);
		for (AliUserInfo uu : dao.getAll()) {
			printUser1(uu);
		}

	}

}
