package uz.pdp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import uz.pdp.dto.ErrorDto;
import uz.pdp.dto.order_dto.OrderRequestDto;
import uz.pdp.dto.order_dto.OrderResponseDto;
import uz.pdp.dto.order_dto.OrderStatusDto;
import uz.pdp.dto.order_dto.UserOrderUpdateDto;
import uz.pdp.service.CustomUserDetails;
import uz.pdp.service.CustomUserDetailsService;
import uz.pdp.service.OrderService;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@PreAuthorize(value = "isAuthenticated()")
@Validated
@Tag(
        name = "Order-Controller",
        description = "Ushbu Controller user buyurtmalari ustida amallar bajarishlari uchun yaratilgan"
)
public class OrderController {

    private final OrderService orderService;


    @Operation(
            summary = "User buyurtmalari",
            description = "Ushbu api login qilgan user buyurtmalarini olish uchun ishlatiladi"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "User buyurtmalari muvaffaqiyatli olinsa",
                    content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = OrderResponseDto.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User buyurtmalari topilmasa (ushbu user buyurtmalari mavjud bo'lmasa)",
                    content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorDto.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Registratsiyadan o'tmagan user ushbu api ga murojaat qilsa",
                    content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorDto.class)
                            )
                    }
            )

    })
    @GetMapping("/orders/user")
    public ResponseEntity<@NonNull List<OrderResponseDto>> findAllBelongUser() {
        return orderService.findAllOrderBelongUsers();
    }

    @Operation(
            summary = "User buyurtmasi",
            description = "Ushbu api login qilgan user ni bergan ID bo'yicha buyurtmasini olish uchun ishlatiladi"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "User buyurtmasi muvaffaqiyatli olinsa",
                    content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = OrderResponseDto.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Berilgan ID bo'yicha joriy user buyurtmasi mavjud bo'lmasa",
                    content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorDto.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid ID malumot kiritilsa, ConstraintViolationException ushlanadi",
                    content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorDto.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Registratsiyadan o'tmagan user ushbu api ga murojaat qilsa",
                    content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorDto.class)
                            )
                    }
            )

    })
    @GetMapping("/orders/user/{id}")
    public ResponseEntity<@NonNull OrderResponseDto> findByIdBelongUser(
            @PathVariable(name = "id")
            @NotNull
            @Min(value = 1)
            @Parameter(description = "Qidirilayotgan buyurtmaning ID raqami")
            Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        return orderService.findByIdBelongUser(id, userDetails);
    }


    @Operation(
            summary = "Buyurtma qilish",
            description = "Ushbu api login qilgan user lar uchun mahsulot buyurtma qilishlari uchun"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "User buyurtmasi muvaffaqiyatli saqlansa, buyurtma uchun yaratilgan ID qaytariladi",
                    content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Long.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Buyurtma qilingan mahsulot mavjud bo'lmasa, OrderNotFoundException otiladi",
                    content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorDto.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Buyurtma qilingan mahsulot soni yetarlicha bo'lmasa InSufficientStockException otiladi, yoki invalid malumot berib yuborilsa MethodArgumentNotValidException otiladi",
                    content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorDto.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Registratsiyadan o'tmagan user ushbu api ga murojaat qilsa",
                    content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorDto.class)
                            )
                    }
            )

    })
    @PostMapping("/orders")
    public ResponseEntity<@NonNull Long> create(
            @RequestBody
            @Valid
            OrderRequestDto dto) {
        return orderService.create(dto);
    }

    @Operation(
            summary = "Buyurtmani yangilash (UPDATE)",
            description = "Ushbu api joriy user(login user) ni buyurtmasini yangialsh(update) qilish uchun"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "User buyurtmasi muvaffaqiyatli yangilansa, Order successfully updated ko'rinishda String message yuboriladi",
                    content = {
                            @Content(
                                    schema = @Schema(implementation = String.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Yangilanuvchi buyurtma topilmasa yoki buyurtma PENDING statusda bo'lmasa va  mahsulot mavjud bo'lmasa mos ravishda OrderNotFoundException yoki ProductNotFoundException otiladi",
                    content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorDto.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Buyurtma qilingan mahsulot soni yetarlicha bo'lmasa InSufficientStockException otiladi, yoki invalid malumot berib yuborilsa MethodArgumentNotValidException otiladi",
                    content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorDto.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Registratsiyadan o'tmagan user ushbu api ga murojaat qilsa",
                    content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorDto.class)
                            )
                    }
            )

    })
    @PutMapping("/orders/user/update")
    public ResponseEntity<@NonNull String> update(
            //@RequestBody
            @Valid
            UserOrderUpdateDto dto,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return orderService.userUpdate(dto, userDetails);
    }

    @Operation(
            summary = "Buyurtmani o'chirish (DELETE)",
            description = "Ushbu api joriy user(login user) ni buyurtmasini o'chirish (delete) qilish uchun"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "User buyurtmasi muvaffaqiyatli o'chirilsa, 204 status code response qaytariladi"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "O'chiriluvchi buyurtma topilmasa OrderNotFoundException otiladi",
                    content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorDto.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "O'chiriluvchi buyurtma statusi PENDING bo'lmasa o'chirib bo'lmaydi, IllegalStateException otiladi. Shuningdek o'chiriluvchi buyurtma ID invalid bo'lsa ConstraintViolationException otiladi",
                    content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorDto.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Registratsiyadan o'tmagan user ushbu api ga murojaat qilsa",
                    content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorDto.class)
                            )
                    }
            )

    })
    @DeleteMapping("/orders/user/{id}")
    public ResponseEntity<@NonNull Void> delete(
            @PathVariable(name = "id")
            @NotNull
            @Min(value = 1)
            @Parameter(description = "O'chirilishi kerak bo'lgan buyurtmaning ID raqami", required = false)
            Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        orderService.deleteBelongUser(id, userDetails);
        return ResponseEntity.noContent().build();
    }
}