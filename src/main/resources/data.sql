------------------------------- PRODUCTS ----------------------------------------------------
insert into products (name, price, stock, category, is_active, created_at)
values ('Blue Jeans', 17.0, 27, 'CLOTHES', true,
        current_timestamp);

insert into products (name, price, stock, category, is_active, created_at)
values ('Black Jeans', 17.0, 28, 'CLOTHES', true,
        current_timestamp);

insert into products (name, price, stock, category, is_active, created_at)
values ('Pure Jeans', 17.0, 29, 'CLOTHES', true,
        current_timestamp);

insert into products (name, price, stock, category, is_active, created_at)
values ('Jeans', 17.0, 29, 'CLOTHES', true,
        current_timestamp);

insert into products (name, price, stock, category, is_active, created_at)
values ('Shoes', 17.0, 29, 'CLOTHES', true,
        current_timestamp);

insert into products (name, price, stock, category, is_active, created_at)
values ('T-SHIRT', 17.0, 26, 'CLOTHES', true,
        current_timestamp);

insert into products (name, price, stock, category, is_active, created_at)
values ('CAP', 17.0, 27, 'CLOTHES', true,current_timestamp);

-----------------------------------------ORDERS------------------------------------------------

insert into orders (customer_name, customer_email, order_date, order_status, total_amount)
values ('John', 'john@gmail.com', current_timestamp, 'PENDING', 85.0);

insert into orders (customer_name, customer_email, order_date, order_status, total_amount)
values ('George', 'george@gmail.com', current_timestamp, 'PENDING', 34.0);

insert into orders (customer_name, customer_email, order_date, order_status, total_amount)
values ('Ali', 'ali@gmail.com', current_timestamp, 'PENDING', 51.0);


insert into orders (customer_name, customer_email, order_date, order_status, total_amount)
values ('Ali', 'ali@gmail.com', current_timestamp, 'PENDING', 85.0);

---------------------------------------ORDER ITEMS--------------------------------------------------------
insert into order_items (order_id, product_id, quantity, unit_price, total_price)
values (3, 7, 3, 17.0, 51.0);

insert into order_items (order_id, product_id, quantity, unit_price, total_price)
values (4, 6, 4, 17.0, 68.0),
       (4, 5, 1, 17.0, 17.0);

insert into order_items (order_id, product_id, quantity, unit_price, total_price)
values (1, 1, 3, 17.0, 51.0);

insert into order_items (order_id, product_id, quantity, unit_price, total_price)
values (1, 2, 2, 17.0, 34.0);

insert into order_items (order_id, product_id, quantity, unit_price, total_price)
values (2, 3, 1, 17.0, 17.0);

insert into order_items (order_id, product_id, quantity, unit_price, total_price)
values (2, 4, 1, 17.0, 17.0);

-------------------------------------------- ROLE -------------------------------------------------

insert into roles (name) values ('ROLE_USER');
insert into roles (name) values ('ROLE_MANAGER');
insert into roles (name) values ('ROLE_ADMIN');

