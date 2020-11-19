package com.junzixiehui.doraon.business.common.validator;

import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * <p>Description: </p>
 * @author: by jxll
 * @date: 2020/3/20  10:57 PM
 * @version: 1.0
 */
public class NotBlankHardValidator implements ConstraintValidator<NotBlankHard, String> {

	@Override
	public void initialize(NotBlankHard constraintAnnotation) {

	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (StringUtils.isBlank(value) || "null".equals(value)){
			return false;
		}
		return true;
	}
}
