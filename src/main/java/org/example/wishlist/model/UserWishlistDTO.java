package org.example.wishlist.model;

import java.util.List;

public class UserWishlistDTO { //samler alle informationer omkring en wishlist
    private String name;
    private String wishlist_name;
    private int wishlist_id;
    private int user_id;
    private Integer role_id;
    private String role_name;
    List<WishTagDTO> wishes;

    public UserWishlistDTO(String name, String wishlist_name, int wishlist_id, int user_id, Integer role_id, String role_name, List<WishTagDTO> wishes) {
        this.name = name;
        this.wishlist_name = wishlist_name;
        this.wishlist_id = wishlist_id;
        this.user_id = user_id;
        this.role_id = 1;
        this.role_name = role_name;
        this.wishes = wishes;
    }

    public UserWishlistDTO(){}

    public String getWishlist_name() {
        return wishlist_name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setWishlist_name(String wishlist_name) {
        this.wishlist_name = wishlist_name;
    }

    public int getWishlist_id() {
        return wishlist_id;
    }

    public void setWishlist_id(int wishlist_id) {
        this.wishlist_id = wishlist_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public Integer getRole_id() {
        return role_id;
    }

    public void setRole_id(Integer role_id) {
        this.role_id = role_id;
    }

    public String getRole_name() {
        return role_name;
    }

    public void setRole_name(String role_name) {
        this.role_name = role_name;
    }

    public List<WishTagDTO> getWishes() {
        return wishes;
    }

    public void setWishes(List<WishTagDTO> wishes) {
        this.wishes = wishes;
    }

    @Override
    public String toString() {
        return "UserWishlistDTO{" +
                "User name: " + name +"wishlist_name='" + wishlist_name + '\'' +
                ", wishlist_id=" + wishlist_id +
                ", user_id=" + user_id +
                ", role_id=" + role_id +
                ", role_name='" + role_name + '\'' +
                ", wishes=" + wishes +
                '}';
    }
}
