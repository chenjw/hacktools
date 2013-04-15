

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import weibo4j.model.WeiboException;

import com.chenjw.spider.dt.service.WeiboService;
import com.chenjw.spider.dt.service.impl.OpenWeiboServiceImpl;

public class OAuthUtils {

	public static void main(String[] args) throws WeiboException, IOException {
		WeiboService weiboService = new OpenWeiboServiceImpl();
		// BareBonesBrowserLaunch.openURL(getAuthorizeUrl());
		System.out.println(weiboService.findAuthorizeUrl());
		System.out.print("Hit enter when it's done.[Enter]:");
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String code = br.readLine();
		//System.out.println(weiboService.findAccessTokenByCode(code));

	}

}