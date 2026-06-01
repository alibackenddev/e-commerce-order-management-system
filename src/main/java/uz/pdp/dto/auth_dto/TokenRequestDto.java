package uz.pdp.dto.auth_dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springdoc.core.annotations.ParameterObject;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "Token Request Dto", description = "Foydalanuvchi token olishi uchun data transfer object")
public class TokenRequestDto {
    @NotBlank
    @Schema(description = "Token oluvchi foydalanuvchining username")
    private String username;

    @NotBlank
    @Schema(description = "Token oluvchi foydalanuvchining paroli")
    private String password;
}
