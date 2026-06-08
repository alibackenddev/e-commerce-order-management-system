package uz.pdp.repository;

import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.pdp.entity.Order;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<@NonNull Order, @NonNull Long> {

    @Query(value = "select o from Order o left join fetch o.orderItems")
    List<@NonNull Order> findAllOrders();
    @Query(value = "select o from Order o left join fetch o.orderItems where o.customerEmail = :email")
    List<Order> findByEmail(@Param(value = "email") String email);

    @Query(value = "select o from Order o left join fetch o.orderItems where o.customerName = :username")
    List<@NonNull Order> findAllByUsername(@Param(value = "username") String username);

    @Query(value = "select o from Order o where o.customerName = :username and o.id = :orderId")
    Optional<Order> findByUserNameAndOrderId(@Param("username") String username, @Param("orderId") Long orderId);
}
