package uz.pdp.dto.order_dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import uz.pdp.enums.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Schema(name = "Order Response Dto", description = "Buyurtmani foydalanuvchiga ko'rsatish uchun data transfer obeject")
public class OrderResponseDto {

    @Schema(description = "Buyurtmaning ID raqami")
    private Long id;

    @Schema(description = "Buyurtmachining ismi (username)")
    private String customerName;

    @Schema(description = "Buyurtmachining emaili (email)")
    private String customerEmail;

    @Schema(description = "Buyurtma qilinan vaqt")
    private LocalDateTime orderDate;

    @Schema(description = "Buyurtmaning holati")
    private OrderStatus orderStatus;

    @Schema(description = "Ushbu buyurtmaning umumiy summasi")
    private BigDecimal totalAmount;

    @Schema(description = "Buyurtmadagi mahsulotlar")
    private List<OrderItemsResponseDto> orderItems;
}
