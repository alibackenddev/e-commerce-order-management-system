package uz.pdp.dto.order_dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import uz.pdp.custom_validator.OrderStatusValid;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "Order Status Dto")
public class OrderStatusDto {

    @NotNull
    @OrderStatusValid
    @Schema(description = "Buyurtma statusi uchun data transfer object")
    private String status;
}
