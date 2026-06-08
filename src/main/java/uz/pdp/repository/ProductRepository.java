package uz.pdp.repository;


import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import uz.pdp.entity.Product;

public interface ProductRepository extends
        JpaRepository<@NonNull Product, @NonNull Long>,
        JpaSpecificationExecutor<@NonNull Product> {

    //@Query(value = "select p from Product p where (:name is null or lower(p.name) like lower(concat('%', :name, '%'))) and (:category is null or p.category = :category)")
}
