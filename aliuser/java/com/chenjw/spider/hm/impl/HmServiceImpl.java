package com.chenjw.spider.hm.impl;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.chenjw.client.exception.HttpClientException;
import com.chenjw.client.impl.SimpleHttpClient;
import com.chenjw.spider.hm.HmService;

public class HmServiceImpl implements HmService {
	private static final String uid = "db5e4740d6584a12a1e469c2f65936fa";
	private static final String url = "http://bpm.alibaba.com/DisplayName/Nick/GetDisplayNameStatus";
	private static final String cookie = "ali_beacon_id=121.0.29.205.1352258084555.1; ali_ab=121.0.29.205.1352258084771.7; cna=1boYCbEmJwYCAXkAHcKwaKYE; alicnweb=touch_tb_at%3D1352875241149%7Clastlogonid%3Dchenjw_tb; ali_apache_id=121.0.29.205.54615658089388.2; __last_loginid__=chenjw_tb; lzstat_uv=4067739822721133515|2938410; JSESSIONID=7Z966G71-TB2ASV9KLQYSJOQDJ6YU1-6QRJV79H-TGNO2; _tmp_hny_0=\"AhAPwLGenDsU2obBvfytDrxpnhD%2FYcjakabrbKOjNcFMNxhbYkJJsc6kcKRnAB%2Fe1MhPnBZNuqxB7eEZkdwwmVom8Ya%2BSZL5qkJ5b2ieUO1gYrsQ5d7LjBvx5jY37Vom8jURvbi5qQlPA%2Bvr71%2F8ZAdR9dDaMntt%2BVwq%2F0POTWyWYq1PwHLvNMC0PYRZ%2FTTDfsbGFq3W4A%2FgWBYO0n1uB%2Fid6e0yG647zxEcRv8QuMHWQSF5GkYhrLABJZWT6pow4HAMEtiCeJ176jaFGLSrLSAm2c325mtEKWg3XiSr%2FLbTVspU57tN2DLTLB%2FnyjfJv8jCkoUnmcpj836EoX%2BPnw%3D%3D\"; ali_apache_sid=121.0.29.205.54615658089388.2|1352259889; track_cookie=n&null; _csrf_token=1352786960452; _tmp_ck_0=Brl%2FbT8zIWNz3tEOj2wNffoWfT9R%2BCmPZvzP5paxkPxEYa9bO%2F0r%2BcN01Zxmuf2svEwjzczxT87v%2Fi719VOFd9Y%2By4%2BZpyEwpaOsqkzGuYA2LADynanUeIatZu29FVWVlngjzrmbKxMLDkVBa1QEEFdM%2B%2BrwJDxauuUakEFZAubvgw86xuk4OqqqYItWqY7f6eyKwwv8crfN%2BhE3IsvnO%2FBqAMDSSYcPGEzYoUwtV%2B%2FPiOSTTkqBn36DdkKdusiPFVNaFCLTcmyNhfkLNY4uRfEMmqsByktIwD1MpHCSgvTj%2BazpWqaB0VGexrXLzMSJR1WmQkmrE5wgGk8rZtF%2FMnux6PfY0LnlulUOCkLorbzzgqRZoAUbJOLKydxeqhAjyhRX8O6eCgLhQZTVZtMmFxH%2FWvlKPH8C6F1yOdKxBR8JnkBxW0x95TCj%2BYWyc09RT5tsDyaaRhQ%3D; __cn_logon_id__=chenjw_tb; __cn_logon__=true; lzstat_ss=3170496914_0_1352827089_2938410; ArkAppAuthen=Authen=gXallRD800xMKc3MXhhb0CZ7jJfgGxVBZY1SNz9JvzBRdKAUMgBI+w0f3U1KM2+u5D5TkgC7z8rKGL9gLppEERi/JKzjRRoOW5YtONrFZU7u9cZtBOVb2hr38vZJX0RaBOJVtqQm63XiWsT5jYgyw6VXLbS8ZW9X/99cVov6PvWb2KbDKvONw6QhRZWdORboPYN1Xf9N0zFEmSIXVqrcnHfA/1/5szFEhrysZjfl8mAxKbt0c+6k6nCqJXKe16l5h+N3Trcy9VTqJAK2GWJ90csxIbELars5p/9tKC6WHscf243X2YvBNiSeeqkoI0i5qTPTGqjY4I8bqK4YflYf3J6E4hR61CvRjyja8IwMAQVUwoKhYAMBPF6G8t934sVQ6OAdu+0o7Hx+FynVi7sUSHPkj6VKoW/2WU7wiiNKMyeKi9htvTib0w86rdNl3JOA12Sdxo99G1XSsBiIlLGGJ0KRKHeVMGNlg+ZJImxHD8va+WuAaTFBVxXSCgHgSeHSLP6d7J3QnrLLiMuA72t7uw==&Scid=ZjMxNDcxZmNlOWQ3LWJmZmEtNTA5NC04ZjFiLTQ0MDliMzg1&Code===AM";
	private SimpleHttpClient httpClient = new SimpleHttpClient();
	{
		httpClient.init();
	}

	public boolean isUsed(String name) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("displayName", name);
		params.put("uid", "db5e4740d6584a12a1e469c2f65936fa");
		try {
			String r = httpClient.post(url, params, "UTF-8", cookie);
			System.out.println(r);
			Map map = JSON.parseObject(r, Map.class);
			return Boolean.parseBoolean(map.get("status").toString());
		} catch (HttpClientException e) {
			e.printStackTrace();
			return false;
		}
	}

	public static void main(String[] args) {
		HmServiceImpl t = new HmServiceImpl();
		boolean u = t.isUsed("俊文");
		System.out.println(u);
	}
}
