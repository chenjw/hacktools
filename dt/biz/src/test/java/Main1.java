

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.chenjw.spider.dt.model.TweetModel;
import com.chenjw.spider.dt.service.WeiboService;

public class Main1 {
	public static void main(String[] args) throws UnsupportedEncodingException {
		ApplicationContext context = new ClassPathXmlApplicationContext(
				"classpath*:context.xml");
		WeiboService weiboService = (WeiboService) context
				.getBean("weiboService");
		// 1783831337
		// 1925238912
		String userId = "1783831337";
		long sinceId = 100000;
		int success = 0;
		long start = System.currentTimeMillis();
		while (true) {
			List<TweetModel> list = weiboService.findUserTimelineByUserId(
					userId, null, sinceId);
			if (list != null) {
				for (TweetModel t : list) {
					System.out.println(t.getId() + " " + t.getText());
				}
				success++;
			}
			System.out.println(success + " "
					+ (System.currentTimeMillis() - start) / 1000 + " s");

		}

		// System.out.println("finished!");
	}
}
