package uz.pdp.dto.page_dto;

import lombok.*;
import uz.pdp.dto.product_dto.ProductResponseDto;

import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PageResponseDto {

    private List<ProductResponseDto> content;
    private PageableDto pageableDto;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
    private boolean first;
    private boolean last;
    private boolean empty;

}
