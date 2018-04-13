package com.senyoboss.ioc;

import java.lang.reflect.Field;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;

/**
 * @author Jr.REX
 * @version 1.0
 * @company Senyoboss
 */
public class AutowiredInterceptor implements Interceptor {

    @Override
    public void intercept(Invocation inv) {

        try {
            Field[] fields = inv.getController().getClass().getDeclaredFields();
            for (Field f : fields) {
                if (f != null && f.isAnnotationPresent(Autowired.class)) {
                    Autowired autowired = f.getAnnotation(Autowired.class);
                    Object value = null;
                    if (autowired.value() != null && !"".equals(autowired.value())) {
                        value = Ioc.getBean(autowired.value());
                    } else {
                        value = Ioc.getBean(f.getType());
                    }

                    f.setAccessible(true);
                    f.set(inv.getController(), value);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        inv.invoke();
    }

}
