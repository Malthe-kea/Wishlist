package org.example.wishlist.repositiory;

import org.example.wishlist.model.Tag;
import org.example.wishlist.model.User;
import org.example.wishlist.model.Role;
import org.example.wishlist.model.UserWishlistDTO;
import org.example.wishlist.model.Wish;
import org.example.wishlist.model.WishTagDTO;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.sql.*;


@Repository("DEPARTMENT_REPOSITORY")
@Lazy
public class WishlistRepository implements IWishlistRepository {
    private static final Logger logger = LoggerFactory.getLogger(WishlistRepository.class);

    @Value("${spring.datasource.url}")
    private String dbUrl;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    public WishlistRepository() {
    }

    @Override
    public List<User> getAllUsers() {
        List<User> allUsers = new ArrayList<>();
        String sqlString = "SELECT * FROM user";
        try (Connection con = DriverManager.getConnection(dbUrl.trim(), username.trim(), password.trim())) {
            Statement statement = con.createStatement();
            ResultSet resultSet = statement.executeQuery(sqlString);
            while (resultSet.next()) {
                String name = resultSet.getString("name");
                int id = resultSet.getInt("user_id");
                allUsers.add(new User(id, name));
            }
        } catch (SQLException e) {
            logger.error("SQL exception occurred", e);
        }
        return allUsers;
    }

    @Override
    public List<Role> getAllRoles() {
        List<Role> allRoles = new ArrayList<>();
        String sqlString = "SELECT * FROM role";
        try (Connection con = DriverManager.getConnection(dbUrl.trim(), username.trim(), password.trim())) {
            Statement statement = con.createStatement();
            ResultSet resultSet = statement.executeQuery(sqlString);
            while (resultSet.next()) {
                String name = resultSet.getString("role_name");
                int id = resultSet.getInt("role_id");
                allRoles.add(new Role(id, name));
            }
        } catch (SQLException e) {
            logger.error("SQL exception occurred", e);
        }
        return allRoles;
    }

    @Override
    public List<Tag> getAvaliableTags() {
        List<Tag> avaliableTags = new ArrayList<>();
        String sqlString = "SELECT * FROM tag";
        try (Connection con = DriverManager.getConnection(dbUrl.trim(), username.trim(), password.trim())) {

            Statement statement = con.createStatement();
            ResultSet resultSet = statement.executeQuery(sqlString);

            while (resultSet.next()) {
                String name = resultSet.getString("tag_name");
                int tagId = resultSet.getInt("tag_id");
                avaliableTags.add(new Tag(name, tagId));
            }

        } catch (SQLException e) {
            logger.error("SQL exception occurred", e);
        }
        return avaliableTags;
    }

    @Override
    public UserWishlistDTO getUserwishlistByWishlistId(int wishlist_id) {
        String sqlString = "SELECT t.wishlist_name, t.wishlist_id, t.user_id, t.role_id, r.role_name, u.name " +
                "FROM wishlist t " +
                "JOIN role r ON r.role_id = t.role_id " +
                "JOIN user u ON u.user_id = t.user_id " + // JOIN referere til u.name
                "WHERE t.wishlist_id = ?";
        String sqlwishes = "SELECT wish_name, description, price, wish_id, wishlist_id FROM wish WHERE wishlist_id=?";
        String sqlTags = "SELECT tag_id FROM wish_tag WHERE wish_id=?";

        List<Integer> tagIds = new ArrayList<>();
        UserWishlistDTO userWishlistDTO = null;


        try (Connection con = DriverManager.getConnection(dbUrl.trim(), username.trim(), password.trim())) {
            PreparedStatement statement = con.prepareStatement(sqlString);
            statement.setInt(1, wishlist_id);

            PreparedStatement statement2 = con.prepareStatement(sqlwishes);
            statement.setInt(1, wishlist_id);

            PreparedStatement statement3 = con.prepareStatement(sqlTags);

            ResultSet resultSet = statement.executeQuery();
            ResultSet resultSet2 = statement2.executeQuery();

            if (resultSet.next()) {
                String name = resultSet.getString("name");
                String wishlist_name = resultSet.getString("wishlist_name");
                int list_id = resultSet.getInt("wishlist_id");
                int user_id = resultSet.getInt("user_id");
                int role_id = resultSet.getInt("role_id");
                String role_name = resultSet.getString("role_name");
                System.out.println("list id fra getuserwishlistbyuser id first sql statement" + list_id);

                ArrayList<WishTagDTO> wishTagDTOS = new ArrayList<>();
                List<Integer> tags = new ArrayList<>();
                while (resultSet2.next()) {
                    String wish_name = resultSet2.getString("wish_name");
                    String description = resultSet.getString("description");
                    int price = resultSet2.getInt("price");
                    int wish_id = resultSet2.getInt("wish_id");
                    statement3.setInt(1, wish_id);
                    ResultSet resultSet3 = statement3.executeQuery();
                    while (resultSet3.next()) {
                        int tagid = resultSet3.getInt("tag_id");
                        tags.add(tagid);
                    }
                    wishTagDTOS.add(new WishTagDTO(wish_name, description, price, wish_id, tags, wishlist_id));
                }
                userWishlistDTO = new UserWishlistDTO(name, wishlist_name, list_id, user_id, role_id, role_name, wishTagDTOS);
            }

        } catch (SQLException e) {
            logger.error("SQL exception occurred", e);
        }

        return userWishlistDTO;
    }


    @Override
    public void addWish(WishTagDTO w, UserWishlistDTO uw) {
        String sqlString = "INSERT INTO wish(wish_name, description, price, wishlist_id, role_id, user_id, wish_id) VALUES(?,?,?,?,?,?,?)";
        String sqlTags = "INSERT INTO wish_tag(tag_id, wish_id) VALUES(?,?)";
        try (Connection con = DriverManager.getConnection(dbUrl.trim(), username.trim(), password.trim())) {
            System.out.println(dbUrl + " " + username + " " + password);

            PreparedStatement statement = con.prepareStatement(sqlString, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, w.getWish_name());
            statement.setString(2, w.getDescription());
            statement.setDouble(3, w.getPrice());
            statement.setDouble(4, uw.getWishlist_id());
            statement.setDouble(5, uw.getRole_id());
            statement.setDouble(6, uw.getUser_id());
            statement.setDouble(7, w.getWish_id());
//                System.out.println("SQL query: " + sqlString);
            statement.executeUpdate();
            ResultSet rs = statement.getGeneratedKeys();
            if (rs.next()) {
                int wish_id = rs.getInt(1);
                PreparedStatement statementTags = con.prepareStatement(sqlTags);
                for (int tag_id : w.getTagIds()) {
                    statementTags.setInt(1, tag_id);
                    statementTags.setInt(2, wish_id);
                    statementTags.executeUpdate();
                }
            }

        } catch (SQLException e) {
            logger.error("SQL exception occurred", e);
        }
    }


    @Override
    public List<WishTagDTO> getAllDTOWishes() {
        List<WishTagDTO> wishes = new ArrayList<>();
        String sqlString = "SELECT wish_name, description, price, wish_id, user_id, role_id, wishlist_id FROM wish";
        String sqlString2 = "SELECT tag_id FROM wish_tag WHERE wish_id = ?";

        try (Connection connection = DriverManager.getConnection(dbUrl.trim(), username.trim(), password.trim())) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sqlString);

            while (resultSet.next()) {
                String wishName = resultSet.getString("wish_name");
                String description = resultSet.getString("description");
                int price = resultSet.getInt("price");
                int wishId = resultSet.getInt("wish_id");
                int userId = resultSet.getInt("user_id");
                int role_id = resultSet.getInt("role_id");
                int wishlistId = resultSet.getInt("wishlist_id");

                PreparedStatement preparedStatement = connection.prepareStatement(sqlString2);
                preparedStatement.setInt(1, wishId);
                ResultSet resultSetTags = preparedStatement.executeQuery();

                // Liste til tags for dette specifikke ønske
                List<Integer> wishTags = new ArrayList<>();
                while (resultSetTags.next()) {
                    wishTags.add(resultSetTags.getInt("tag_id"));
                }

                // Opret DTO for ønske med tilknyttet tags
                WishTagDTO dto = new WishTagDTO(wishName, description, price, wishId, wishTags, wishlistId);

                dto.setTagIds(wishTags);
                wishes.add(dto);

            }
        } catch (SQLException e) {
            logger.error("SQL exception occurred", e);
        }
        return wishes;
    }


    @Override
    public List<Wish> getWishlistById(int wishlist_id) {
        return List.of();
    }


    @Override
    public List<Tag> getTags(int wish_id) {
        List<Tag> tags = new ArrayList<>();
        String sqlString = "SELECT t.tag_id, t.tag_name FROM tag t JOIN wish_tag wt ON t.tag_id = wt.tag_id WHERE wt.wish_id = ?";
        try (Connection connection = DriverManager.getConnection(dbUrl.trim(), username.trim(), password.trim())) {
            PreparedStatement preparedStatement = connection.prepareStatement(sqlString);
            preparedStatement.setInt(1, wish_id);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int tagId = resultSet.getInt("tag_id");
                String tagName = resultSet.getString("tag_name");
                tags.add(new Tag(tagName, tagId));
            }
        } catch (SQLException e) {
            logger.error("SQL exception occurred", e);
        }

        return tags;
    }


    @Override
    public WishTagDTO getWishByID(int wish_id) {
        String sqlStringWish = "SELECT w.name, w.description, w.price, w.wishlist_id, w.role_id, w.user_id, w.wish_id FROM wish w WHERE w.wish_id = ?";
        String sqlStringTag = "SELECT t.tag_id, t.tag_name FROM tag t JOIN wish_tag wt ON t.tag_id = wt.tag_id WHERE wt.wish_id = ?";

        WishTagDTO wishTagDTO = null;
        Wish wish = null;
        List<Integer> tagIDList = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(dbUrl.trim(), username.trim(), password.trim())) {
            PreparedStatement statement = connection.prepareStatement(sqlStringWish);
            PreparedStatement statement2 = connection.prepareStatement(sqlStringTag);

            statement.setInt(1, wish_id);
            statement2.setInt(1, wish_id);

            ResultSet resultSet = statement.executeQuery();
            ResultSet resultSet2 = statement2.executeQuery();
            if (resultSet.next()) {
                String wishName = resultSet.getString("wish_name");
                String description = resultSet.getString("wish_description");
                int price = resultSet.getInt("price");
                int wishlist_id = resultSet.getInt("wishlist_id");
                int role_id = resultSet.getInt("role_id");
                int user_id = resultSet.getInt("user_id");
                int wishid = resultSet.getInt("wish_id");

                while (resultSet2.next()) {
                    int tagid = resultSet2.getInt("tag_id");
                    tagIDList.add(tagid);

                    wishTagDTO = new WishTagDTO(wishName, description, price, wishid, tagIDList, wishlist_id);
                }
            }

        } catch (SQLException e) {
            logger.error("SQL exception occured", e);

        }
        return wishTagDTO;
    }

    @Override
    public void editWish(int wish_id) {

    }

    @Override
    public void deleteDTOWish(int id) {
        String sqlStringTag = "DELETE FROM wish_tag WHERE tag_id = ?";
        String sqlStringWish = "DELETE FROM wish WHERE wish_id = ?";
        try (Connection connection = DriverManager.getConnection(dbUrl.trim(), username.trim(), password.trim())) {
            PreparedStatement preparedStatement = connection.prepareStatement(sqlStringTag);
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();

            PreparedStatement preparedStatementWish = connection.prepareStatement(sqlStringWish);
            preparedStatementWish.setInt(1, id);
            preparedStatementWish.executeUpdate();


        } catch (SQLException e) {
            logger.error("SQL exception occured", e);
        }
    }

    @Override
    public String getUserNameById(int userId) {
        String sql = "SELECT name FROM user WHERE user_id=?";
        String user_name = null;
        try (Connection con = DriverManager.getConnection(dbUrl.trim(), username.trim(), password.trim())) {
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                user_name = resultSet.getString("name");
            }
        } catch (SQLException e) {
            logger.error("SQL exception occurred", e);

        }
        return user_name;
    }

    @Override
    public void giveWish(int wish_id) {

    }

    @Override
    public UserWishlistDTO getUserwishlistByUserId(int user_id) {
        String sqlString = "SELECT t.wishlist_name, t.wishlist_id, t.user_id, t.role_id, r.role_name, u.name " +
                "FROM wishlist t " +
                "JOIN role r ON r.role_id = t.role_id " +
                "JOIN user u ON u.user_id = t.user_id " + // Tilføjet 'u' alias
                "WHERE r.role_name = 'giftwisher' AND t.user_id = ?";
        String sqlwishes = "SELECT wish_name, wish_description, price, wish_id, wishlist_id FROM wish WHERE wishlist_id=?";
        String sqlTags = "SELECT tag_id FROM wish_tag WHERE wish_id=?";
        System.out.println("recived userid in getuserwishlistbyuserid " + user_id + "");
        List<Integer> tagIds = new ArrayList<>();
        UserWishlistDTO userWishlistDTO = new UserWishlistDTO();
        ArrayList<WishTagDTO> wishTagDTOS = new ArrayList<>();
        WishTagDTO wishTagDTO;
        try (Connection con = DriverManager.getConnection(dbUrl.trim(), username.trim(), password.trim())) {
            PreparedStatement statement = con.prepareStatement(sqlString);
            statement.setInt(1, user_id);


            PreparedStatement statement3 = con.prepareStatement(sqlTags);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String name = resultSet.getString("name");
                String wishlist_name = resultSet.getString("wishlist_name");
                int wishlist_id = resultSet.getInt("wishlist_id");
                int role_id = resultSet.getInt("role_id");
                String role_name = resultSet.getString("role_name");

                System.out.println("fra getuserwishlistbyuserid har jeg wishlistId: " + wishlist_id);

                PreparedStatement statement2 = con.prepareStatement(sqlwishes);
                statement2.setInt(1, wishlist_id);
                ResultSet resultSet2 = statement2.executeQuery();

                wishTagDTOS = new ArrayList<>();

                while (resultSet2.next()) {
                    String wish_name = resultSet2.getString("wish_name");
                    String wish_description = resultSet2.getString("wish_description");
                    int price = resultSet2.getInt("price");
                    int wish_id = resultSet2.getInt("wish_id");

                    System.out.println("fra getuserwishlistbyuserid har jeg wish_id: " + wish_id);

                    statement3.setInt(1, wish_id);
                    ResultSet resultSet3 = statement3.executeQuery();
                    while (resultSet3.next()) {
                        int tagid = resultSet3.getInt("tag_id");
                        tagIds.add(tagid);
                    }
                    wishTagDTO = new WishTagDTO(wish_name, wish_description, price, wish_id, tagIds, wishlist_id);
                    System.out.println("dette er mit wishTagDto objekt " + wishTagDTO); // test
                    wishTagDTOS.add(wishTagDTO);
                }
                userWishlistDTO = new UserWishlistDTO(name, wishlist_name, wishlist_id, user_id, role_id, role_name, wishTagDTOS);
                System.out.println("dette er mit userWishlistDTO objekt " + userWishlistDTO); //test
            }

        } catch (SQLException e) {
            logger.error("SQL exception occurred", e);
        }

        return userWishlistDTO;
    }


    @Override
    public void createUserAndWishlistDTO(String user_name, UserWishlistDTO uw) {
        logger.info("Received role_id: {}", uw.getRole_id());

        String sqlInsertUser = "INSERT INTO user (name) VALUES (?)";
        String sqlInsertWishlist = "INSERT INTO wishlist(wishlist_name, user_id, role_id) VALUES (?,?,?)";
        String sqlInsertUserRole = "INSERT INTO user_role(user_id, role_id) VALUES (?,?)";

        try (Connection con = DriverManager.getConnection(dbUrl.trim(), username.trim(), password.trim())) {
            con.setAutoCommit(false);

            // Insert user
            PreparedStatement statement1 = con.prepareStatement(sqlInsertUser, Statement.RETURN_GENERATED_KEYS);
            statement1.setString(1, user_name);
            statement1.executeUpdate();

            ResultSet resultSet1 = statement1.getGeneratedKeys();
            if (resultSet1.next()) {
                int generatedUserId = resultSet1.getInt(1); // Get the generated user_id

                // Insert into user_role table
                PreparedStatement statement3 = con.prepareStatement(sqlInsertUserRole);
                statement3.setInt(1, generatedUserId);
                statement3.setInt(2, uw.getRole_id());
                statement3.executeUpdate();

                // Insert into wishlist table
                PreparedStatement statement2 = con.prepareStatement(sqlInsertWishlist);
                statement2.setString(1, uw.getWishlist_name());
                statement2.setInt(2, generatedUserId); // Use the generated user_id
                statement2.setInt(3, uw.getRole_id());
                statement2.executeUpdate();
            }

            con.commit();
        } catch (SQLException rollbackEx) {
            logger.error("Error occurred during rollback", rollbackEx);
        }
    }

}
