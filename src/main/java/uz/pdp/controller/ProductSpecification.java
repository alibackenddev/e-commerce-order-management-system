package uz.pdp.controller;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.data.jpa.domain.Specification;
import uz.pdp.dto.product_dto.SearchingCriteriaDto;
import uz.pdp.entity.Product;
import uz.pdp.enums.Category;

import java.util.ArrayList;
import java.util.List;

public class ProductSpecification {

    private ProductSpecification() {
    }

    public static Specification<@NonNull Product> filter(String name, Category category) {
        return new Specification<>() {
            @Override
            public @Nullable Predicate toPredicate(@NotNull Root<Product> root, @NotNull CriteriaQuery<?> query, @NotNull CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();

                if (name != null) {
                    predicates.add(
                            cb.like(
                                    cb.lower(root.get("name")), "%" + name.toLowerCase() + "%"
                            )
                    );
                }

                if (category != null) {
                    predicates.add(cb.equal(root.get("category"), category.toString()));
                }

                return cb.and(predicates.toArray(new Predicate[0]));
            }
        };
    }
}
