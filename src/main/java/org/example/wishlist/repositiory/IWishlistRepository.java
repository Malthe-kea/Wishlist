package org.example.wishlist.repositiory;
import org.example.wishlist.model.*;

import java.util.List;

public interface IWishlistRepository {

    //CREATE
    void addWish(WishTagDTO wishTagDTO, UserWishlistDTO uw); //Tilføj et ønske til en ønskeliste

    public UserWishlistDTO getUserwishlistByWishlistId(int wishlist_id);

    public void createUserAndWishlistDTO(String user_name, UserWishlistDTO uw);

    //READ
    public List<User> getAllUsers();

    public String getRoleNameById(int role_id);

    public UserWishlistDTO getUserwishlistByUserId(int user_id);

    public String getUserNameById(int userId);

    public List<Role> getAllRoles();

    List<WishTagDTO> getAllDTOWishes();


    public List<Wish> getWishlistById(int wishlist_id);

    List<Tag> getAvaliableTags();//se alle mulige tags


    List<Tag> getTags(int wish_id); //se tags på et specifikt ønske

    WishTagDTO getWishByID(int wish_id); //Se et specifikt ønske fra ID

    //UPDATE
    void editWish(int wish_id, UserWishlistDTO uw, WishTagDTO w); //editer et specifikt ønske

    //DELETE
    void deleteDTOWish(int wish_id); //slet et ønske

    void giveWish(int wish_id); //giv et ønske


}
