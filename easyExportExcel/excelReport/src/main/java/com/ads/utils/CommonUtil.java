package com.ads.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommonUtil {
	public static boolean isIdCard(String idCard) {
		String regex = "/(^\\d{15}$)|(^\\d{18}$)|(^\\d{17}(\\d|X|x)$)/";
		if (idCard.length() != 18) {
			return false;
		} else {
			Pattern p = Pattern.compile(regex);
			Matcher m = p.matcher(idCard);
			boolean isMatch = m.matches();
			return isMatch;
		}
	}

	public static boolean isPhone(String phone) {
		String regex = "^((13[0-9])|(14[5,7,9])|(15([0-3]|[5-9]))|(166)|(17[0,1,3,5,6,7,8])|(18[0-9])|(19[8|9]))\\d{8}$";
		if (phone.length() != 11) {
			return false;
		} else {
			Pattern p = Pattern.compile(regex);
			Matcher m = p.matcher(phone);
			boolean isMatch = m.matches();
			return isMatch;
		}
	}
}
