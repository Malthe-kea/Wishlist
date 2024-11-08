drop database if exists wishlist;
create database if not exists wishlist;
use wishlist;

create table tag(tag_name varChar(255) NOT NULL, tag_id INT AUTO_INCREMENT PRIMARY KEY);

create table role(role_id int auto_increment primary key, role_name varChar(255));

create table user(name varChar(255),
                  user_id int PRIMARY KEY auto_increment);

create table user_role(user_id int , role_id int,
                       PRIMARY KEY (user_id, role_id),
                       FOREIGN KEY (user_id) REFERENCES user(user_id),
                       FOREIGN KEY (role_id) REFERENCES role(role_id));

create table wishlist(wishlist_name varChar(255) NOT NULL,
                      wishlist_id INT auto_increment PRIMARY KEY, user_id int, role_id int,
                      FOREIGN KEY (user_id, role_id) REFERENCES user_role(user_id, role_id));

create table wish(wish_name varChar(255) NOT NULL, wish_description varChar(255), price INT, wishlist_id INT, role_id INT, user_id int,
                  wish_id INT AUTO_INCREMENT PRIMARY KEY,
                  FOREIGN KEY (user_id, role_id) REFERENCES user_role(user_id, role_id),
                  FOREIGN KEY (wishlist_id) REFERENCES wishlist(wishlist_id));

create table wish_tag(tag_id int , wish_id int,
                      PRIMARY KEY (tag_id, wish_id),
                      FOREIGN KEY (tag_id) REFERENCES tag(tag_id),
                      FOREIGN KEY (wish_id) REFERENCES wish(wish_id));

INSERT INTO role(role_name)
VALUES('giftwisher'),('giftgiver');

INSERT INTO user(name)
VALUES('sabrina'), ('morten'), ('Amalie'),('Penelope');

INSERT INTO user_role(user_id, role_id)
VALUES(1, 1), (2, 1), (3,1),(4,1);

INSERT INTO wishlist(wishlist_name, user_id, role_id)
VALUES('sabrinas wishes', 1, 1),('morten wishes', 2, 1),('amalies birthday',3,1);

INSERT INTO wish(wish_name, wish_description, price, wishlist_id, role_id, user_id)
VALUES('Chair', 'A red chair', 2, 1, 1, 1), ('Lamp', 'Tall floorlamp in black', 2, 2, 1, 1), ('Socks', 'in either black or white', 2, 1, 1, 2),('Blanket', 'Fluffy and soft blanket in beige', 2, 1, 1, 2),
      ('Headphone', 'Soundproof and comfortable headphones from bose', 2, 2, 1, 3), ('Playstation 4', 'in white or black', 2, 1, 1, 3);

INSERT INTO tag(tag_name)
VALUES
    ('Blue ocean'),
    ('Reusable'),
    ('no plastic'),
    ('Produced in denmark'),
    ('Plants a tree'),
    ('Sustainable colors');

INSERT INTO wish_tag(tag_id, wish_id)
VALUES(1,1),(2,1),(2,2);



