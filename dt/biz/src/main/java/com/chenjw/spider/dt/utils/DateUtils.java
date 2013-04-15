package com.chenjw.spider.dt.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {
	public static String toLocaleString(Date date) {
		if (date == null) {
			return "";
		}
		Date now = new Date();
		long time = now.getTime() - date.getTime();
		// 60分钟内，显示“30分钟前”
		if (time < org.apache.commons.lang.time.DateUtils.MILLIS_PER_HOUR) {
			return time
					/ org.apache.commons.lang.time.DateUtils.MILLIS_PER_MINUTE
					+ "分钟前";
		}
		if (isSameDay(now, date)) {
			return (new SimpleDateFormat("今天HH:mm")).format(date);
		}
		if (isSameYear(now, date)) {
			return (new SimpleDateFormat("M月d日HH:mm")).format(date);
		}
		return (new SimpleDateFormat("yy年M月d日HH:mm")).format(date);
	}

	public static boolean isSameDay(Date d1, Date d2) {
		if (d1 == null || d2 == null) {
			throw new IllegalArgumentException("The date must not be null");
		}
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(d1);
		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(d2);
		return (cal1.get(Calendar.ERA) == cal2.get(Calendar.ERA)
				&& cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) && cal1
					.get(Calendar.DAY_OF_YEAR) == cal2
				.get(Calendar.DAY_OF_YEAR));
	}

	public static boolean isSameMonth(Date d1, Date d2) {
		if (d1 == null || d2 == null) {
			throw new IllegalArgumentException("The date must not be null");
		}
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(d1);
		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(d2);
		return (cal1.get(Calendar.ERA) == cal2.get(Calendar.ERA)
				&& cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) && cal1
					.get(Calendar.MONTH) == cal2.get(Calendar.MONTH));
	}

	public static boolean isSameYear(Date d1, Date d2) {
		if (d1 == null || d2 == null) {
			throw new IllegalArgumentException("The date must not be null");
		}
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(d1);
		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(d2);
		return (cal1.get(Calendar.ERA) == cal2.get(Calendar.ERA) && cal1
				.get(Calendar.YEAR) == cal2.get(Calendar.YEAR));
	}
}
