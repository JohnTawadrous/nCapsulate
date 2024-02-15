package io.ncapsulate.letsbet.repository;

import io.ncapsulate.letsbet.models.ERole;
import io.ncapsulate.letsbet.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(ERole name);
}
