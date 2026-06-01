package uz.pdp.custom_validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueProductsIdValidator.class)
public @interface UniqueProductId {

    String message() default "Duplicate product id found";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
