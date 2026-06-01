package uz.pdp.repository;

import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.pdp.entity.AuthUser;

import java.util.List;
import java.util.Optional;

public interface AuthUserRepository extends JpaRepository<@NonNull AuthUser,@NonNull Long> {

    @Query(value = "select a from AuthUser a where (a.username = :username)")
    Optional<AuthUser> findByUserName(@Param(value = "username") String username);
}
