package br.com.zup.desafiomercadolivre.annotation;

import br.com.zup.desafiomercadolivre.annotation.validator.ExistByIdValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = {ExistByIdValidator.class})
@Target({FIELD})
@Retention(RUNTIME)
public @interface ExistById {

    String message() default "{br.com.zup.desafiomercadolivre.annotation.ExistById}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String idFieldName();

    Class<?> domainClass();
}
