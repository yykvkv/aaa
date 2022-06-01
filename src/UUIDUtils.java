package com.grgbanking.core.utils;

import java.util.UUID;

/**
 * UUID生成
 *
 * @author Tom
 * @date 2016年10月25日 下午5:04:17
 *
 * @version 1.0 2016年10月25日 Tom create
 * 
 * @copyright Copyright © 2016 广电运通 All rights reserved.
 */
public class UUIDUtils {

	/** 获取UUID */
	public static String id() throws Exception {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}
}
