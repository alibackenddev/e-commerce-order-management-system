package uz.pdp.custom_validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = OrderStatusValidator.class)
public @interface OrderStatusValid {
    String message() default "Category name is invalid. Category must be follow : [ PENDING, CONFIRMED, SHIPPED, DELIVERED, CANCELLED]";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
