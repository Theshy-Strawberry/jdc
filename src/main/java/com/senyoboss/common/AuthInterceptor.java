package com.senyoboss.common;

import com.jfinal.aop.Interceptor;
import com.jfinal.core.Controller;
import com.jfinal.aop.Invocation;
import com.jfinal.plugin.ehcache.CacheKit;

/**
 * @company Senyoboss
 * @author Jr
 * @version 1.0
 */
public class AuthInterceptor implements Interceptor {

	@Override
	public void intercept(Invocation ai) {
		// TODO Auto-generated method stub
		Controller controller = ai.getController();
	    if (CacheKit.get("UserCachingFilter", "user") != null)
	      ai.invoke();
	    else
	      controller.setAttr("result","no_user");
	}

}
