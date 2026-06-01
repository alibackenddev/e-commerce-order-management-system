package uz.pdp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import uz.pdp.dto.ErrorDto;
import uz.pdp.dto.order_dto.OrderResponseDto;
import uz.pdp.dto.order_dto.OrderStatusDto;
import uz.pdp.exception_handling.OrderNotFoundException;
import uz.pdp.service.OrderService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/api")
@PreAuthorize(value = "hasAnyRole('ADMIN','MANAGER')")
@Tag(
        name = "Admin-Controller",
        description = "Ushbu Controller ADMIN va MANAGER tomonidan orderlar ustida amallar bajarish uchun yaratilingan"
)

public class AdminOrderController {

    private final OrderService orderService;


    @Operation(
            summary = "Orderlarni topadigan api",
            description = "Ushbu api databasedagi barcha orderlarni olib beradi, List ko'rinishida"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Serverdagi barcha orderlar olinadi",
                    content = {@Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = OrderResponseDto.class))}
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Not found exception - ushlanadi",
                    content = {@Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorDto.class)
                    )}
            )
    })
    @GetMapping("/orders")
    public ResponseEntity<@NonNull List<OrderResponseDto>> findAll() {
        return orderService.findAll();
    }


    @Operation(
            summary = "Orderni topadigan api",
            description = "Ushbu api order id bo'yicha databasedan orderni olib beradi"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Serverdagi order olinadi",
                    content = {@Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = OrderResponseDto.class))}
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "OrderNotFoundException exception - ushlanadi, agar mavjud bo'lmagan id kiritilsa",
                    content = {@Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorDto.class)
                    )}
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "ConstraintViolationException exception- ushlanadi, agar id invalid kiritilsa",
                    content = {@Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorDto.class)
                    )}
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "AuthenticationException exception- ushlanadi, agar user authenticated bo'lmasa",
                    content = {@Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorDto.class)
                    )}
            )
    })
    @GetMapping("/orders/{id}")
    public ResponseEntity<@NonNull OrderResponseDto> findById(
            @PathVariable(name = "id")
            @Min(value = 1)
            @Parameter(description = "Qidirilayotgan buyurtmaning ID raqami")
            Long id) {
        return orderService.findById(id);
    }

    @Operation(
            summary = "Orderni update qiladigan api",
            description = "Ushbu api order id bo'yicha orderni update qiladi"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Request successful bajariladi, Response body da hechnarsa bo'lmaydi, HttpStatus 204 bo'ladi"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "OrderNotFoundException exception - ushlanadi, agar mavjud bo'lmagan id kiritilsa",
                    content = {@Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorDto.class)
                    )}
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "ConstraintViolationException yoki MethodArgumentNotValidException exception- ushlanadi, mos ravishda invalid id kiritilsa yoki request body invalid kiritilsa",
                    content = {@Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorDto.class)
                    )}
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "AuthenticationException exception- ushlanadi, agar user authenticated bo'lmasa",
                    content = {@Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorDto.class)
                    )}
            )
    })
    @PutMapping("/orders/{id}/status")
    public ResponseEntity<@NonNull Void> update(
            @PathVariable(name = "id")
            @Min(value = 1)
            @Parameter(description = "Yangilanishi kerak bo'lgan buyurtmaning ID raqami")
            Long id,
            @RequestBody
            @Valid OrderStatusDto dto) {
        orderService.update(id, dto.getStatus());
        return ResponseEntity.noContent().build();
    }


    @Operation(
            summary = "Orderni delete qiladigan api",
            description = "Ushbu api order @PathVariable da keluvchi id bo'yicha orderni topadi va delete qiladi"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Request successful bajariladi, Response body da hechnarsa bo'lmaydi, HttpStatus 204 bo'ladi"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "OrderNotFoundException - ushlanadi, agar mavjud bo'lmagan id kiritilsa",
                    content = {@Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorDto.class)
                    )}
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "ConstraintViolationException - ushlanadi,  invalid id kiritilsa",
                    content = {@Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorDto.class)
                    )}
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "AuthenticationException - ushlanadi, agar user authenticated bo'lmasa",
                    content = {@Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorDto.class)
                    )}
            )
    })
    @DeleteMapping("/orders/{id}")
    public ResponseEntity<@NonNull Void> delete(
            @PathVariable(name = "id")
            @NotNull
            @Parameter(description = "O'chirilishi kerak bo'lgan buyurtmaning ID raqami")
            Long id) {
        orderService.delete(id);
        return ResponseEntity.noContent().build();
    }


    @Operation(
            summary = "Orderlarni topadigan api",
            description = "Ushbu api @PathVariable da keluvchi haridor email bo'yicha orderlarni topadi"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Request successful bajariladi, Response body da Orderlarni List keladi",
                    content = {@Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = OrderResponseDto.class)
                    )}
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "OrderNotFoundException - ushlanadi, agar mavjud bo'lmagan email kiritilsa",
                    content = {@Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorDto.class)
                    )}
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "ConstraintViolationException - ushlanadi, invalid email kiritilsa",
                    content = {@Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorDto.class)
                    )}
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "AuthenticationException - ushlanadi, agar user authenticated bo'lmasa",
                    content = {@Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorDto.class)
                    )}
            )
    })
    @GetMapping("/orders/customer/{email}")
    public ResponseEntity<@NonNull List<OrderResponseDto>> customerOrders(
            @PathVariable(name = "email")
            @Pattern(
                    regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",
                    message = "Email invalid"
            )
            @Parameter(description = "Buyurtmalari qidirilayotgan foydalanuvchining emaili")
            String email) {

        return ResponseEntity
                .ok()
                .body(orderService.findOrdersOfCustomer(email));
    }
}
