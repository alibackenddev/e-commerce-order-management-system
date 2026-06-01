package uz.pdp.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import uz.pdp.enums.Category;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, name = "name")
    private String name;

    @Column(nullable = false, name = "price")
    private BigDecimal price;

    @Column(nullable = false, name = "stock")
    private Integer stock;

    @Column(nullable = false, name = "category")
    @Enumerated(EnumType.STRING)
    private Category category;

    @Column(nullable = false, name = "is_active")
    @Builder.Default
    private Boolean isActive = true;

    @CreatedDate
    @Column(nullable = false, name = "created_at")
    private LocalDateTime createdAt;

}
