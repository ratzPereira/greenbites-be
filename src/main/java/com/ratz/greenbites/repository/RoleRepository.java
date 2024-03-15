package com.ratz.greenbites.repository;

import com.ratz.greenbites.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    String SELECT_ROLE_BY_ID_QUERY = "SELECT r.id, r.name, r.permission FROM Roles r JOIN User_Roles ur ON ur.role_id = r.id JOIN Users u ON u.id = ur.user_id WHERE u.id = :userId";

    @Query(value = SELECT_ROLE_BY_ID_QUERY, nativeQuery = true)
    Role getRoleByPlayerId(@Param("userId") Long userId);

    Optional<Role> findByName(String name);
}
