package uz.pdp.dto.order_dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.springdoc.core.annotations.ParameterObject;
import uz.pdp.custom_validator.UniqueProductId;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Schema(
        name = "OrderRequestDto",
        description = "Foydalanuvchi buyurtma qilishi uchun kerak bo'ladigan asosiy birlamchi data transfer object"
)
public class OrderRequestDto {

    @NotEmpty
    @Valid
    @UniqueProductId
    @Schema(description = "Buyurtmalar")
    private List<OrderItemsRequestDto> itemsDto;
}
