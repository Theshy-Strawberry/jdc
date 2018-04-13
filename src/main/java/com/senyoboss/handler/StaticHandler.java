package com.senyoboss.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jfinal.handler.Handler;

/**
 * 
 * 静态文件目录设置
 * 
 * <br>解决伪静态后的，静态文件中的html文件不能被访问
 * 
 *  * example
 * <pre>
 * me.add(new StaticHandler("/js", "/css"));
 * </pre>
 * @title StaticHandler.java
 * @description
 * @company Senyoboss
 * @author Jr.REX
 * @version 1.0
 */
public class StaticHandler extends Handler {

	// 需要排除的目录...
	public final String[] dirs;

	public StaticHandler(String... dirs) {
		this.dirs = dirs;
	}

	@Override
	public void handle(String target, HttpServletRequest request,
			HttpServletResponse response, boolean[] isHandled) {

		// 判定是否要排除
		boolean needExclude = false;
		for (String dir : dirs) {
			if (target.startsWith(dir)) {
				needExclude = true;
				break;
			}
		}
		if (needExclude) {
			return;
		}
		nextHandler.handle(target, request, response, isHandled);
	}

}
