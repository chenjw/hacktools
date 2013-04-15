package com.chenjw.spider.dt.utils;

import java.math.BigDecimal;
import java.util.Date;

public class DeleteSortUtils {
	// TID的系数
	private static final BigDecimal TID_RATE = new BigDecimal((long) (Math.pow(
			10, 16)));

	public static String getDeleteSort(Date deleteDate, String tid) {
		BigDecimal r = new BigDecimal(deleteDate.getTime()).multiply(TID_RATE)
				.add(new BigDecimal(Long.parseLong(tid)));
		return String.valueOf(r);
	}

	public static void main(String[] args) {
		System.out.println(getDeleteSort(new Date(), "3533633015206343"));
	}
}
