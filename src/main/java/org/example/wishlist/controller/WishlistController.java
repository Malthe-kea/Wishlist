package org.example.wishlist.controller;

import org.example.wishlist.model.UserWishlistDTO;

import org.example.wishlist.model.Wish;
import org.example.wishlist.model.WishTagDTO;
import org.example.wishlist.service.WishlistService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.*;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
    public String welcome(Model model) {
        model.addAttribute("users", wishlistService.getAllUsers());
        model.addAttribute("roles", wishlistService.getAllRoles());
        //tjekker om der er bedsked i model
        if (model.containsAttribute("message")) {
            model.addAttribute("message", model.getAttribute("message"));
        }
        return "loginpage";
    }

    @GetMapping("/createUser")
    public String createUser(Model model) {
        UserWishlistDTO userWishlistDTO = new UserWishlistDTO();
        model.addAttribute("userWishlistDTO", userWishlistDTO);
        return "createUser";
    }

    @PostMapping("/saveCreatedUser")
    public String saveCreatedUser(@ModelAttribute UserWishlistDTO userWishlistDTO, Model model, String name) {
        wishlistService.createUserAndWishlistDTO(name, userWishlistDTO);
        // Tilføj en besked til model
        model.addAttribute("message", "Bruger oprettet! Du kan nu logge ind.");
        return "redirect:/login?userId=" + userWishlistDTO.getUser_id() + "&role=" + userWishlistDTO.getRole_id();
    }

    @GetMapping("/addWish")
    public String addWish(@RequestParam(required = false) Integer wishlistId, Model model) {
        WishTagDTO wishdto = new WishTagDTO();
        if (wishlistId != null) {
            wishdto.setWishlist_id(wishlistId);
        } else {
            int defaultWishlistId = 1; //skal slettes senere
            wishdto.setWishlist_id(defaultWishlistId);
        }

        model.addAttribute("wishdto", wishdto);
        model.addAttribute("avaliableTags", wishlistService.getAvaliableTags());
        return "addWish";
    }


    @PostMapping("/addWish")
    public String addWish(@ModelAttribute WishTagDTO wishtagdto, Model model) {
        if (wishtagdto.getWishlist_id() == 0) {
            return "addWish";
        }
        System.out.println("Wishlist ID: " + wishtagdto.getWishlist_id()); // Log wishlist_id
        UserWishlistDTO userWishlistDTO = wishlistService.getUserwishlistById(wishtagdto.getWishlist_id());
        if (userWishlistDTO == null || userWishlistDTO.getRole_id() == null) {
            return "addWish";
        }
        wishlistService.addWish(wishtagdto, userWishlistDTO);
        return "redirect:/showallwishes?userId=" + userWishlistDTO.getUser_id();
    }

    @GetMapping("/login")
    public String login(@RequestParam String role, @RequestParam int userId, Model model) {
        System.out.println("Received userId: " + userId);
        System.out.println("Received role: " + role);
        model.addAttribute("role", role);
        model.addAttribute("userId", userId);
        return "redirect:/showallwishes?userId=" + userId + "&role=" + role;
    }


    @GetMapping("/showallwishes")
    public String showAllDTOWishes(@RequestParam int userId, Model model) {
        System.out.println("show all wishes has userid: " + userId);
        System.out.println(userId);
        UserWishlistDTO userWishlistDTO = wishlistService.getUserwishlistByUserId(userId);
        System.out.println(userWishlistDTO); //den er null???
        model.addAttribute("userWishlistDTO", userWishlistDTO);
        model.addAttribute("user", wishlistService.getUserNameById(userId));
        model.addAttribute("avaliableTags", wishlistService.getAvaliableTags());
        return "show-wishlist";
    }

    @PostMapping("/{wish_id}/show")
    public String showWish(@PathVariable int wish_id, Model model) {
        WishTagDTO wishTagDTO = wishlistService.getWishById(wish_id);
        if (wishTagDTO == null) {
            throw new RuntimeException("Wish not found for id: " + wish_id);
        }
        model.addAttribute("wishTagDTOId", wishTagDTO.getWish_id());
        model.addAttribute("wishTagDTOP", wishTagDTO);
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

        if(wishTagDTO == null){
            throw new RuntimeException("wish is null");
        }
        return "delete-wish";
    }

    @PostMapping ("/deleteWish")
    public String deleteWishlistItem(@RequestParam int wishTagDTOId) {
        WishTagDTO w = wishlistService.getWishById(wishTagDTOId);
        wishlistService.deleteDTOWish(w.getWish_id());
       UserWishlistDTO u = wishlistService.getUserwishlistById(w.getWishlist_id());

       return "redirect:/showallwishes?userId=" + u.getUser_id();
    }

}
