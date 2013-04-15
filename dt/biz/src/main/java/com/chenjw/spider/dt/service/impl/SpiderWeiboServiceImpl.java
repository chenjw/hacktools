package com.chenjw.spider.dt.service.impl;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.chenjw.client.HttpClient;
import com.chenjw.client.exception.HttpClientException;
import com.chenjw.parser.utils.TemplateUtils;
import com.chenjw.spider.dt.model.TweetModel;
import com.chenjw.spider.dt.parser.UserTimelineParser;
import com.chenjw.spider.location.HttpUrl;
import com.chenjw.spider.location.UrlParseUtils;

public class SpiderWeiboServiceImpl extends OpenWeiboServiceImpl {
	private HttpClient httpClient;

	// 时间轴翻页
	String url1 = "http://weibo.com/aj/mblog/fsearch?_wv=5&page=1&count=15&pre_page=2&_k=135796535397699&_t=0&max_msign=-1&__rnd=1357965509659";

	// 个人时间轴
	String url2 = "http://e.weibo.com/aj/mblog/mbloglist?page=1&count=50&pre_page=2&_k=135796474315975&uid=${uid}&_t=0&__rnd=1357965039997";
	// 时间轴更新
	String url3 = "http://weibo.com/aj/mblog/fsearch?_wv=5&since_id=3533633015206343&_k=135796715987798&_t=0&__rnd=1357967484394";
	String cookie = "UOR=weibo.com,weibo.com,; ULV=1357916844382:5:5:5:9273337546941.754.1357916844067:1357911239690; un=cjw1983@gmail.com; myuid=1925238912; ALF=1358337854; wvr=5; SinaRot/u/1925238912%3Ftopnav%3D1%26wvr%3D5=38; SinaRot/u/1925238912=35; SINAGLOBAL=9273337546941.754.1357916844067; SinaRot/u/1925238912%3Fwvr%3D5%26lf%3Dreg=18; Apache=9273337546941.754.1357916844067; _s_tentry=login.sina.com.cn; v=5; SSOLoginState=1357916836; SUS=SID-1925238912-1357916836-XD-xqw3p-02715d906517a1694c3d79d112f703b0; SUP=cv%3D1%26bt%3D1357916836%26et%3D1358003236%26d%3Dc909%26i%3Dd78f%26us%3D1%26vf%3D0%26vt%3D0%26ac%3D0%26uid%3D1925238912%26user%3Dcjw1983%2540gmail.com%26ag%3D4%26name%3Dcjw1983%2540gmail.com%26nick%3Dcjw1983%26fmp%3D%26lcp%3D2011-12-12%252021%253A55%253A59; SUE=es%3Ddff95aba4e047ca07ea26f6e278e6d49%26ev%3Dv1%26es2%3Db4ffb6a85cc34d75357eafc9167c10fe%26rs0%3DUnysl21zDCcw45pFsIGyBN4xOMNHDLe1bedoE6gWf33bO%252FuI03ik5195dqnWWF4zjzI6v4btX8ggIsEFMU54wZYH9bDYs8dYyv6NwcrgFwOp3RSqm%252Frx5Q6%252FkTENQVqedDVIuI7wCf%252BLlWZNlZKgZLKsPNSZzhq3RIuVPJMu0Qk%253D%26rv%3D0; USRHAWB=usrmdins315_51";

	@Override
	public List<TweetModel> findUserTimelineByUserId(String userId,
			String token, long sinceId) {
		try {
			HttpUrl url = UrlParseUtils.parseUrl(url2);
			// url.getQueryParam().put("since_id", String.valueOf(sinceId));
			url.getQueryParam().put("uid", userId);
			String s = TemplateUtils.render(cookie, url.getQueryParam());
			String str = httpClient.get(url, "UTF-8", s);

			Map m = JSON.parseObject(str, Map.class);
			String d = (String) m.get("data");
			// System.out.println(d);
			List<TweetModel> result = UserTimelineParser.parse(d);
			return result;
		} catch (HttpClientException e) {
			e.printStackTrace();
			return null;
		}

	}

	public void setHttpClient(HttpClient httpClient) {
		this.httpClient = httpClient;
	}

}
