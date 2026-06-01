package uz.pdp.dto.order_dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springdoc.core.annotations.ParameterObject;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(
        name = "User Order Update Dto"
        ,description = "Foydalanuvchi o'ziga tegishli buyurtmani yangilashi uchun data transfer object"
)
@ParameterObject
public class UserOrderUpdateDto {

    @NotNull
    @Min(value = 1)
    @Schema(description = "Yangilanishi kerak bo'lgan buyurtmaning ID raqami")
    private Long orderId;

    @NotNull
    @Min(value = 1)
    @Schema(description = "Yangilanishi kerak bo'lgan buyurtmadagi buyurtmaning ID raqami")
    private Long orderItemId;

    @NotNull
    @Min(value = 1)
    @Schema(description = "Yangilanishi kerak bo'lgan buyurtmadagi mahsulotning ID raqami")
    private Long productId;

    @NotNull
    @Min(value = 1)
    @Schema(description = "Yangilanishi kerak bo'lgan buyurtmadagi mahsulotning miqdori")
    private Integer quantity;
}
