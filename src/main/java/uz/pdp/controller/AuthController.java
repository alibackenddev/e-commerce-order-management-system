package uz.pdp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.pdp.dto.ErrorDto;
import uz.pdp.dto.auth_dto.AuthUserRequestDto;
import uz.pdp.dto.auth_dto.TokenRequestDto;
import uz.pdp.service.AuthUserService;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(
        name = "Auth-Controller",
        description = "User register va login uchun controller")
public class AuthController {

    private final AuthUserService authUserService;

    @Operation(
            summary = "Log in",
            description = "Ushbu Api registratsiyadan o'tgan user uchun jwt token berish (login) maqsadida yozilgan",
            method = "POST METHOD",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Request body da json formatda username, password berilishi kerak",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(
                                    implementation = TokenRequestDto.class
                            )
                    )
            )
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Username va Password bo'yicha User authenticated tekshiriladi, va response body sifatida String token beriladi",
                    content = {@Content(
                            schema = @Schema(implementation = String.class)
                    )}
            ),

            @ApiResponse(
                    responseCode = "401",
                    description = "Username va Password bo'yicha mos kelmasa AuthenticationException otiladi",
                    content = {@Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorDto.class)
                    )}
            ),

    })
    @PostMapping("/auth/token")
    public ResponseEntity<@NonNull String> token(
            @RequestBody
            @Valid
            TokenRequestDto dto) {

        return authUserService.login(dto);
    }


    @Operation(
            summary = "Register",
            description = "Userni register qilish uchun api",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Request body da json formatda username, password, email berilishi kerak",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = AuthUserRequestDto.class)
                    )

            )
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Berilgan parameterlar bo'yicha validadsiya qilinib, user register qilinadi",
                    content = {@Content(
                            schema = @Schema(implementation = String.class)
                    )}
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Berilgan parameterlar bo'yicha validadsiya hatolik bersa MethodArgumentNotValidException ",
                    content = {@Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorDto.class)
                    )}
            )
    })
    @PostMapping("/auth/register")
    public ResponseEntity<@NonNull String> register(
            @RequestBody
            @Valid
            AuthUserRequestDto dto) {
        return authUserService.register(dto);
    }
}
