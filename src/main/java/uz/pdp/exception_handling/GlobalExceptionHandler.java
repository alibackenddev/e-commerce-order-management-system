package uz.pdp.exception_handling;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.HandlerMapping;
import uz.pdp.dto.ErrorDto;
import uz.pdp.enums.OrderStatus;

import java.util.*;
import java.util.function.BiFunction;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(value = ConstraintViolationException.class)
    public ResponseEntity<@NonNull ErrorDto> handleConstraintViolationException(ConstraintViolationException ex, HttpServletRequest request) {
        Map<String, List<String>> map = new HashMap<>();
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            String message = violation.getMessage();
            String field = violation.getPropertyPath().toString();

//            List<String> strings = map.computeIfAbsent(field, key -> new ArrayList<>());
//            strings.add(message);
//            map.compute(field, (k,v) -> new ArrayList<>()).add(message);
            map.compute(field, (BiFunction<String, List<String>, List<String>>) (s, strings) -> {
                if (strings == null)
                    strings = new ArrayList<>();
                strings.add(message);
                return strings;
            });
        }
        ErrorDto error = ErrorDto.builder()
                .error_message(map.toString())
                .error_path(request.getRequestURI())
                .error_code(HttpStatus.BAD_REQUEST.value())
                .build();
        log.error(error.toString(), ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResponseEntity<@NonNull ErrorDto> handleException(HttpServletRequest req, MethodArgumentNotValidException e) {
        String error_path = req.getRequestURI();

        List<FieldError> fieldErrors = e.getFieldErrors();
        Map<String, List<String>> errors = getStringListMap(fieldErrors);
        ErrorDto errorDto = ErrorDto.builder()
                .error_message(errors.toString())
                .error_path(error_path)
                .error_code(HttpStatus.BAD_REQUEST.value())
                .build();
        log.error(errorDto.toString(), e);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errorDto);
    }

    @ExceptionHandler(value = {
            ItemNotFoundException.class,
            OrderNotFoundException.class,
            ProductNotFoundException.class})
    public ResponseEntity<@NonNull ErrorDto> notFoundException(HttpServletRequest req, Exception e) {
        String message = e.getMessage();
        String error_path = req.getRequestURI();
        ErrorDto errorDto = ErrorDto.builder()
                .error_path(error_path)
                .error_code(HttpStatus.NOT_FOUND.value())
                .error_message(message)
                .build();
        log.error(errorDto.toString(), e);
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND.value())
                .body(errorDto);
    }

    @ExceptionHandler(value = {
            IllegalStateException.class,
            DataIntegrityViolationException.class,
            InSufficientStockException.class})
    public ResponseEntity<@NonNull ErrorDto> badRequest(HttpServletRequest req, Exception e) {
        String message = e.getMessage();
        String error_path = req.getRequestURI();
        ErrorDto errorDto = ErrorDto.builder()
                .error_path(error_path)
                .error_code(HttpStatus.BAD_REQUEST.value())
                .error_message(message)
                .build();
        log.error(errorDto.toString(), e);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST.value())
                .body(errorDto);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> handleEnumError(HttpMessageNotReadableException ex, HttpServletRequest request) {
        @SuppressWarnings("unchecked")
        Map<String, String>  attribute = (Map<String, String> ) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        String id = attribute.get("id");


        if (request.getRequestURI().equals("/api/orders/%s/status".formatted(id))) {
            return ResponseEntity
                    .badRequest()
                    .body(Collections.singletonMap("error_message",
                    "Status noto'g'ri. Mumkin bo'lgan qiymatlar: "
                            + Arrays.toString(OrderStatus.values())));
        }
        log.error(ex.getMessage(), ex);
        return ResponseEntity.badRequest().body("Hato malumot kiritildi");
    }


    private static Map<String, List<String>> getStringListMap(List<FieldError> fieldErrors) {
        Map<String, List<String>> errors = new HashMap<>();
        for (FieldError fieldError : fieldErrors) {
            String field = fieldError.getField();
            String defaultMessage = fieldError.getDefaultMessage();

            errors.compute(field, new BiFunction<String, List<String>, List<String>>() {
                @Override
                public List<String> apply(String s, List<String> strings) {
                    if (strings == null) {
                        strings = new ArrayList<>();
                    }
                    strings.add(defaultMessage);
                    return strings;
                }
            });
        }
        return errors;
    }
}
