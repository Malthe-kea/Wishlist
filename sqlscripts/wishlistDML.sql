INSERT INTO role(role_name)
VALUES('giftwisher'),('giftgiver');

INSERT INTO user(name)
VALUES('sabrina'), ('morten'), ('Amalie');

INSERT INTO user_role(user_id, role_id)
VALUES(1, 1), (2, 1), (3,1);

INSERT INTO wishlist(wishlist_name, user_id, role_id)
VALUES('sabrinas wishes', 1, 1),('morten wishes', 2, 1);

INSERT INTO wish(wish_name, wish_description, price, wishlist_id, role_id, user_id)
VALUES('book', 'sabrebook1', 2, 1, 1, 1), ('book', 'mortensebook', 2, 2, 1, 2), ('book', 'sabreebook2', 2, 1, 1, 1);

INSERT INTO tag(tag_name)
VALUES
    ('saves the ocean'),
    ('people pleaser'),
    ('no plastic');

INSERT INTO  wish_tag(tag_id, wish_id)
VALUES(1,1),(2,1),(2,2);

