-- Add a unique constraint to products.name to prevent duplicate product names
ALTER TABLE products
    ADD CONSTRAINT uq_products_name UNIQUE (name);

WITH categories_to_insert (name, description) AS (
    VALUES
        ('Electronics', 'Devices and gadgets, from computers to smartphones.'),
        ('Books', 'Fiction, non-fiction, and educational books.'),
        ('Home & Kitchen', 'Appliances, cookware, and home decor.'),
        ('Clothing', 'Apparel for men, women, and children.'),
        ('Sports & Outdoors', 'Gear for fitness, hiking, and various sports activities.')
)
INSERT INTO categories (name, description)
SELECT name, description FROM categories_to_insert;

WITH products_to_insert (category_name, product_name, description, quantity, price) AS (
    VALUES
        -- Electronics
        ('Electronics', 'Laptop Pro 15"', 'A powerful laptop with a 15-inch retina display and M3 chip.', 50, 1999.99),
        ('Electronics', 'Wireless Noise-Cancelling Headphones', 'Immersive sound with adaptive noise cancellation.', 150, 349.00),
        -- Books
        ('Books', 'The Art of Programming', 'A classic computer science textbook covering fundamental algorithms.', 200, 75.50),
        ('Books', 'Dune', 'A science fiction masterpiece by Frank Herbert.', 300, 15.99),
        -- Home & Kitchen
        ('Home & Kitchen', 'Smart Coffee Maker', 'A WiFi-enabled coffee maker that you can control with your phone.', 80, 129.95),
        ('Home & Kitchen', 'Non-Stick Cookware Set', 'A 10-piece set of durable, non-stick pots and pans.', 120, 249.99),
        -- Clothing
        ('Clothing', 'Men''s Classic T-Shirt', 'A comfortable and stylish 100% cotton t-shirt.', 500, 25.00),
        ('Clothing', 'Women''s Running Shoes', 'Lightweight and breathable shoes perfect for running.', 250, 119.90),
        -- Sports & Outdoors
        ('Sports & Outdoors', '2-Person Camping Tent', 'A waterproof and easy-to-assemble tent for outdoor adventures.', 100, 99.99),
        ('Sports & Outdoors', 'Yoga Mat', 'An extra-thick, non-slip mat for yoga and pilates.', 400, 39.50)
)
INSERT INTO products (name, description, available_quantity, price, category_id)
SELECT
    p.product_name,
    p.description,
    p.quantity,
    p.price,
    c.id -- We fetch the category's UUID by joining on its unique name.
FROM products_to_insert p
         JOIN categories c ON p.category_name = c.name;
