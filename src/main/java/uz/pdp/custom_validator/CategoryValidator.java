package uz.pdp.custom_validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import uz.pdp.enums.Category;

import java.util.Objects;

public class CategoryValidator implements ConstraintValidator<ProductCategory, Category> {
    @Override
    public boolean isValid(Category category, ConstraintValidatorContext context) {
        if (Objects.isNull(category))
            return true;
        for (Category value : Category.values()) {
            if (Objects.equals(category, value)) {
                return true;
            }
        }
        return false;
    }
}
