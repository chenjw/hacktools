package com.chenjw.spider.aliuser.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;

import com.chenjw.client.exception.ErrorCodeEnum;
import com.chenjw.spider.DefaultTaskExecutor;
import com.chenjw.spider.Page;
import com.chenjw.spider.PageFetcher;
import com.chenjw.spider.Task;
import com.chenjw.spider.TaskExecutor;
import com.chenjw.spider.aliuser.AliUserService;
import com.chenjw.spider.aliuser.constants.AliUserConstants;
import com.chenjw.spider.aliuser.dao.AliUserDAO;
import com.chenjw.spider.aliuser.model.AliUserContext;
import com.chenjw.spider.aliuser.model.AliUserInfo;
import com.chenjw.spider.config.PageConfig;
import com.chenjw.spider.config.SpiderConfig;
import com.chenjw.spider.location.HttpLocation;
import com.chenjw.tools.BeanCopyUtils;

public class AliUserServiceImpl implements AliUserService {

	private SpiderConfig spiderConfig;

	private PageFetcher pageFetcher;

	private AliUserDAO aliUserDAO;
	private TaskExecutor taskExecutor = new DefaultTaskExecutor();

	public void start() {
		AliUserInfo userInfo = aliUserDAO.getLastUserInfo();
		if (userInfo == null) {
			restart();
		} else {
			startFrom(userInfo.getAliwayId() + 1);
		}
	}

	public void restart() {
		startFrom(1);
	}

	private void startFrom(int startNum) {
		AliUserContext context = new AliUserContext();
		context.getUserInfo().setAliwayId(startNum);
		HttpLocation startLocation = createAliwayLocation(context);
		HttpTask task = new HttpTask(startLocation, context);
		taskExecutor.execute(task);
	}

	private void handle(AliUserContext context, HttpLocation loc, Page page) {
		if (StringUtils.equals(loc.getPageId(), AliUserConstants.PAGE_ALIWAY)) {
			// aliway页面不存在，结束
			if (!page.isSuccess()) {
				// 如果是系统原因，重试
				if (page.getErrorCode() == ErrorCodeEnum.SYSTEM_ERROR) {
					HttpTask t = new HttpTask(loc, context);
					taskExecutor.execute(t);
				}
				return;
			}
			AliUserInfo userInfo = context.getUserInfo();
			BeanCopyUtils.copyProperties(userInfo, page.getContext());
			String email = userInfo.getEmail();
			if (!StringUtils.isBlank(email)) {
				if (email.indexOf("@") != -1) {
					String userId = StringUtils.substringBefore(email, "@");
					userInfo.setUserId(userId);
				}
			}
			boolean isFetchNw = false;

			if (userInfo.getWorkNum() != 0) {
				if (StringUtils.equals("已离职", userInfo.getStatus())) {
					System.out.println("[info]" + userInfo.getName() + " 已离职");
				} else {
					HttpTask t = new HttpTask(createNwLocation(context),
							context);
					taskExecutor.execute(t);
					isFetchNw = true;
				}
			}

			// 如果不取内网数据就直接保存了
			if (isFetchNw == false) {
				aliUserDAO.save(userInfo);
			}
			// 创建下一个aliwayId的任务
			AliUserContext next = new AliUserContext();
			next.getUserInfo().setAliwayId(userInfo.getAliwayId() + 1);
			HttpTask t = new HttpTask(createAliwayLocation(next), next);
			taskExecutor.execute(t);

		} else if (StringUtils
				.equals(loc.getPageId(), AliUserConstants.PAGE_NW)) {
			AliUserInfo userInfo = context.getUserInfo();
			if (page.isSuccess()) {
				Map<String, String> temp = new HashMap<String, String>();
				for (Entry<String, String> entry : page.getContext().entrySet()) {
					if (StringUtils.isNotBlank(entry.getValue())) {
						temp.put(entry.getKey(), entry.getValue());
					}
				}
				BeanCopyUtils.copyProperties(userInfo, temp);
				aliUserDAO.save(userInfo);
			} else {
				if (page.getErrorCode() == ErrorCodeEnum.SYSTEM_ERROR) {
					HttpTask t = new HttpTask(loc, context);
					taskExecutor.execute(t);
				}
				return;
			}
		}

	}

	private HttpLocation createAliwayLocation(AliUserContext context) {
		PageConfig alipwayPageConfig = spiderConfig
				.getPage(AliUserConstants.PAGE_ALIWAY);
		String url = alipwayPageConfig.getUrl();
		Map<String, String> param = copyMap(alipwayPageConfig.getParams());
		param.put("u", String.valueOf(context.getUserInfo().getAliwayId()));
		HttpLocation loc = new HttpLocation();
		loc.setEncoding(alipwayPageConfig.getEncoding());
		loc.setUrl(url);
		loc.setQueryParam(param);
		loc.setPageId(AliUserConstants.PAGE_ALIWAY);

		return loc;
	}

	private HttpLocation createNwLocation(AliUserContext context) {
		PageConfig nwPageConfig = spiderConfig
				.getPage(AliUserConstants.PAGE_NW);
		String url = String.format(nwPageConfig.getUrl(), context.getUserInfo()
				.getWorkNum());
		HttpLocation loc = new HttpLocation();
		loc.setEncoding(nwPageConfig.getEncoding());
		loc.setUrl(url);
		loc.setPageId(AliUserConstants.PAGE_NW);

		return loc;
	}

	private Map<String, String> copyMap(Map<String, String> orign) {
		if (orign == null) {
			return null;
		}
		Map<String, String> result = new HashMap<String, String>();
		for (Entry<String, String> entry : orign.entrySet()) {
			result.put(entry.getKey(), entry.getValue());
		}
		return result;
	}

	public class HttpTask implements Task {

		private HttpLocation location;
		private AliUserContext context;

		public HttpTask(HttpLocation location, AliUserContext context) {

			this.location = location;
			this.context = context;
		}

		public void execute() {
			long start = System.currentTimeMillis();
			Page page = pageFetcher.fetch(location);
			handle(context, location, page);
			System.out.println(location.getUrl() + " "
					+ location.getQueryParam() + " use "
					+ (System.currentTimeMillis() - start) + " ms");
		}

		@Override
		public Object getContextKey() {
			return context;
		}

	}

	public void setSpiderConfig(SpiderConfig spiderConfig) {
		this.spiderConfig = spiderConfig;
	}

	public void setPageFetcher(PageFetcher pageFetcher) {
		this.pageFetcher = pageFetcher;
	}

	public void setAliUserDAO(AliUserDAO aliUserDAO) {
		this.aliUserDAO = aliUserDAO;
	}

}
