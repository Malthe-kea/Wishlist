package org.example.wishlist.model;

public class Wish {
    private String wish_name;
    private String description;
    private int price;
    private int wishlist_id;
    private int role_id;
    private int user_id;
    private int wish_id;

    public Wish(String wish_name, String description, int price, int wishlist_id, int role_id, int user_id, int wish_id) {
        this.wish_name = wish_name;
        this.description = description;
        this.price = price;
        this.wishlist_id = wishlist_id;
        this.role_id = role_id;
        this.user_id = user_id;
        this.wish_id = wish_id;
    }

    public String getWish_name() {
        return wish_name;
    }

    public void setWish_name(String wish_name) {
        this.wish_name = wish_name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getWishlist_id() {
        return wishlist_id;
    }

    public void setWishlist_id(int wishlist_id) {
        this.wishlist_id = wishlist_id;
    }

    public int getRole_id() {
        return role_id;
    }

    public void setRole_id(int role_id) {
        this.role_id = role_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getWish_id() {
        return wish_id;
    }

    public void setWish_id(int wish_id) {
        this.wish_id = wish_id;
    }

    @Override
    public String toString() {
        return "Wish{" +
                "wish_name='" + wish_name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", wishlist_id=" + wishlist_id +
                ", role_id=" + role_id +
                ", user_id=" + user_id +
                ", wish_id=" + wish_id +
                '}';
    }
}
