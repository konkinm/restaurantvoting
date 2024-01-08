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

INSERT INTO DISH (description, price, restaurant_id)
VALUES ('Khachapuri', 500, 2),
       ('Adjapsandali', 1800, 1),
       ('Hinkali', 1500, 2),
       ('Pkhali', 1600, 1);
