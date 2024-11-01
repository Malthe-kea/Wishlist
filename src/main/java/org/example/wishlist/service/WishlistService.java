package org.example.wishlist.service;


import org.example.wishlist.model.*;

import org.example.wishlist.model.UserWishlistDTO;

import org.example.wishlist.repositiory.IWishlistRepository;
import org.example.wishlist.repositiory.WishtlistRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WishlistService {

 private final IWishlistRepository wishlistRepository;


    public WishlistService(ApplicationContext context, @Value("${department.repository.impl}") String impl) {
        wishlistRepository = (IWishlistRepository) context.getBean(impl);
    }
    public UserWishlistDTO getUserwishlistById(int wishlist_id) {
        return wishlistRepository.getUserwishlistByWishlistId(wishlist_id);
    }
    public List<User> getAllUsers() {
        return wishlistRepository.getAllUsers();
    }
    public UserWishlistDTO getUserwishlistByUserId(int user_id) {
        return wishlistRepository.getUserwishlistByUserId(user_id);
    }

    public List<Role> getAllRoles() {
        return wishlistRepository.getAllRoles();
    }

    public List<WishTagDTO> getAllDTOWishes() {
        return wishlistRepository.getAllDTOWishes();
    }

    public void deleteDTOaWishlistItem(int wish_id) {
        wishlistRepository.deleteDTOWish(wish_id);
    }

    public void addWish(WishTagDTO w, UserWishlistDTO uw) {
        wishlistRepository.addWish(w, uw);
    }

    public List<Tag> getAvaliableTags() {
        return wishlistRepository.getAvaliableTags();
    }
}
