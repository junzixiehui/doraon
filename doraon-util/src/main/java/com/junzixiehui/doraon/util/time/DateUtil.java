package com.junzixiehui.doraon.util.time;

import com.alibaba.excel.util.DateUtils;
import com.google.common.base.Preconditions;
import com.vip.vjtools.vjkit.time.DateFormatUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.format.DateTimeFormat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * <p>Description: </p>
 *
 * @author: by qulibin
 * @date: 2020/3/9  5:38 PM
 * @version: 1.0
 */
@Slf4j
public final class DateUtil {

	public static final String FORMAT_YYYY_MM_DD = "yyyy-MM-dd";
	public static final String FORMAT_YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
	public static final String FORMAT_YYYY_MM_DD_HH_MM_SS_SSS = "yyyy-MM-dd HH:mm:ss.SSS";
	private static final TimeZone TIMEZONE = TimeZone.getTimeZone("GMT+8");

	public static String getCurrentTime() {
		return DateUtils.format(new Date(), FORMAT_YYYY_MM_DD_HH_MM_SS);
	}

	public static String format(long dateTime) {
		if (dateTime <= 0) {
			return "";
		}
		try {
			return DateUtils.format(parse(dateTime), FORMAT_YYYY_MM_DD_HH_MM_SS);
		} catch (Exception e) {
			log.error("format error" + dateTime, e);
			return "";
		}
	}

	public static long parse(String dateTime) {
		if (StringUtils.isBlank(dateTime)) {
			return 0;
		}
		try {
			return format2Long(DateFormatUtil.parseDate(FORMAT_YYYY_MM_DD_HH_MM_SS, dateTime));
		} catch (Exception e) {
			log.error("format error" + dateTime, e);
			return 0;
		}
	}

	public static String timeStamp2Date(long seconds, String format) {
		if (StringUtils.isBlank(format)) {
			format = FORMAT_YYYY_MM_DD_HH_MM_SS;
		}
		final Long timeStamp = Long.valueOf(seconds + "000");
		final Date date = new Date(timeStamp);
		DateTime dateTime = new DateTime(date);
		return dateTime.toString(DateTimeFormat.forPattern(format));
	}

	public static long date2TimeStamp(String date, String format) {
		DateTime dateTime = DateTime.parse(date, DateTimeFormat.forPattern(format));
		return dateTime.toDate().getTime() / 1000;
	}

	public static int compare(Date date1, Date date2) {
		Preconditions.checkArgument(date1 != null, "Param date1 must be not null");
		Preconditions.checkArgument(date2 != null, "Param date2 must be not null");
		if (date1.after(date2)) {
			return 1;
		} else {
			return date1.before(date2) ? -1 : 0;
		}
	}

	public static long format2Long(Date date) {
		return format2Long(date, false);
	}

	public static long format2Long(Date date, boolean hasMillisecond) {
		Calendar calendar = Calendar.getInstance(TIMEZONE);
		calendar.setTime(date);
		String year = String.format("%04d", calendar.get(1));
		String month = String.format("%02d", calendar.get(2) + 1);
		String day = String.format("%02d", calendar.get(5));
		String hour = String.format("%02d", calendar.get(11));
		String minute = String.format("%02d", calendar.get(12));
		String second = String.format("%02d", calendar.get(13));
		String str = year + month + day + hour + minute + second;
		if (hasMillisecond) {
			String millisecond = String.format("%03d", calendar.get(14));
			str = str.concat(millisecond);
		}

		return Long.parseLong(str);
	}

	public static int format2Int(Date date) {
		Calendar calendar = Calendar.getInstance(TIMEZONE);
		calendar.setTime(date);
		String year = String.format("%04d", calendar.get(1));
		String month = String.format("%02d", calendar.get(2) + 1);
		String day = String.format("%02d", calendar.get(5));
		return Integer.parseInt(year + month + day);
	}

	public static long formatNow2Long() {
		return format2Long(new Date());
	}


	public static Date parse(long format) {
		String str = String.valueOf(format);
		if (str.length() >= 8) {
			Calendar calendar = Calendar.getInstance(TIMEZONE);
			calendar.clear();
			calendar.set(1, Integer.parseInt(str.substring(0, 4)));
			calendar.set(2, Integer.parseInt(str.substring(4, 6)) - 1);
			calendar.set(5, Integer.parseInt(str.substring(6, 8)));
			if (str.length() >= 14) {
				calendar.set(11, Integer.parseInt(str.substring(8, 10)));
				calendar.set(12, Integer.parseInt(str.substring(10, 12)));
				calendar.set(13, Integer.parseInt(str.substring(12, 14)));
			}

			if (str.length() == 17) {
				calendar.set(14, Integer.parseInt(str.substring(14, 17)));
			}

			return calendar.getTime();
		} else {
			return null;
		}
	}

	public static Date addYears(Date date, int amount) {
		return org.apache.commons.lang3.time.DateUtils.addYears(date, amount);
	}

	public static Date addMonths(Date date, int amount) {
		return org.apache.commons.lang3.time.DateUtils.addMonths(date, amount);
	}

	public static Date addWeeks(Date date, int amount) {
		return org.apache.commons.lang3.time.DateUtils.addWeeks(date, amount);
	}

	public static Date addDays(Date date, int amount) {
		return org.apache.commons.lang3.time.DateUtils.addDays(date, amount);
	}

	public static Date addHours(Date date, int amount) {
		return org.apache.commons.lang3.time.DateUtils.addHours(date, amount);
	}

	public static Date addMinutes(Date date, int amount) {
		return org.apache.commons.lang3.time.DateUtils.addMinutes(date, amount);
	}

	public static Date addSeconds(Date date, int amount) {
		return org.apache.commons.lang3.time.DateUtils.addSeconds(date, amount);
	}

	public static Date addMilliseconds(Date date, int amount) {
		return org.apache.commons.lang3.time.DateUtils.addMilliseconds(date, amount);
	}


	public static long durationSeconds(Date startDate, Date endDate) {
		DateTime startTimeD = new DateTime(startDate);
		DateTime endTimeD = new DateTime(endDate);
		Duration duration;
		if (startTimeD.isBefore(endTimeD)) {
			duration = new Duration(startTimeD, endTimeD);
		} else {
			duration = new Duration(endTimeD, startTimeD);
		}

		return duration.getStandardSeconds();
	}

	public static long durationSeconds(long startDateTime, long endDateTime) {
		final Date startDate = parse(startDateTime);
		final Date endDate = parse(endDateTime);
		return durationSeconds(startDate, endDate);
	}
}
