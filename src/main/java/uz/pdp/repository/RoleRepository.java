package uz.pdp.repository;

import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.pdp.entity.Role;
import uz.pdp.enums.RoleName;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<@NonNull Role,@NonNull Long> {

    @Query(value = "select r from Role r where r.name = :name")
    Optional<Role> findByName(@Param(value = "name") RoleName name);
}
