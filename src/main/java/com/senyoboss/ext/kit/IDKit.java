package com.senyoboss.ext.kit;

import java.util.UUID;

public class IDKit {

	/**
	 * 获取UUID
	 * @return
	 */
	public static String getUuid(boolean is32bit){
		String uuid = UUID.randomUUID().toString();
		if(is32bit){
			return uuid.toString().replace("-", ""); 
		}
		return uuid;
	}
	
	public static void main(String[] args){
		System.out.println(getUuid(true));
	}
	
}
