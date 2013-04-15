package com.chenjw.spider.dt.parser;

import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Tag;
import org.htmlparser.tags.Div;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.visitors.NodeVisitor;

import com.chenjw.spider.dt.model.ReasonModel;
import com.chenjw.spider.dt.model.TweetModel;
import com.chenjw.spider.dt.model.UserModel;

public class WeiboComTweetDetailNodeVisitor extends NodeVisitor {

	private TweetModel tweet;

	public WeiboComTweetDetailNodeVisitor(TweetModel tweet) {
		this.tweet = tweet;
	}

	private String filterText(String text) {
		return text;
	}

	@SuppressWarnings("serial")
	@Override
	public void visitTag(Tag tag) {
		if ("weibo_info".equals(tag.getAttribute("node-type"))) {
			tweet.setId(tag.getAttribute("mid"));
		} else if ("feed_list_content".equals(tag.getAttribute("node-type"))) {
			tweet.setText(filterText(tag.toPlainTextString()));
		} else if ("feed_list_forwardContent".equals(tag
				.getAttribute("node-type"))) {
			final ReasonModel reason = new ReasonModel();
			reason.setUser(new UserModel());

			tag.collectInto(new NodeList(), new NodeFilter() {

				@Override
				public boolean accept(Node node) {
					if (node instanceof Div) {
						Div div = (Div) node;
						if ("WB_text".equals(div.getAttribute("class"))) {
							reason.setText(filterText(div.toPlainTextString()));
						}
					} else if (node instanceof LinkTag) {
						LinkTag link = (LinkTag) node;
						if ("WB_name S_func3".equals(link.getAttribute("class"))) {
							reason.getUser().setScreenName(
									link.getAttribute("nick-name"));
							reason.getUser().setId(
									StringUtils.substringAfter(
											link.getAttribute("usercard"),
											"id="));

						}
					}
					return false;
				}

			});
			tweet.setReason(reason);
		} else if ("WB_handle".equals(tag.getAttribute("class"))) {
			if (tag.getAttribute("mid") != null) {
				tweet.getReason().setId(tag.getAttribute("mid"));
			} else {
				tag.collectInto(new NodeList(), new NodeFilter() {
					public boolean accept(Node node) {
						if (node instanceof LinkTag) {
							LinkTag link = (LinkTag) node;
							if ("forward_counter".equals(link
									.getAttribute("node-type"))
									&& StringUtils.startsWith(
											link.toPlainTextString(), "转发(")) {
								tweet.setRepostsCount(Integer
										.parseInt(StringUtils.substringBetween(
												link.toPlainTextString(),
												"转发(", ")")));

							} else if ("comment_counter".equals(link
									.getAttribute("node-type"))
									&& StringUtils.startsWith(
											link.toPlainTextString(), "评论(")) {
								tweet.setCommentsCount(Integer
										.parseInt(StringUtils.substringBetween(
												link.toPlainTextString(),
												"评论(", ")")));
							}
						}
						return false;
					}
				});
			}
		} else if ("feed_list_media_bgimg"
				.equals(tag.getAttribute("node-type"))) {
			if (tweet.getReason() == null) {
				tweet.setThumbnailPic(tag.getAttribute("src"));
			} else {
				tweet.getReason().setThumbnailPic(tag.getAttribute("src"));
			}
		} else if ("feed_list_item_date".equals(tag.getAttribute("node-type"))) {
			if ("S_link2 WB_time".equals(tag.getAttribute("class"))) {
				tweet.setCreatedAt(new Date(Long.parseLong(tag
						.getAttribute("date"))));
			} else if ("S_func2 WB_time".equals(tag.getAttribute("class"))) {
				tweet.getReason().setCreatedAt(
						new Date(Long.parseLong(tag.getAttribute("date"))));
			}
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
