package uz.pdp.dto.product_dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(name = "Product Response Dto", description = "Mahsulotni foydalanuvchiga ko'rsatish uchun data transfer object")
public class ProductResponseDto {

    @Schema(description = "Mahsulot ID raqami")
    private Long id;

    @Schema(description = "Mahsulot nomi")
    private String name;

    @Schema(description = "Mahsulot narxi")
    private BigDecimal price;

    @Schema(description = "Mahsulot soni")
    private Integer stock;

    @Schema(description = "Mahsulot categoriyasi")
    private String category;

    @Schema(description = "Mahsulot aktivligi")
    private Boolean isActive;

    @Schema(description = "Mahsulot yaratilingan vaqti")
    private LocalDateTime createdAt;
}
