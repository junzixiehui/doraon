package com.junzixiehui.doraon.business.common.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>Description: </p>
 * @author: by qulibin
 * @date: 2020/3/20  10:55 PM
 * @version: 1.0
 */
@Target({ ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NotBlankHardValidator.class)
public @interface NotBlankHard {

	String message() default "字段不能为空或者null值";

	Class<?>[] groups() default { };

	Class<? extends Payload>[] payload() default { };
}
