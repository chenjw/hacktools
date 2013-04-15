package com.chenjw.spider.mail.service.impl;

import org.springframework.beans.factory.InitializingBean;

import com.chenjw.client.HttpClient;
import com.chenjw.client.exception.HttpClientException;
import com.chenjw.spider.dt.constants.EnvConstants;
import com.chenjw.spider.location.UrlParseUtils;
import com.chenjw.spider.mail.MailSenderInfo;
import com.chenjw.spider.mail.SimpleMailSender;
import com.chenjw.spider.mail.service.IsSheBrokeUpService;

public class IsSheBrokeUpServiceImpl implements IsSheBrokeUpService,
		InitializingBean {
	private HttpClient httpClient;

	private static final String cookie = "_s_tentry=www.chinaz.com; USRUG=usrmdins41455; __utma=15428400.1689549717.1361177996.1361177996.1361177996.1; __utmc=15428400; __utmz=15428400.1361177996.1.1.utmcsr=blog.sina.com.cn|utmccn=(referral)|utmcmd=referral|utmcct=/s/blog_4ac9146601014x82.html; Apache=381955145858.2282.1361179535394; SINAGLOBAL=381955145858.2282.1361179535394; ULV=1361179535401:1:1:1:381955145858.2282.1361179535394:; SinaRot/u/1925238912%3Fleftnav%3D1%26wvr%3D5=60; WBStore=84b1a0f0478cbbde|; SinaRot/u/1925238912%3Fwvr%3D5%26page%3D2%26pre_page%3D1%26end_id%3D3547481843070164%26end_msign%3D-1%231361269874389=78; SinaRot/u/1925238912%3Fwvr%3D5%26=64; v5reg=usrmdins1027; SinaRot/u/3221292113%3Fwvr%3D5%26lf%3Dreg%26uut%3Dfin=76; SinaRot/u/3221292113%3Fwvr%3D5%26topnav%3D1%26wvr%3D5=27; SinaRot/u/3221292113%3Fwvr%3D5%26=71; USRHAWB=usrmdins312_134; SinaRot/u/1925238912%3Ftopnav%3D1%26wvr%3D5=65; SinaRot/u/3221292113%3Fwvr%3D5%26wvr%3D5%26lf%3Dreg=45; SinaRot/u/3221292113%3Ftopnav%3D1%26wvr%3D5=45; myuid=3221292113; UOR=www.chinaz.com,widget.weibo.com,#login.sina.com.cn; SUE=es%3Dfd7e74a192575650399b0a8b94b6bf2c%26ev%3Dv1%26es2%3D7e83f8ab6f1db21cef19c0fa5806503e%26rs0%3DQ%252Fgpn1NYkbkhAvNfImxgz9v4LNo3zs4mIos7K8RTqS%252B2TCRcLApI1spwUMKAGSm3CSC5wBBiibUMzNis%252BiHV2OHLjLKhDIaNuhEKuOheYasPYCNAWaV0hxyNMxm%252FdvoP5I4utJxM7toyzUPDpQXRC%252Fxjx%252FVZcXa93e7OmBkp%252Fis%253D%26rv%3D0; SUP=cv%3D1%26bt%3D1361274466%26et%3D1361360866%26d%3Dc909%26i%3D1c53%26us%3D1%26vf%3D0%26vt%3D0%26ac%3D0%26uid%3D1925238912%26user%3Dcjw1983%2540gmail.com%26ag%3D4%26name%3Dcjw1983%2540gmail.com%26nick%3Dcjw1983%26fmp%3D%26lcp%3D2013-02-07%252012%253A10%253A09; SUS=SID-1925238912-1361274466-JA-bkyd9-11c39420e61ed87ec482b89efecba423; ALF=1361879266; SSOLoginState=1361274466; v=5; un=cjw1983@gmail.com; wvr=5; SinaRot/u/1925238912%3Fwvr%3D5%26wvr%3D5%26lf%3Dreg=33";

	@Override
	public void check() {
		if (isDeleted()) {
			sendMailBroke();
		} else {
			sendMailNotBroke();
		}

	}

	private boolean isDeleted() {
		String result = null;
		try {
			result = httpClient.get(UrlParseUtils
					.parseUrl("http://weibo.com/2646772100/zhZ1FdYYG"),
					"UTF-8", cookie);
		} catch (HttpClientException e) {
		}
		if (result == null) {
			return false;
		}
		if (result.indexOf("这问题到底是该跟脑走，还是跟着心情走？") != -1) {
			return false;
		}
		return true;
	}

	public void sendMailNotBroke() {
		MailSenderInfo mailInfo = new MailSenderInfo();
		mailInfo.setSsl(true);
		mailInfo.setMailServerHost("smtp.gmail.com");
		mailInfo.setMailServerPort("465");
		mailInfo.setValidate(true);
		mailInfo.setUserName("dttg2013@gmail.com");
		mailInfo.setPassword("19830926");// 您的邮箱密码
		mailInfo.setFromAddress("dttg2013@gmail.com");
		mailInfo.setToAddress("cjw1983@gmail.com");
		mailInfo.setSubject("not broke up");
		mailInfo.setContent("att.");

		// 这个类主要来发送邮件
		SimpleMailSender.sendTextMail(mailInfo);// 发送文体格式
	}

	public void sendMailBroke() {
		MailSenderInfo mailInfo = new MailSenderInfo();
		mailInfo.setSsl(true);
		mailInfo.setMailServerHost("smtp.gmail.com");
		mailInfo.setMailServerPort("465");
		mailInfo.setValidate(true);
		mailInfo.setUserName("dttg2013@gmail.com");
		mailInfo.setPassword("19830926");// 您的邮箱密码
		mailInfo.setFromAddress("dttg2013@gmail.com");
		mailInfo.setToAddress("cjw1983@gmail.com");
		mailInfo.setSubject("she broke up!!!!!!!!!!!!!!!!!!!");
		mailInfo.setContent("http://weibo.com/2646772100/zhZ1FdYYG");
		// 这个类主要来发送邮件
		SimpleMailSender.sendTextMail(mailInfo);// 发送文体格式
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		if (!EnvConstants.isProductMode()) {
			return;
		}
		new Thread(new Runnable() {

			@Override
			public void run() {
				while (true) {
					try {
						check();
						Thread.sleep(24 * 60 * 60 * 1000);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

			}

		}).start();

	}

	public void setHttpClient(HttpClient httpClient) {
		this.httpClient = httpClient;
	}

}
