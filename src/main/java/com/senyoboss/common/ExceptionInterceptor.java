package com.senyoboss.common;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;
import com.jfinal.log.Logger;

public class ExceptionInterceptor implements Interceptor {

    @Override
    public void intercept(Invocation ai) {
        Controller controller = ai.getController();
        Logger logger = Logger.getLogger(controller.getClass());
        try {
            ai.invoke();
        } catch (Exception e) {
            logger.info("["+controller.getClass().getName()+"]-["+e.getMessage()+"]");
            controller.setAttr("result", "error");
        }
    }
}
