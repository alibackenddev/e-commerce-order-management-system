package uz.pdp.custom_validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import uz.pdp.enums.OrderStatus;

import java.util.Objects;

public class OrderStatusValidator implements ConstraintValidator<OrderStatusValid, String> {
    @Override
    public boolean isValid(String status, ConstraintValidatorContext constraintValidatorContext) {
        for (OrderStatus value : OrderStatus.values()) {
            if (Objects.equals(value.name(), status)) {
                return true;
            }
        }
        return false;
    }
}
