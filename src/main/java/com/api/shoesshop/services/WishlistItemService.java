package com.api.shoesshop.services;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;

import com.api.shoesshop.entities.WishlistItem;

public interface WishlistItemService {
    Page<WishlistItem> findByAccountId(long accountId, Map<String, String> query);

    WishlistItem save(WishlistItem entity);

    List<WishlistItem> findAllByAccountId(long accountId);

    void delete(long id);
}
