

import java.io.UnsupportedEncodingException;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.chenjw.spider.dt.service.DeletedTweetCheckService;

public class Main {
	public static void main(String[] args) throws UnsupportedEncodingException {
		ApplicationContext context = new ClassPathXmlApplicationContext(
				"classpath*:context.xml");
		DeletedTweetCheckService deletedTweetCheckService = (DeletedTweetCheckService) context
				.getBean("deletedTweetCheckService");
		deletedTweetCheckService.check("1925238912");
		// DeletedTweetDAO deletedTweetDAO = (DeletedTweetDAO) context
		// .getBean("deletedTweetDAO");
		// TweetDAO tweetDAO = (TweetDAO) context.getBean("tweetDAO");
		// for (TweetDO tweetDO : deletedTweetDAO.findByUserId("1925238912")) {
		// // System.out.println(new String(tweetDO.getContent(), "UTF-8"));
		// }

	}
}
