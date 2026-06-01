package uz.pdp.dto.order_dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import uz.pdp.dto.product_dto.ProductResponseDto;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(name = "Order Items Response Dto", description = "Buyurtma qilingan mahsulotni faoydalanuvchiga ko'rsatuvchi data transfer object")
public class OrderItemsResponseDto {

    @Schema(description = "Buyurtmadagi mahsulotning ID ra")
    private Long id;

    @Schema(description = "Buyurtmadagi mahsulot haqidagi malumotlar")
    private ProductResponseDto product;

    @Schema(description = "Buyurtma qilingan mahsulotning soni")
    private Integer quantity;

    @Schema(description = "Buyurtma qilingan mahsulotni buyurtma vaqtidagi narxi")
    private BigDecimal unitPrice;

    @Schema(description = "Buyurtma qilingan mahsulotning umumiy narxi")
    private BigDecimal totalPrice;
}
