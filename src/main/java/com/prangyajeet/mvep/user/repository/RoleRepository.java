package com.prangyajeet.mvep.user.repository;

import com.prangyajeet.mvep.common.enums.RoleName;
import com.prangyajeet.mvep.user.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(RoleName name);

}