INSERT INTO category (name) VALUES ('Furniture');
INSERT INTO category (name) VALUES ('Discounted');
INSERT INTO category (name) VALUES ('Featured');
INSERT INTO category (name) VALUES ('Refurbished');

INSERT INTO product (name, brand, description, price) VALUES ('Desk', 'Solid Co.', 'A desk', 250.00);
INSERT INTO product (name, brand, description, price) VALUES ('Chair', 'Chairs-R-US', 'A chair', 75.95);
INSERT INTO product (name, brand, description, price) VALUES ('Table', 'Solid Co.', 'A table', 144.50);

INSERT INTO product_category (product_id, category_id) VALUES ((select id from product where name = 'Desk'), (select id from category where name = 'Furniture'));
INSERT INTO product_category (product_id, category_id) VALUES ((select id from product where name = 'Desk'), (select id from category where name = 'Featured'));
INSERT INTO product_category (product_id, category_id) VALUES ((select id from product where name = 'Chair'), (select id from category where name = 'Furniture'));
INSERT INTO product_category (product_id, category_id) VALUES ((select id from product where name = 'Chair'), (select id from category where name = 'Discounted'));
INSERT INTO product_category (product_id, category_id) VALUES ((select id from product where name = 'Table'), (select id from category where name = 'Furniture'));
INSERT INTO product_category (product_id, category_id) VALUES ((select id from product where name = 'Table'), (select id from category where name = 'Refurbished'));
