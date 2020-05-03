package com.junzixiehui.doraon.business.common.validator;

import com.vip.vjtools.vjkit.number.NumberUtil;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * <p>Description: </p>
 * @author: by qulibin
 * @date: 2020/3/20  10:57 PM
 * @version: 1.0
 */
public class NumberValidator implements ConstraintValidator<Number, Object> {

	@Override
	public void initialize(Number constraintAnnotation) {

	}

	@Override
	public boolean isValid(Object value, ConstraintValidatorContext context) {
		String val = String.valueOf(value);
		if (StringUtils.isBlank(val)){
			return false;
		}
		if (NumberUtil.isNumber(val)){
			return true;
		}
		return false;
	}
}
