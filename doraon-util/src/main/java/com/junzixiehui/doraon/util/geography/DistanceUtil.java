package com.junzixiehui.doraon.util.geography;

/**
 * <p>Description: 距离工具</p>
 * @author: by jxll
 * @date: 2020/3/22  9:51 PM
 * @version: 1.0
 */
public class DistanceUtil {

	private static final double EARTH_RADIUS = 6378137;//赤道半径

	private static double rad(double d) {
		return d * Math.PI / 180.0;
	}

	/**
	 * @author: jxll
	 * @description:
	 * @date: 9:52 PM 2020/3/22
	 * @return: 单位米
	 */
	public static double getDistance(double lng, double lat, double lng1, double lat1) {
		double radLat1 = rad(lat);
		double radLat2 = rad(lat1);
		double a = radLat1 - radLat2;
		double b = rad(lng) - rad(lng1);
		double s = 2 * Math.asin(Math.sqrt(
				Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
		s = s * EARTH_RADIUS;
		return s;
	}
}
