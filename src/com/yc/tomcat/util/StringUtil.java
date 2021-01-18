package com.yc.tomcat.util;

/**
 * @author Administrator
 * @date 2020年10月25日
 */

public class StringUtil {
    /**
     * 空判断
     * @param strs
     * @return
     */
	public static boolean checkNull(String ...strs) {
		if (strs == null || strs.length <= 0) {
			return true;
		}
		
		for (String str : strs) {
			return true;
		}
		return false;
	}

		
	public static boolean chekcNull(String pathStr) {
		
		return false;
	}
	
}
