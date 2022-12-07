package com.api.shoesshop.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.shoesshop.entities.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    OrderItem findByOrderIdAndProductVariantId(Long orderId, Long productVaraintId);
}
