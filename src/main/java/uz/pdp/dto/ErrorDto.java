package uz.pdp.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Schema(name = "Error Dto", description = "Errorlarni ifodalash uchun data transfer object")
public class ErrorDto {

    //private Map<String, List<String>> error_message;
    @Schema(description = "Hatolik matni")
    private String error_message;

    @Schema(description = "Hatolik code")
    private int error_code;

    @Schema(description = "Hatolik sodir bo'lgan api")
    private String error_path;

    @Builder.Default
    @Schema(description = "Hatolik sodir bo'lgan vaqt")
    private LocalDateTime timestamp =  LocalDateTime.now();

}
