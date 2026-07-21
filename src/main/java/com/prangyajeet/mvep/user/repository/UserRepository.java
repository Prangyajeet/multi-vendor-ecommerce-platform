package com.prangyajeet.mvep.user.repository;

import com.prangyajeet.mvep.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import com.prangyajeet.mvep.common.enums.RoleName;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    Optional<User> findByPhoneNumber(String phoneNumber);

    boolean existsByPhoneNumber(String phoneNumber);
    List<User> findByRole_Name(RoleName roleName);

}