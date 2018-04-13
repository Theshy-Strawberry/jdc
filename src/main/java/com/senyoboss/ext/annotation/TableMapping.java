package com.senyoboss.ext.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.senyoboss.common.FieldConst;

@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface TableMapping {

    String value();
    
    String ds() default FieldConst.db_config_main;
    
    String pk() default "id";
    
}
