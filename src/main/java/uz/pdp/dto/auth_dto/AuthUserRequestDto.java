package uz.pdp.dto.auth_dto;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(
        name = "AuthUser Request Dto",
        description = "Foydalanuvchi ro'yxatdan o'tish jarayoni uchun data transfer object"
)
public class AuthUserRequestDto {

    @NotBlank
    @Schema(description = "Ro'yxatdan o'tayotgan foydalanuvchining ismi")
    private String username;

    @NotBlank
    @Schema(description = "Ro'yxatdan o'tayotgan foydalanuvchining paroli")
    private String password;

    @NotBlank
    @Pattern(
            regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",
            message = "Email invalid"
    )
    @Schema(description = "Ro'yxatdan o'tayotgan foydalanuvchining emaili")
    private String email;
}