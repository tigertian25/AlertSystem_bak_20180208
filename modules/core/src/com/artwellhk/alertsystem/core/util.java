package com.artwellhk.alertsystem.core;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.time.DateUtils;

public class util {
//	public static Date stringToDate(String dateStr) {
//		System.out.println(dateStr);
//		Date date = null;
//		try {
//			date = DateUtils.parseDate(dateStr, new String[] { "yyyy-MM-dd HH:mm:ss" });
//		} catch (ParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return date;
//
//	}

	// 计算时间差工具
	public static String dateUtil(Date d1, Date d2) {
		String timeDifference = "";
		long l = d1.getTime() - d2.getTime();
		long day = l / (24 * 60 * 60 * 1000);
		long hour = (l / (60 * 60 * 1000) - day * 24);
		long min = ((l / (60 * 1000)) - day * 24 * 60 - hour * 60);
		long s = (l / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
		if (day > 0) {
			timeDifference += day + "天";
		}
		if (hour > 0) {
			timeDifference += hour + "小时";
		}
		if (min > 0) {
			timeDifference += min + "分";
		}
		if (s > 0) {
			timeDifference += s + "秒";
		}
		//System.out.println("" + day + "天" + hour + "小时" + min + "分" + s + "秒");
		return timeDifference;
	}
}
