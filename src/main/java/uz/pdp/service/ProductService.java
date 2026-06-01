package uz.pdp.service;

import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
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
public class ProductService {


    private final CustomMapper customMapper;
    private final ProductRepository productRepository;

    public ProductService(CustomMapper customMapper, ProductRepository productRepository) {
        this.customMapper = customMapper;
        this.productRepository = productRepository;
    }

    public PageResponseDto2/*Page<@NonNull ProductResponseDto>*/ findAll(Integer page, Integer size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<@NonNull Product> all = productRepository.findAll(pageRequest);
        return PageResponseDto2
                .builder()
                .page(all.map(customMapper::toProductDto))
                .build();
    }

    public PageResponseDto findAll22(Integer page, Integer size) {
        Sort sort = Sort.by(Sort.Order.by("id"), Sort.Order.desc("name"));
        PageRequest pageRequest = PageRequest.of(page, size, sort);

        Page<@NonNull Product> all = productRepository.findAll(pageRequest);

        return customMapper.toResponse(all);
    }

    public Page<@NonNull ProductResponseDto> findAll2(Pageable pageable) {
        Page<@NonNull Product> all = productRepository.findAll(pageable);
        return all.map(customMapper::toProductDto);
    }

    public ResponseEntity<@NonNull ProductResponseDto> findOne(@NonNull Long id) {
        Product product = productRepository
                .findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));

        return ResponseEntity.ok(customMapper.toProductDto(product));
    }

    public ResponseEntity<@NonNull Long> create(ProductRequestDto dto) {
        Product product = customMapper.toEntity(dto);
        return ResponseEntity.ok().body(productRepository.save(product).getId());
    }

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

    public ResponseEntity<@NonNull String> delete(Long id) {
        Product product = productRepository
                .findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));
        productRepository.deleteById(product.getId());
        return ResponseEntity.ok().body("Product has been deleted");
    }

    public List<@NonNull ProductResponseDto> find(String name, Category category) {
        List<@NonNull Product> products = productRepository.find(name, category.name());
        return customMapper.toProductResponseDtos(products);
    }


    public PageResponseDto search(SearchingCriteriaDto dto) {
        Pageable pageable = PageRequest.of(dto.getPage(), dto.getSize(), Sort.by("id", "name").ascending());
        Page<@NonNull Product> all = productRepository.findAll(pageable);
        return customMapper.toResponse(all);
    }
}
