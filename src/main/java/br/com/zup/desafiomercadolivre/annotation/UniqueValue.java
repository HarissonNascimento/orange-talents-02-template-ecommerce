package br.com.zup.desafiomercadolivre.annotation;

import br.com.zup.desafiomercadolivre.annotation.validator.UniqueValueValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Constraint(validatedBy = UniqueValueValidator.class)
@Target(FIELD)
@Retention(RUNTIME)
public @interface UniqueValue {
    String message() default "{br.com.zup.desafiomercadolivre.annotation.UniqueValue}";

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    String fieldName();
    Class<?> domainClass();
}
