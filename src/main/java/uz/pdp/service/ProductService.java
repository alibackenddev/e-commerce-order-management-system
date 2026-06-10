package uz.pdp.service;

import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.pdp.controller.ProductSpecification;
import uz.pdp.dto.page_dto.PageResponseDto;
import uz.pdp.dto.page_dto.PageResponseDto2;
import uz.pdp.dto.product_dto.ProductRequestDto;
import uz.pdp.dto.product_dto.ProductResponseDto;
import uz.pdp.dto.product_dto.SearchingCriteriaDto;
import uz.pdp.entity.Product;
import uz.pdp.enums.Category;
import uz.pdp.exception_handling.ProductNotFoundException;
import uz.pdp.mapper.CustomMapper;
import uz.pdp.repository.ProductRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

    private final CustomMapper customMapper;
    private final ProductRepository productRepository;

    public PageResponseDto2/*Page<@NonNull ProductResponseDto>*/ findAll1(Integer page, Integer size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<@NonNull Product> all = productRepository.findAll(pageRequest);
        return PageResponseDto2
                .builder()
                .page(all.map(customMapper::toProductDto))
                .build();
    }

    public PageResponseDto findAll2(Integer page, Integer size) {
        Sort sort = Sort.by(Sort.Order.by("id"), Sort.Order.desc("name"));
        PageRequest pageRequest = PageRequest.of(page, size, sort);

        Page<@NonNull Product> all = productRepository.findAll(pageRequest);

        return customMapper.toResponse(all);
    }

    public Page<@NonNull ProductResponseDto> findAll3(Pageable pageable) {
        Page<@NonNull Product> all = productRepository.findAll(pageable);
        return all.map(customMapper::toProductDto);
    }

    public ResponseEntity<@NonNull ProductResponseDto> findOne(@NonNull Long id) {
        Product product = productRepository
                .findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));

        return ResponseEntity.ok(customMapper.toProductDto(product));
    }

    @Transactional
    public ResponseEntity<@NonNull Long> create(ProductRequestDto dto) {
        Product product = customMapper.toEntity(dto);
        return ResponseEntity.ok().body(productRepository.save(product).getId());
    }

    @Transactional
    public ResponseEntity<@NonNull Long> update(Long id, ProductRequestDto dto) {
        Product product = productRepository
                .findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));

        product.setName(dto.getName());
        product.setPrice(dto.getPrice());
        product.setCategory(dto.getCategory());
        product.setStock(dto.getStock());
        productRepository.save(product);
        return ResponseEntity.ok(product.getId());
    }

    @Transactional
    public ResponseEntity<@NonNull String> delete(Long id) {
        Product product = productRepository
                .findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));
        productRepository.deleteById(product.getId());
        return ResponseEntity.ok().body("Product has been deleted");
    }

    public List<@NonNull ProductResponseDto> find(String name, Category category) {
        Specification<@NonNull Product> specification = ProductSpecification.filter(name, category);
        List<@NonNull Product> products = productRepository.findAll(specification);
        return customMapper.toProductResponseDtos(products);
    }


    public PageResponseDto search(SearchingCriteriaDto dto) {
        Pageable pageable = PageRequest.of(
                dto.getPage(),
                dto.getSize(),
                Sort.by(Sort.Order.asc("id"),
                        Sort.Order.asc("name")
                )
        );
        Specification<@NonNull Product> spec = ProductSpecification.filter(dto.getName(), dto.getCategory());
        Page<@NonNull Product> all = productRepository.findAll(spec, pageable);
        return customMapper.toResponse(all);
    }
}
