

import java.io.UnsupportedEncodingException;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.chenjw.spider.dt.dao.TweetDAO;
import com.chenjw.spider.dt.dataobject.TweetDO;

public class Test {
	public static void main(String[] args) throws UnsupportedEncodingException {
		ApplicationContext context = new ClassPathXmlApplicationContext(
				"classpath*:context.xml");

		TweetDAO tweetDAO = (TweetDAO) context.getBean("tweetDAO");
		TweetDO tweetDO = new TweetDO();
		String id = Math.random() + "";
		tweetDO.setTid(id);
		tweetDO.setUserId("啊啊啊");
		tweetDO.setContent("陈聚文".getBytes("UTF-8"));
		tweetDAO.addTweet(tweetDO);
		for (TweetDO t : tweetDAO.findByMemberUserId("啊啊啊")) {
			System.out.println(new String(t.getContent(), "UTF-8"));
		}
	}
}
