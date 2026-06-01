package uz.pdp.custom_validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import uz.pdp.dto.order_dto.OrderItemsRequestDto;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UniqueProductsIdValidator implements ConstraintValidator<UniqueProductId, List<OrderItemsRequestDto>> {

    @Override
    public boolean isValid(List<OrderItemsRequestDto> items, ConstraintValidatorContext context) {
        if (items == null || items.isEmpty()) {
            return true;
        }

        Set<Long> uniqueProductIds = new HashSet<>();
        for (OrderItemsRequestDto item : items) {
            if (!uniqueProductIds.add(item.getProductId())) {
                return false;
            }
        }
        return true;
    }
}
