package uz.pdp.dto.product_dto;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springdoc.core.annotations.ParameterObject;
import uz.pdp.custom_validator.ProductCategory;
import uz.pdp.enums.Category;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Schema(name = "Product Request Dto", description = "Mahsulot yaratish uchun data transfer object")
@ParameterObject
public class ProductRequestDto {

    @NotBlank
    @Size(min = 2, max = 100)
    @Schema(description = "Mahsulot nomi")
    private String name;

    @NotNull
    @Digits(integer = 10, fraction = 2, message = "Invalid format")
    @Positive
    @Schema(description = "Mahsulot narxi")
    private BigDecimal price;

    @NotNull
    @PositiveOrZero(message = "Stock can't be negative")
    @Schema(description = "Mahsulot miqdori")
    private Integer stock;

    @NotNull
    @ProductCategory
    @Schema(description = "Mahsulot kategoriyasi")
    private Category category;
}
