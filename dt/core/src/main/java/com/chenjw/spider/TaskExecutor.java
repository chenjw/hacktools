package com.chenjw.spider;

import java.util.List;

public interface TaskExecutor {
	public void execute(Task task);

	public void execute(List<Task> tasks);
}
