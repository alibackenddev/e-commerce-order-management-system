package uz.pdp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.SchemaProperty;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import uz.pdp.custom_validator.ProductCategory;
import uz.pdp.dto.ErrorDto;
import uz.pdp.dto.order_dto.OrderResponseDto;
import uz.pdp.dto.page_dto.PageResponseDto;
import uz.pdp.dto.page_dto.PageResponseDto2;
import uz.pdp.dto.product_dto.ProductRequestDto;
import uz.pdp.dto.product_dto.ProductResponseDto;
import uz.pdp.dto.product_dto.SearchingCriteriaDto;
import uz.pdp.enums.Category;
import uz.pdp.service.ProductService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
@Validated
@Tag(
        name = "Product-Controller",
        description = "Ushbu controller mahsulotlar ustida amallar bajarish uchun yaratilingan"
)
public class ProductController {

    private final ProductService productService;


    @Operation(
            summary = "Mahsulotlarni olish",
            description = "Ushbu api barcha mahsulotlarni berilgan page va pageSize bo'yicha pagination qilib olish uchun"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Mahsulotlar muvaffaqiyatli olinsa",
                    content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = PageResponseDto2.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Request parameterlar invalid kiritilsa, ConstraintViolationException otiladi",
                    content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorDto.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "User uchun berilgan token amal qilish muddati tugasa bo'lsa",
                    content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorDto.class)
                            )
                    }
            )

    })
    @GetMapping("")
    public ResponseEntity<@NonNull PageResponseDto2> findAll(
            @RequestParam(name = "pageNumber", required = false, defaultValue = "0")
            @Min(value = 0)
            Integer page,
            @RequestParam(name = "pageSize", required = false, defaultValue = "1")
            @Min(value = 1)
            Integer size) {
        return ResponseEntity
                .ok()
                .body(productService.findAll(page, size));
    }


    @Operation(
            summary = "Mahsulotlarni olish-2",
            description = "Ushbu api barcha mahsulotlarni berilgan page va pageSize bo'yicha pagination, sort qilib olish uchun"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Mahsulotlar muvaffaqiyatli olinsa",
                    content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(
                                            implementation = PageResponseDto.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Request parameterlar invalid kiritilsa, ConstraintViolationException otiladi",
                    content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorDto.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "User uchun berilgan token amal qilish muddati tugasa bo'lsa",
                    content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorDto.class)
                            )
                    }
            )

    })
    @GetMapping("/all")
    public ResponseEntity<@NonNull PageResponseDto> findAll22(
            @RequestParam(name = "pageNumber", required = false, defaultValue = "0")
            @Min(value = 0)
            Integer page,
            @RequestParam(name = "pageSize", required = false, defaultValue = "1")
            @Min(value = 1)
            Integer size) {
        return ResponseEntity
                .ok()
                .body(productService.findAll22(page, size));
    }

    @Operation(
            summary = "Mahsulotlarni olish-3",
            description = "Ushbu api barcha mahsulotlarni default hamda berilgan page va size bo'yicha pagination, sort qilib oladi, agarda o'zingiz page va size belgilamoqchi bo'lsangiz, " +
                    "@RequestParam orqali page hamda size parameterlarga qiymat berishingiz mumkin"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Mahsulotlar muvaffaqiyatli olinsa",
                    content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Page.class)
                                    //schema = @Schema(implementation = ProductResponseDto.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Request parameterlar invalid kiritilsa, ConstraintViolationException otiladi",
                    content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorDto.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "User uchun berilgan token amal qilish muddati tugasa bo'lsa",
                    content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorDto.class)
                            )
                    }
            )

    })
    @GetMapping("/get-all")
    public ResponseEntity<@NonNull Page<@NonNull ProductResponseDto>> findAll2(
            @PageableDefault(page = 0, size = 10, sort = {"id", "name"})
            Pageable pageable) {
        return ResponseEntity
                .ok()
                .body(productService.findAll2(pageable));
    }


    @Operation(
            summary = "Mahsulotni olish",
            description = "Ushbu api mahsulotni berilgan ID raqami bo'yicha oladi"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Mahsulot muvaffaqiyatli olinsa",
                    content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ProductResponseDto.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Request parameterlar invalid kiritilsa, ConstraintViolationException otiladi",
                    content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorDto.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Berilgan ID bo'yicha mahsulot topilmasa OrderNotFoundException otiladi",
                    content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorDto.class)
                            )
                    }
            )

    })
    @GetMapping("/{id}")
    public ResponseEntity<@NonNull ProductResponseDto> findOne(
            @PathVariable(name = "id")
            @Min(value = 1)
            Long id) {
        return productService.findOne(id);
    }


    @Operation(
            summary = "Mahsulotni olish",
            description = "Ushbu api mahsulotni beriladigan nomi va categoriyasi bo'yicha search qilib topadi"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Mahsulot muvaffaqiyatli olinsa",
                    content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ProductResponseDto.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Request parameterlar invalid kiritilsa, ConstraintViolationException otiladi",
                    content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorDto.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Berilgan ID bo'yicha mahsulot topilmasa OrderNotFoundException otiladi",
                    content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorDto.class)
                            )
                    }
            )

    })
    @GetMapping("/search")
    public ResponseEntity<@NonNull List<@NonNull ProductResponseDto>> search1(
            @RequestParam(name = "name", required = false)
            String name,

            @RequestParam(name = "category", required = false)
            @ProductCategory
            Category category
//            ,@RequestParam(required = false)
//            @PageableDefault(size = 10, page = 0, sort = {"id", "name"})
//            Pageable pageable
    ) {


        List<@NonNull ProductResponseDto> list = productService.find(name, category);

        return ResponseEntity.ok().body(list);
    }


    @Operation(
            summary = "Mahsulotni olish",
            description = "Ushbu api mahsulotni beriladigan nomi, categoriyasi, page, size bo'yicha search qilib topadi"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Mahsulot muvaffaqiyatli olinsa",
                    content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ProductResponseDto.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Request parameterlar invalid kiritilsa, ConstraintViolationException otiladi",
                    content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorDto.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Berilgan ID bo'yicha mahsulot topilmasa OrderNotFoundException otiladi",
                    content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorDto.class)
                            )
                    }
            )

    })
    @GetMapping("/search2")
    public ResponseEntity<@NonNull PageResponseDto> search2(
            SearchingCriteriaDto dto) {

        PageResponseDto page = productService.search(dto);
        return ResponseEntity.ok().body(page);
    }


    @Operation(
            summary = "Mahsulot qo'shish",
            description = "Ushbu api @RequestBody da kelgan parameterlar bo'yicha yangi mahsulot qo'shish uchun ishlatiladi"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Mahsulot muvaffaqiyatli qo'shilsa, ushbu mahsulotning unique ID raqami qaytariladi",
                    content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Long.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Request parameterlar invalid kiritilsa yoki unique va primary key qoidasi buzilsa(bir xil nomdagi mahsulot qayta kiritilsa)",
                    content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorDto.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Ushbu apidan faqat ADMIN va MANAGER foydalanishi mumkin",
                    content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorDto.class)
                            )
                    }
            )

    })
    @PreAuthorize(value = "hasAnyRole('ADMIN','MANAGER')")
    @PostMapping("")
    public ResponseEntity<@NonNull Long> create(
            //@RequestBody
            @Valid
            ProductRequestDto dto) {
        return productService.create(dto);
    }


    @Operation(
            summary = "Mahsulotni yangilash",
            description = "Ushbu api @PathVariable orqali berilgan ID bo'yicha @RequestBody ichida kelgan parameterlar yordamida mavjud mahsulotni yangilaydi"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Mahsulot muvaffaqiyatli yangilansa ushbu mahsulotning unique ID raqami qaytariladi",
                    content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Long.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Berilgan ID bo'yicha mahsulot topilmasa ProductNotFoundException hatolik otiladi",
                    content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorDto.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Request parameterlar invalid kiritilsa hatolik otiladi",
                    content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorDto.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Ushbu apidan faqat ADMIN va MANAGER foydalanishi mumkin",
                    content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorDto.class)
                            )
                    }
            )

    })
    @PreAuthorize(value = "hasAnyRole('ADMIN','MANAGER')")
    @PutMapping("/{id}")
    public ResponseEntity<@NonNull Long> update(
            @PathVariable("id")
            @Min(value = 1)
            Long id,
            //@RequestBody
            ProductRequestDto dto) {
        return productService.update(id, dto);
    }


    @Operation(
            summary = "Mahsulotni o'chirish",
            description = "Ushbu api @PathVariable orqali berilgan ID bo'yicha mahsulotni o'chirish uchun"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Mahsulot muvaffaqiyatli o'chirilsa ushbu mahsulot muvaffaqiyatli o'chirildi degan message yuboriladi",
                    content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = String.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Berilgan ID bo'yicha mahsulot topilmasa ProductNotFoundException hatolik otiladi",
                    content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorDto.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Request parameterlar invalid kiritilsa hatolik otiladi",
                    content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorDto.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Ushbu apidan faqat ADMIN va MANAGER foydalanishi mumkin",
                    content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorDto.class)
                            )
                    }
            )

    })
    @PreAuthorize(value = "hasAnyRole('ADMIN','MANAGER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<@NonNull String> delete(
            @PathVariable(name = "id")
            @Min(value = 1)
            Long id) {
        return productService.delete(id);
    }
}
