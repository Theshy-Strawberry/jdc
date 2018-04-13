package com.senyoboss.Validator;

/**
 * 短路校验，有一个参数不和规法立即跳出匹配
 * @title ShortCircuitValidator.java
 * @description
 * @company Senyoboss
 * @author Jr.REX
 * @version 1.0
 */
public abstract class ShortCircuitValidator extends BaseValidator {
	{
		this.setShortCircuit(true);
	}

}
