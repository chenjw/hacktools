package com.chenjw.spider.dt.parser;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.htmlparser.Tag;
import org.htmlparser.visitors.NodeVisitor;

import com.chenjw.spider.dt.model.ReasonModel;
import com.chenjw.spider.dt.model.TweetModel;
import com.chenjw.spider.dt.model.UserModel;
import com.chenjw.spider.location.UrlParseUtils;

public class WapUserTimelineNodeVisitor extends NodeVisitor {

	private List<TweetModel> tweets = new ArrayList<TweetModel>();
	private TweetModel tweet;
	private ReasonModel reason;

	public List<TweetModel> getTweets() {
		return tweets;
	}

	@Override
	public void visitTag(Tag tag) {
		if ("feed_list_item".equals(tag.getAttribute("action-type"))
				&& StringUtils.isBlank(tag.getAttribute("act_id"))) {
			if (tweet != null) {
				tweets.add(tweet);
			}
			tweet = new TweetModel();
			tweet.setHtml(tag.toHtml());
			tweet.setUser(new UserModel());
			reason = null;
			tweet.setId(tag.getAttribute("mid"));
		} else if ("feed_list_content".equals(tag.getAttribute("node-type"))) {
			tweet.setText(tag.toHtml());
		} else if ("feed_list_forwardContent".equals(tag
				.getAttribute("node-type"))) {
			reason = new ReasonModel();
			reason.setUser(new UserModel());
			tweet.setReason(reason);
		} else if ("feed_list_originNick".equals(tag.getAttribute("node-type"))) {
			reason.getUser().setId(
					StringUtils.substringAfter(tag.getAttribute("usercard"),
							"id="));
			reason.getUser().setScreenName(tag.getAttribute("nick-name"));
		} else if ("feed_list_reason".equals(tag.getAttribute("node-type"))) {
			reason.setText(tag.toHtml());
		} else if ("feed_list_media_bgimg"
				.equals(tag.getAttribute("node-type"))) {
			if (reason == null) {
				tweet.setThumbnailPic(tag.getAttribute("src"));
			} else {
				reason.setThumbnailPic(tag.getAttribute("src"));
			}

		} else if ("feed_list_item_date".equals(tag.getAttribute("node-type"))) {
			if ("S_link2 WB_time".equals(tag.getAttribute("class"))) {
				tweet.setCreatedAt(new Date(Long.parseLong(tag
						.getAttribute("date"))));
			} else if ("S_func2 WB_time".equals(tag.getAttribute("class"))) {
				reason.setCreatedAt(new Date(Long.parseLong(tag
						.getAttribute("date"))));
			}
		}

		else if ("WB_handle".equals(tag.getAttribute("class"))) {
			if (reason != null && tag.getAttribute("mid") != null) {
				reason.setId(tag.getAttribute("mid"));
			}
		} else if ("feed_list_forward".equals(tag.getAttribute("action-type"))) {
			String s = tag.getAttribute("action-data");
			Map<String, String> data = UrlParseUtils.parseUrl(s)
					.getQueryParam();
			tweet.getUser().setScreenName(data.get("name"));
			tweet.getUser().setId(data.get("uid"));
		}

	}

	@Override
	public void beginParsing() {
		tweets = new ArrayList<TweetModel>();
		tweet = null;
		reason = null;
	}

	@Override
	public void finishedParsing() {
		if (tweet != null) {
			tweets.add(tweet);
		}
	}

	public static void main(String[] args) {
		// t.train(3530829567353879L, "zcW16uR4X");
		// t.train(3530687992576071L, "zcSkLaO9x");
		// t.train(3530485105448989L, "zcN3wmRwV");
		// t.train(3529449054118693L, "zcm6thhsx");
		// t.train(3529041673874812L, "zcbvpgg0Y");
		// t.train(3527929143007098L, "zbIz0cChA");
		System.out.println(EncodeTester.s("zcW16uR4X"));
		System.out.println(EncodeTester.s("zcSkLaO9x"));
		System.out.println(EncodeTester.s("zcN3wmRwV"));

		System.out.println(EncodeTester.s("zcm6thhsx"));
		System.out.println(EncodeTester.s("zcbvpgg0Y"));
		System.out.println(EncodeTester.s("zbIz0cChA"));

	}

}
