package uz.pdp;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.crypto.password.PasswordEncoder;
import uz.pdp.entity.AuthUser;
import uz.pdp.entity.Role;
import uz.pdp.enums.RoleName;
import uz.pdp.repository.AuthUserRepository;
import uz.pdp.repository.RoleRepository;

import java.util.List;
import java.util.Set;

@SpringBootApplication
@EnableJpaAuditing
//@OpenAPIDefinition(
//        info = @Info(
//                title = "E-commerce order management system",
//                contact = @Contact(
//                        email = "imomalirabbimqulov@gmail.com",
//                        name = "Rabbimqulov Imomali",
//                        url = ""
//                )
//        ),
//        servers = {
//                @Server(
//                        url = "http://localhost:8080",
//                        description = "Test-Server"
//                ),
//                @Server(
//                        url = "http://localhost:9090",
//                        description = "Production-Server"
//                )
//        }
//)
//@SecuritySchemes(value = {
//        @io.swagger.v3.oas.annotations.security.SecurityScheme(
//                name = "Bearer Authentication",
//                type = SecuritySchemeType.HTTP,
//                scheme = "bearer",
//                bearerFormat = "JWT"
//        )
//})

//        ,
//        @io.swagger.v3.oas.annotations.security.SecurityScheme(
//                name = "basicAuth",
//                type = SecuritySchemeType.HTTP,
//                scheme = "basic"
//        )

public class ECommerceOrderManagementSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(ECommerceOrderManagementSystemApplication.class, args);
    }

    @Bean
    public CommandLineRunner init(
            AuthUserRepository authUserRepository,
            PasswordEncoder passwordEncoder,
            RoleRepository roleRepository) {

        return args -> {
            Role user = roleRepository.findByName(RoleName.ROLE_USER).orElseThrow(() -> new RuntimeException("Role not found"));
            Role admin = roleRepository.findByName(RoleName.ROLE_ADMIN).orElseThrow(() -> new RuntimeException("Role not found"));
            Role manager = roleRepository.findByName(RoleName.ROLE_MANAGER).orElseThrow(() -> new RuntimeException("Role not found"));

            authUserRepository.save(AuthUser.builder()
                    .username("Ali")
                    .password(passwordEncoder.encode("123"))
                    .email("ali@gmail.com")
                    .roles(Set.of(user))
                    .build());

            authUserRepository.save(AuthUser.builder()
                    .username("John")
                    .password(passwordEncoder.encode("123"))
                    .email("john@gmail.com")
                    .roles(Set.of(user))
                    .build());

            authUserRepository.save(AuthUser.builder()
                    .username("George")
                    .password(passwordEncoder.encode("123"))
                    .email("george@gmail.com")
                    .roles(Set.of(user))
                    .build());

            authUserRepository.save(AuthUser.builder()
                    .username("Admin")
                    .password(passwordEncoder.encode("123"))
                    .email("admin@gmail.com")
                    .roles(Set.of(admin))
                    .build());

            authUserRepository.save(AuthUser.builder()
                    .username("Manager")
                    .password(passwordEncoder.encode("123"))
                    .email("manager@gmail.com")
                    .roles(Set.of(manager))
                    .build());
        };
    }

    @Bean
    public OpenApiCustomizer hidePageableSchemas() {
        return openApi -> {
            if (openApi.getComponents() != null &&
                    openApi.getComponents().getSchemas() != null) {

                openApi.getComponents().getSchemas().remove("Pageable");
                openApi.getComponents().getSchemas().remove("PageableObject");
                openApi.getComponents().getSchemas().remove("Sort");
                openApi.getComponents().getSchemas().remove("SortObject");
                openApi.getComponents().getSchemas().remove("Page");
            }
        };
    }


    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("E-commerce Order Management System")
                        .contact(new Contact()
                                .email("imomalirabbbimqulov@gmail.com")
                                .url("https://github.com/alibackenddev")
                                .name("Rabbimqulov Imomali")))
                .servers(List.of(
                        new Server().url("http://localhost:8080").description("Test-Server"),
                        new Server().url("http://localhost:9090").description("Production-Server")))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .components(
                        new Components()
                                .addSecuritySchemes(
                                        "bearerAuth",
                                        new SecurityScheme()
                                                .name("bearerAuth")
                                                .type(SecurityScheme.Type.HTTP)
                                                .scheme("bearer")
                                                .bearerFormat("JWT"))
                );

    }

//    @Bean
//    public GroupedOpenApi groupOrderOpenApi() {
//        return GroupedOpenApi.builder()
//                .group("Orders")
//                .pathsToMatch("/api/orders/**")
//                .build();
//    }
//
//    @Bean
//    public GroupedOpenApi groupProductOpenApi() {
//        return GroupedOpenApi.builder()
//                .group("Product")
//                .pathsToMatch("/api/products/**")
//                .build();
//    }
}
