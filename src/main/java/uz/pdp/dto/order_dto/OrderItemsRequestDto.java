package uz.pdp.dto.order_dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springdoc.core.annotations.ParameterObject;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Schema(name = "Order Items Request Dto", description = "Buyurtma qilinuvchi mahsulot uchun data transfer object")
public class OrderItemsRequestDto {

    @NotNull
    @Min(value = 1)
    @Schema(description = "Buyurtma qilinuvchi mahsulotning ID raqami")
    private Long productId;

    @NotNull
    @Min(value = 1)
    @Schema(description = "Buyurtma qilinuvchi mahsulotning miqdori")
    private Integer quantity;

}
