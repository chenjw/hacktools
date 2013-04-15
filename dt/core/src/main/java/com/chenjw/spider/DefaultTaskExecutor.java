package com.chenjw.spider;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class DefaultTaskExecutor implements TaskExecutor {

	private ThreadPoolExecutor executorService = new ThreadPoolExecutor(10, 10,
			1000, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());

	@Override
	public void execute(final Task task) {
		executorService.execute(new Runnable() {
			@Override
			public void run() {
				task.execute();
			}
		});
		// task.execute();
		// getTaskList(task.getContextKey()).addLast(task);
	}

	@Override
	public void execute(List<Task> tasks) {
		for (Task task : tasks) {
			execute(task);
		}
	}

}
