package org.example.wishlist.controller;

import org.example.wishlist.model.UserWishlistDTO;
import org.example.wishlist.model.WishTagDTO;
import org.example.wishlist.service.WishlistService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class WishlistControllerTest {

    @Mock
    private WishlistService wishlistService;

    @Mock
    private Model model;

    @InjectMocks
    private WishlistController wishlistController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deleteWishlistItem() {
        // Arrange
        int wishTagDTOId = 1;
        int userId = 2;

        WishTagDTO wishTagDTO = new WishTagDTO();
        wishTagDTO.setWish_id(wishTagDTOId);
        wishTagDTO.setWishlist_id(userId);

        UserWishlistDTO userWishlistDTO = new UserWishlistDTO();
        userWishlistDTO.setUser_id(userId);

        when(wishlistService.getWishById(wishTagDTOId)).thenReturn(wishTagDTO);
        when(wishlistService.getUserwishlistById(userId)).thenReturn(userWishlistDTO);

        // Act
        String result = wishlistController.deleteWishlistItem(wishTagDTOId);

        // Assert
        verify(wishlistService).getWishById(wishTagDTOId);
        verify(wishlistService).deleteDTOWish(wishTagDTOId);
        verify(wishlistService).getUserwishlistById(userId);
        assertEquals("redirect:/showallwishes?userId=" + userId, result);
    }

    @Test
    void editWish() {
        //ARRANGE
        int wishTagDTOToEditID = 1;
        int userIdToEdit = 2;

        WishTagDTO wishTagDTOToEdit = new WishTagDTO();
        wishTagDTOToEdit.setWish_id(wishTagDTOToEditID);
        wishTagDTOToEdit.setWishlist_id(userIdToEdit);

        UserWishlistDTO userWishlistDTO = new UserWishlistDTO();
        userWishlistDTO.setUser_id(userIdToEdit);

        when(wishlistService.getWishById(wishTagDTOToEditID)).thenReturn(wishTagDTOToEdit);
        when(wishlistService.getUserwishlistById(userIdToEdit)).thenReturn(userWishlistDTO);

        //ACT
        String result = wishlistController.editWish(wishTagDTOToEdit);

        //ASSERT
        assertEquals("redirect:/showallwishes?userId=" + userIdToEdit, result);
    }
    @Test
    void addWish() {
        //Arrange
        int wishlistId = 1;

        WishTagDTO wishTagDTO = new WishTagDTO();
        wishTagDTO.setWish_id(wishlistId);
        wishTagDTO.setWishlist_id(wishlistId);

        UserWishlistDTO userWishlistDTO = new UserWishlistDTO();
        userWishlistDTO.setUser_id(1);

        when(wishlistService.getAvaliableTags()).thenReturn(new ArrayList<>());
        //Act
        String result = wishlistController.addWish(wishlistId, model);

        //Assert
        verify(wishlistService).getAvaliableTags();
        assertEquals("addWish", result);
    }
}
