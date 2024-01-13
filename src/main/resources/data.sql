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

INSERT INTO DISH (description, price, local_date, restaurant_id)
VALUES ('Khachapuri', 500, curdate(), 2),
       ('Adjapsandali', 1800, curdate(), 1),
       ('Hinkali', 1500, '2024-01-11', 2),
       ('Pkhali', 1600, '2024-01-11', 1);

INSERT INTO VOTE (user_id, restaurant_id, vote_date)
VALUES (1, 1, curdate()),
       (2, 2, '2024-01-12');
