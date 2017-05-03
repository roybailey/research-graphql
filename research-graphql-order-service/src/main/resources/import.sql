INSERT INTO order_form (user_id, status) VALUES ('U02', 'SUBMITTED');
INSERT INTO order_item (orderform_id, product_id, quantity) VALUES ((select id from order_form where user_id = 'U02'), '1', 5);
INSERT INTO order_item (orderform_id, product_id, quantity) VALUES ((select id from order_form where user_id = 'U02'), '2', 3);

INSERT INTO order_form (user_id, status) VALUES ('U03', 'PAYED');
INSERT INTO order_item (orderform_id, product_id, quantity) VALUES ((select id from order_form where user_id = 'U03'), '2', 7);
