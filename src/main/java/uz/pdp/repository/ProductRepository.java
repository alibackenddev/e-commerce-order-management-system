package uz.pdp.repository;

import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.pdp.entity.Product;

import java.util.List;

public interface ProductRepository extends JpaRepository<@NonNull Product, @NonNull Long> {

    //@Query(value = "select p from Product p where (/*:name is null or*/ lower(p.name) like lower(concat('%', :name, '%'))) or (/*:category is null or */lower(p.category) = lower(:category))")
    @Query(value = "select p from Product p where (lower(p.name) like lower(concat('%', :name, '%'))) or (lower(p.category) = lower(:category))")
    List<@NonNull Product> find(
            @Param(value = "name") String name,
            @Param(value = "category") String category
    );
}
