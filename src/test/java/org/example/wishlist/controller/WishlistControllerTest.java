package org.example.wishlist.controller;

import org.example.wishlist.model.Wish;
import org.example.wishlist.service.WishlistService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(WishlistController.class)
class WishlistControllerTest {



    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WishlistService wishlistService;

    @Test
    void login() {
    }

    @Test
    void createUser() {
    }

    @Test
    void viewSharedWishlist() {
    }

    @Test
    void addWish() {
    }

    @Test
    void testAddWish() {
    }

    @Test
    void showAllDTOWishes() {
    }

    @Test
    void deleteWishlistItem() {
    }

    @Test
    void editWish() {
    }

    @Test
    void testEditWish() {
    }
}