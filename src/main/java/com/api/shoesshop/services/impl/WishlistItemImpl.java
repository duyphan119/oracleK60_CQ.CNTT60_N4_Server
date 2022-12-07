package com.api.shoesshop.services.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.api.shoesshop.entities.WishlistItem;
import com.api.shoesshop.repositories.WishlistItemRepository;
import com.api.shoesshop.services.WishlistItemService;
import com.api.shoesshop.utils.Helper;

@Service
public class WishlistItemImpl implements WishlistItemService {

    @Autowired
    private WishlistItemRepository wishlistItemRepository;

    @Override
    public List<WishlistItem> findAllByAccountId(long accountId) {
        return wishlistItemRepository.findByAccountId(accountId);
    }

    @Override
    public Page<WishlistItem> findByAccountId(long accountId, Map<String, String> query) {
        return wishlistItemRepository.findByAccountId(accountId, Helper.getPageable(query));
    }

    @Override
    public WishlistItem save(WishlistItem entity) {
        return wishlistItemRepository.save(entity);
    }

    @Override
    public void delete(long id) {
        wishlistItemRepository.deleteById(id);
    }

}
