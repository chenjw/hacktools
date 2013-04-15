package com.chenjw.spider;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class DefaultTaskExecutor implements TaskExecutor {
	private Map<Object, LinkedList<Task>> handling = new ConcurrentHashMap<Object, LinkedList<Task>>();
	private ThreadPoolExecutor executorService = new ThreadPoolExecutor(10, 10,
			1000, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());

	private LinkedList<Task> getTaskList(Object contextKey) {
		LinkedList<Task> list = handling.get(contextKey);
		if (list == null) {
			list = new LinkedList<Task>();
			handling.put(contextKey, list);
		}
		return list;
	}

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
