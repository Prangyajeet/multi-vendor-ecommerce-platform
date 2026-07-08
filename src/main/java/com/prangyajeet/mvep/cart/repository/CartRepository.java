package com.prangyajeet.mvep.cart.repository;

import com.prangyajeet.mvep.cart.entity.Cart;
import com.prangyajeet.mvep.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {

    List<Cart> findByUser(User user);

    List<Cart> findByUserId(Long userId);

    Optional<Cart> findByUserIdAndProductId(
            Long userId,
            Long productId
    );

    @Transactional
    @Modifying
    void deleteByUserId(Long userId);

    @Transactional
    void deleteAllByUser(User user);
}