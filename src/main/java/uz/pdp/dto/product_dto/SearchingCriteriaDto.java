package uz.pdp.dto.product_dto;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.Min;
import lombok.*;
import org.springdoc.core.annotations.ParameterObject;
import uz.pdp.enums.Category;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ParameterObject
public class SearchingCriteriaDto {

    @Parameter(description = "Qidirilayotgan mahsulot nomi")
    private String name;

    @Parameter(description = "Qidirilayotgan mahsulot categoriyasi")
    private Category category;

    @Parameter(description = "Nechanchii sahifa", example = "0")
    @Min(0)
    private Integer page;

    @Parameter(description = "Sahifa o'lchami", example = "1")
    @Min(1)
    private Integer size;
}
