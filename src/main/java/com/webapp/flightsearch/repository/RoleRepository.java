package com.webapp.flightsearch.repository;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.webapp.flightsearch.entity.Role;


public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByName(String name);
}