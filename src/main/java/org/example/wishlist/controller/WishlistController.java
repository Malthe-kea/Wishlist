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
    public String addWish(Model model) {
        WishTagDTO wishTagDTO = new WishTagDTO();
        model.addAttribute("wishdto", wishTagDTO);
        model.addAttribute("avaliableTags", wishlistService.getAvaliableTags());
        return "addWish";
    }

    @PostMapping("/addWish")
    public String addAttraction(@ModelAttribute WishTagDTO wishtagdto, Model model) throws Exception {
        UserWishlistDTO userWishlistDTO = wishlistService.getUserwishlistById(wishtagdto.getWishlist_id());
        wishlistService.addWish(wishtagdto, userWishlistDTO);
        return "redirect:/show-wishlist";
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
        UserWishlistDTO userWishlistDTO = wishlistService.getUserwishlistByUserId(userId);
        System.out.println(userWishlistDTO); //den er null???
        model.addAttribute("userWishlistDTO", userWishlistDTO);
        model.addAttribute("user", wishlistService.getUserNameById(userId));
        model.addAttribute("avaliableTags", wishlistService.getAvaliableTags());
        return "show-wishlist";
    }

    @PostMapping("/{wish_id}/delete")
    public String getWishToDelete(@PathVariable int wish_id, Model model) {
        WishTagDTO wishTagDTO = wishlistService.getWishById(wish_id);
        model.addAttribute("wish", wishTagDTO);
        return "delete-wish";
    }

    @PostMapping("/delete")
    public String deleteWishlistItem(@RequestParam int wish_Id) {
        WishTagDTO w = wishlistService.getWishById(wish_Id);
        if (w != null) {
            wishlistService.deleteDTOWish(wish_Id);
        }
        return "redirect:/";
    }

}
