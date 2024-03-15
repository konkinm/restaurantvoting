INSERT INTO USERS (name, email, password)
VALUES ('User', 'user@yandex.ru', '{noop}password'),
       ('Admin', 'admin@gmail.com', '{noop}admin'),
       ('Guest', 'guest@gmail.com', '{noop}guest');

INSERT INTO USER_ROLE (role, user_id)
VALUES ('USER', 1),
       ('ADMIN', 2),
       ('USER', 2);

INSERT INTO RESTAURANT (name)
VALUES ('Khedi'),
       ('Khachapuri House');

INSERT INTO MENU (menu_date, restaurant_id)
VALUES ('2024-01-11', 1),
       (curdate(), 1),
       ('2024-01-11', 2),
       (curdate(), 2);

INSERT INTO DISH (description, price, menu_id)
VALUES ('Khachapuri', 500, 1),
       ('Adjapsandali', 1800, 2),
       ('Hinkali', 1500,  3),
       ('Pkhali', 1600, 4),
       ('Tea', 500, 1),
       ('Coffee', 700, 2),
       ('Beer', 600, 3),
       ('Mineral water', 350, 4);

INSERT INTO VOTE (user_id, restaurant_id, vote_date)
VALUES (2, 1, curdate()),
       (1, 1, '2024-03-10'),
       (2, 2, '2024-01-12');
