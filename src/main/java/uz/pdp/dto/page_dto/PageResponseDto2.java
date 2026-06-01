package uz.pdp.dto.page_dto;

import lombok.*;
import org.springframework.data.domain.Page;
import uz.pdp.dto.product_dto.ProductResponseDto;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PageResponseDto2 {
    private Page<@NonNull ProductResponseDto> page;

}
