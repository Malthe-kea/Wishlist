package org.example.wishlist.controller;

import org.example.wishlist.SuperheroException;
import org.example.wishlist.model.UserWishlistDTO;
import org.example.wishlist.model.WishTagDTO;
import org.example.wishlist.service.WishlistService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.*;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/")
public class WishlistController {
    //
    private final WishlistService wishlistService;

    public WishlistController(WishlistService wishlistService) {
        this.wishlistService = wishlistService;
    }

    @GetMapping("/")
    public String login(Model model) {
        model.addAttribute("users", wishlistService.getAllUsers());
        model.addAttribute("roles", wishlistService.getAllRoles());
        return "loginpage";
    }

    @GetMapping("/showallwishesAsGiftgiver")
    public String showAllDTOWishesAsGiftGiver(@RequestParam int userId, Model model) throws SuperheroException {
        UserWishlistDTO userWishlistDTO = wishlistService.getUserwishlistByUserId(userId);
        model.addAttribute("userWishlistDTO", userWishlistDTO);
        model.addAttribute("user", wishlistService.getUserNameById(userId));
        model.addAttribute("avaliableTags", wishlistService.getAvaliableTags());
        return "show-wishlist-giftgiver";
    }

    @GetMapping("/login")
    public String login(@RequestParam String role, @RequestParam int userId, Model model) {
        model.addAttribute("role", role);
        model.addAttribute("userId", userId);
        return "redirect:/showallwishes?userId=" + userId;
    }
    @PostMapping("/login")
    public String loginUser(@RequestParam int userId, @RequestParam int role_id, Model model) {
        String roleName = wishlistService.getRoleNameById(role_id);
        if (roleName.equals("giftwisher")) {
            return "redirect:/showallwishes?userId=" + userId;
        } else {
            return "redirect:/showallwishesAsGiftgiver?userId=" + userId;
        }
    }

    @GetMapping("/createUser")
    public String createUser(Model model) {
        UserWishlistDTO userWishlistDTO = new UserWishlistDTO();
        model.addAttribute("userWishlistDTO", userWishlistDTO);
        return "createUser";
    }

    @PostMapping("/saveCreatedUser")
    public String saveCreatedUser(@ModelAttribute UserWishlistDTO userWishlistDTO, Model model, String name) {
        int userid = wishlistService.createUserAndWishlistDTO(name, userWishlistDTO);
        int roleId = 1;
        model.addAttribute("message", "Bruger oprettet! Du kan nu logge ind.");
        return "redirect:/login?userId=" + userid + "&role=" + roleId;
    }


    @ExceptionHandler(SuperheroException.class)
    public String handleError(Model model, Exception exception) {
        model.addAttribute("message", exception.getMessage());
        return "error";
    }

    @GetMapping("/generateShareLink")
    public String generateShareLink(@RequestParam int wishlistId, Model model) {
        // Tjekker "giftwisher" rolle
        UserWishlistDTO userWishlistDTO = wishlistService.getUserwishlistById(wishlistId);
        if (userWishlistDTO != null && "giftwisher".equals(userWishlistDTO.getRole_name())) {
            String shareLink = "http://localhost:8080/viewSharedWishlist?wishlistId=" + wishlistId;
            model.addAttribute("shareLink", shareLink);
            model.addAttribute("userWishlistDTO", userWishlistDTO);
            return "show-wishlist";
        } else {
            model.addAttribute("message", "You do not have permission to share this wishlist.");
            return "error";
        }
    }


    @GetMapping("/viewSharedWishlist")
    public String viewSharedWishlist(@RequestParam int wishlistId, Model model) {
        UserWishlistDTO userWishlistDTO = wishlistService.getUserwishlistById(wishlistId);

        // Tjekker om wishlist exists
        if (userWishlistDTO == null) {
            model.addAttribute("message", "Wishlist not found.");
            return "error";
        }

        model.addAttribute("userWishlistDTO", userWishlistDTO);

        return "show-wishlist-giftgiver";
    }

    @GetMapping("/addWish")
    public String addWish(@RequestParam Integer wishlist_id, Model model) {
        WishTagDTO wishdto = new WishTagDTO();
        if (wishlist_id != null) {
            wishdto.setWishlist_id(wishlist_id);
        } else {
            int defaultWishlistId = 1;
            wishdto.setWishlist_id(defaultWishlistId);
        }
        model.addAttribute("wishdto", wishdto);
        model.addAttribute("avaliableTags", wishlistService.getAvaliableTags());
        return "addWish";
    }


    @PostMapping("/addWish")
    public String addWish(@ModelAttribute WishTagDTO wishtagdto, Model model) {
        if (wishtagdto.getWishlist_id() == 0) {
            return "redirect:/addWish";
        }
        UserWishlistDTO userWishlistDTO = wishlistService.getUserwishlistById(wishtagdto.getWishlist_id());
        if (userWishlistDTO == null || userWishlistDTO.getRole_id() == null) {
            return "addWish";
        }
        wishlistService.addWish(wishtagdto, userWishlistDTO);
        return "redirect:/showallwishes?userId=" + userWishlistDTO.getUser_id();
    }

    @GetMapping("/showallwishes")
    public String showAllDTOWishes(@RequestParam int userId, Model model) throws SuperheroException {
        UserWishlistDTO userWishlistDTO = wishlistService.getUserwishlistByUserId(userId);
        model.addAttribute("userWishlistDTO", userWishlistDTO);
        model.addAttribute("user", wishlistService.getUserNameById(userId));
        model.addAttribute("avaliableTags", wishlistService.getAvaliableTags());
        return "show-wishlist";
    }

    @GetMapping("/{wish_id}/show")
    public String showWish(@PathVariable int wish_id, Model model) {
        WishTagDTO wishTagDTO = wishlistService.getWishById(wish_id);
        if (wishTagDTO == null) {
            throw new RuntimeException("Wish not found for id: " + wish_id);
        }
        model.addAttribute("wishTagDTOId", wishTagDTO.getWish_id());
        model.addAttribute("wishTagDTOP", wishTagDTO);
        model.addAttribute("avaliableTags", wishlistService.getAvaliableTags());
        UserWishlistDTO u = wishlistService.getUserwishlistById(wishTagDTO.getWishlist_id());
        model.addAttribute("userWishlistDTO", u);
        model.addAttribute("userId", u.getUser_id());

        return "show-wish";
    }

    @GetMapping("/{wish_id}/delete")
    public String getWishToDelete(@PathVariable int wish_id, Model model) {
        WishTagDTO wishTagDTO = wishlistService.getWishById(wish_id);
        model.addAttribute("wishTagDTOId", wishTagDTO.getWish_id());
        model.addAttribute("wishTagDTOP", wishTagDTO);
        UserWishlistDTO u = wishlistService.getUserwishlistById(wishTagDTO.getWishlist_id());
        model.addAttribute("userWishlistDTO", u);
        model.addAttribute("userId", u.getUser_id());

        return "delete-wish";
    }

    @PostMapping("/deleteWish")
    public String deleteWishlistItem(@RequestParam int wishTagDTOId) {
        WishTagDTO w = wishlistService.getWishById(wishTagDTOId);
        wishlistService.deleteDTOWish(w.getWish_id());
        UserWishlistDTO u = wishlistService.getUserwishlistById(w.getWishlist_id());

        return "redirect:/showallwishes?userId=" + u.getUser_id();
    }

    @GetMapping("/{wish_id}/edit")
    public String editWish(@PathVariable int wish_id, Model model) {

        WishTagDTO wishTagDTO = wishlistService.getWishById(wish_id);
        model.addAttribute("wishTagDTO", wishTagDTO);
        model.addAttribute("avaliableTags", wishlistService.getAvaliableTags());
        UserWishlistDTO u = wishlistService.getUserwishlistById(wishTagDTO.getWishlist_id());
        model.addAttribute("userWishlistDTO", u);
        model.addAttribute("userId", u.getUser_id());
        model.addAttribute("userWishlistDTOId", wishlistService.getUserwishlistById(wishTagDTO.getWishlist_id()));
        return "edit-wish";
    }

    @PostMapping("/editWish")
    public String editWish(@ModelAttribute WishTagDTO wishTagDTO) {
        WishTagDTO w = wishlistService.getWishById(wishTagDTO.getWish_id());
        UserWishlistDTO u = wishlistService.getUserwishlistById(w.getWishlist_id());
        wishlistService.editDTOwish(wishTagDTO, u);

        return "redirect:/showallwishes?userId=" + u.getUser_id();
    }

    @GetMapping("/deleteuser")
    public String deleteUser(Model model, int userId) {
        model.addAttribute("userId", userId);
        return "delete-user";
    }

    @PostMapping("/deleteuserconfirmation")
    public String deleteUserConfirmation(int userId){
        //TODO skal også have et wishlist_ID og måske også et wish_id. 
        wishlistService.deleteUserFromUserId(userId);
        return "redirect:/";
    }


}
