package com.api.shoesshop.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.api.shoesshop.entities.WishlistItem;

public interface WishlistItemRepository extends JpaRepository<WishlistItem, Long> {
    Page<WishlistItem> findByAccountId(long accountId, Pageable pageable);

    List<WishlistItem> findByAccountId(long accountId);
}
