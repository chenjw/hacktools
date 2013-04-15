

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.chenjw.spider.dt.model.TweetModel;
import com.chenjw.spider.dt.model.TokenModel;
import com.chenjw.spider.dt.service.WeiboService;

public class WeiboCnTest {
	public static void main(String[] args) throws UnsupportedEncodingException {
		ApplicationContext context = new ClassPathXmlApplicationContext(
				"classpath*:weibo-cn-test.xml");
		WeiboService weiboService = (WeiboService) context
				.getBean("weiboService");
		// 1783831337
		// 1925238912
		while(true){
			weiboService.findTopTimeline();
		}
		
		// System.out.println("finished!");
	}
}
