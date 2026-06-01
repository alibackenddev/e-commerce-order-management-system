package uz.pdp.mapper;

import lombok.NonNull;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;
import uz.pdp.dto.auth_dto.AuthUserRequestDto;
import uz.pdp.dto.order_dto.OrderResponseDto;
import uz.pdp.dto.page_dto.PageResponseDto;
import uz.pdp.dto.page_dto.PageableDto;
import uz.pdp.dto.product_dto.ProductRequestDto;
import uz.pdp.dto.product_dto.ProductResponseDto;
import uz.pdp.entity.AuthUser;
import uz.pdp.entity.Order;
import uz.pdp.entity.Product;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CustomMapper {


    OrderResponseDto toOrderDto(Order order);

    List<OrderResponseDto> toListOrderDto(List<Order> order);

    ProductResponseDto toProductDto(Product product);

    Product toEntity(ProductRequestDto dto);

    List<ProductResponseDto> toProductResponseDtos(List<Product> dto);

    AuthUser toAuthUser(AuthUserRequestDto dto);

    default PageResponseDto toResponse(Page<@NonNull Product> page) {

        PageableDto pageableDto = PageableDto.builder()
                .offest(page.getPageable().getOffset())
                .pageNumber(page.getPageable().getPageNumber())
                .pageSize(page.getPageable().getPageSize())
                .paged(page.getPageable().isPaged())
                .build();

        return new PageResponseDto(
                page.getContent().stream()
                        .map(this::toProductDto)
                        .toList(),
                pageableDto,
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isFirst(),
                page.isLast(),
                page.isEmpty()
        );
    }
}
