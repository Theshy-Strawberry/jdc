package com.senyoboss.Validator;

import com.jfinal.core.Controller;
import com.jfinal.kit.StrKit;
import com.jfinal.validate.Validator;
import com.senyoboss.render.JCaptchaRender;

/**
 * 通用校验器
 * @title BaseValidator.java
 * @description
 * @company Senyoboss
 * @author Jr.REX
 * @version 1.0
 */
public abstract class BaseValidator extends Validator {

	/**
	 * Validate JCaptcha
	 */
	protected void validateJCaptcha(String field, String errorKey, String errorMessage) {
		Controller c = getController();
		if (StrKit.isBlank(field)) {
			addError(errorKey, errorMessage);
		}
		String value = c.getPara(field);
		boolean result = JCaptchaRender.validate(c, value);
		if (!result) {
			addError(errorKey, errorMessage);
		}
	}
}
