DELETE
FROM user_roles;
DELETE
FROM votes;
DELETE
FROM users;
DELETE
FROM ITEMS;
DELETE
FROM RESTAURANTS;

ALTER SEQUENCE global_seq RESTART WITH 100000;
ALTER SEQUENCE VOTE_SEQ RESTART WITH 100000;
ALTER SEQUENCE ITEM_SEQ RESTART WITH 100000;

INSERT INTO users (name, email, password)
VALUES ('User', 'user@yandex.ru', '{noop}password'),
       ('Admin', 'admin@gmail.com', '{noop}admin');

INSERT INTO user_roles (role, user_id)
VALUES ('ROLE_USER', 100000),
       ('ROLE_ADMIN', 100001),
       ('ROLE_USER', 100001);

INSERT INTO RESTAURANTS (NAME)
VALUES ('TOKYO-CITY'), /* 100002 */
       ('KETCH-UP'), /* 100003 */
       ('ХАЧАПУРИ И ВИНО'), /* 100004 */
       ('KWAKINN'); /* 100005 */

INSERT INTO ITEMS (rest_id, name, price, date_time)
VALUES (100002, 'beer', 150, '2019-09-20'),
       (100002, 'wok', 200, '2019-09-20'),
       (100003, 'beer', 300, '2019-09-20'),
       (100002, 'beer', 150, '2019-09-21'),
       (100003, 'salad', 200, '2019-09-21'),
       (100002, 'salad', 200, curdate() ),
       (100002, 'sushi', 350, curdate()),
       (100003, 'sushi', 420, curdate());

INSERT INTO VOTES (user_id, rest_id,  date_time)
VALUES (100000, 100002, '2019-09-20'),
       (100000, 100003, '2019-09-21'),
       (100001, 100002, '2019-09-21');




