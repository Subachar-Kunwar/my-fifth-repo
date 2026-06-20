CREATE DATABASE group7_Rewear;
USE group7_Rewear;

CREATE TABLE users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role ENUM('buyer', 'seller') NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


CREATE TABLE otp_table (
    email VARCHAR(150) PRIMARY KEY,
    otp VARCHAR(10) NOT NULL,
    expiry DATETIME NOT NULL
);


INSERT INTO users (username, email, password, role) 
VALUES ('testuser', 'test@gmail.com', '12345678', 'buyer');


CREATE TABLE otp_codes (
    id INT PRIMARY KEY AUTO_INCREMENT,
    email VARCHAR(150) NOT NULL,
    otp_code VARCHAR(10) NOT NULL,
    expires_at DATETIME NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_email (email)
);


CREATE TABLE buyer_details (
    user_id INT PRIMARY KEY,
    address TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);


CREATE TABLE seller_details (
    user_id INT PRIMARY KEY,
    total_products INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

SELECT * FROM users;
SELECT * FROM buyer_details;
SELECT * FROM seller_details;

SELECT * FROM otp_table;
SELECT * FROM otp_codes;
SHOW TABLES;

SELECT username, email, role FROM users;

SELECT * FROM users WHERE role = 'seller';
SELECT * FROM users WHERE role = 'buyer';


-- Check users
SELECT id, username, email, role FROM users;

-- Check buyer_details
SELECT * FROM buyer_details ORDER BY user_id;

-- Check seller_details  
SELECT * FROM seller_details ORDER BY user_id ;

SELECT 
    u.id,
    u.username,
    u.email,
    u.password,
    u.role,
    u.created_at as user_created_at,
    s.total_products,
    s.created_at as seller_since
FROM users u
INNER JOIN seller_details s ON u.id = s.user_id
WHERE u.role = 'seller';

ALTER TABLE otp_codes 
ADD COLUMN failed_attempts INT DEFAULT 0;

SELECT * FROM otp_codes WHERE email = 'subacharkunwar@gmail.com';
SELECT 
    id,
    email,
    otp_code,
    expires_at,
    created_at,
    failed_attempts
FROM otp_codes;

SELECT 
    email,
    otp_code,
    failed_attempts,
    TIMESTAMPDIFF(MINUTE, created_at, expires_at) AS expiry_minutes
FROM otp_codes 
WHERE email = 'subacharkunwar@gmail.com';

SELECT 
    email,
    failed_attempts AS attempts_used,
    3 - failed_attempts AS attempts_left,
    TIME_FORMAT(TIMEDIFF(expires_at, NOW()), '%i:%s') AS time_left
FROM otp_codes 
WHERE expires_at > NOW()
ORDER BY created_at DESC;

-- Check if OTP exists
SELECT * FROM otp_codes WHERE email = 'subacharkunwar@gmail.com';



CREATE TABLE products (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    category VARCHAR(50) NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    image_path VARCHAR(255),
    seller_id INT,
    FOREIGN KEY (seller_id) REFERENCES users(id)
);

ALTER TABLE products 
ADD COLUMN description VARCHAR(500),
ADD COLUMN stock INT DEFAULT 0;


DELETE FROM products;

-- CHILDREN (c1-c18)
INSERT INTO products (name, category, price, image_path, seller_id) VALUES
('Bape Zipup Hoodie', 'Children', 800, 'images/c1.jpg', 1),
('Knight Print T-shirt', 'Children', 850, 'images/c2.jpeg', 1),
('Spider-Man Button Up', 'Children', 900, 'images/c3.jpeg', 1),
('Mad Doc Tee', 'Children', 750, 'images/c4.jpeg', 1),
('Yellow Baby T-shirt', 'Children', 700, 'images/c5.jpeg', 1),
('Transformers T-shirt', 'Children', 650, 'images/c6.jpeg', 1),
('Ralph Button Up', 'Children', 600, 'images/c7.jpeg', 1),
('Black Shorts', 'Children', 550, 'images/c8.jpeg', 1),
('Cargo Pants', 'Children', 520, 'images/c9.jpg', 1),
('Loose Fit Trousers', 'Children', 250, 'images/c10.jpg', 1),
('Soccer Sleeveless', 'Children', 300, 'images/c11.jpeg', 1),
('Sweatpants', 'Children', 350, 'images/c12.jpeg', 1),
('VENUM T-shirt', 'Children', 820, 'images/c13.jpeg', 1),
('Seahawks Jersey', 'Children', 950, 'images/c14.jpg', 1),
('Nike Pro', 'Children', 400, 'images/c15.jpeg', 1),
('Printed Shorts', 'Children', 420, 'images/c16.jpeg', 1),
('Blue Striped Jersey', 'Children', 480, 'images/c17.jpeg', 1),
('Emergence Hoodie', 'Children', 780, 'images/c18.jpeg', 1),

-- MEN (m1-m18)
('Ajax Home Jersey', 'Men', 1200, 'images/m1.jpeg', 1),
('Argentina Retro Jersey', 'Men', 6500, 'images/m2.jpg', 1),
('Leather Jacket', 'Men', 8900, 'images/m3.jpg', 1),
('Tottenham Jersey', 'Men', 1350, 'images/m4.jpg', 1),
('Brazil Jersey', 'Men', 1500, 'images/m5.jpg', 1),
('Bayern Jersey', 'Men', 1650, 'images/m6.jpeg', 1),
('Calvin Klein T-shirt', 'Men', 2200, 'images/m7.jpeg', 1),
('England Away Jersey', 'Men', 2600, 'images/m8.jpg', 1),
('Barcelona Home Jersey', 'Men', 3100, 'images/m9.jpg', 1),
('Polo Tee', 'Men', 320, 'images/m10.jpeg', 1),
('Nike Dri-Fit', 'Men', 380, 'images/m11.jpeg', 1),
('FILA Tee', 'Men', 420, 'images/m12.jpeg', 1),
('Carhartt Hoodie', 'Men', 1800, 'images/m13.jpeg', 1),
('Nrvana T-shirt', 'Men', 450, 'images/m14.jpeg', 1),
('Vintage Nike Trackpants', 'Men', 7200, 'images/m15.jpeg', 1),
('PSG Home Jersey', 'Men', 3800, 'images/m16.jpeg', 1),
('Adidas Sports T-shirt', 'Men', 490, 'images/m17.jpeg', 1),
('Stussy Shorts', 'Men', 4200, 'images/m18.jpeg', 1),

-- UNISEX (u1-u18)
('Racer Jacket', 'Unisex', 1100, 'images/u1.jpg', 1),
('Vintage Work Jacket', 'Unisex', 7500, 'images/u2.jpg', 1),
('Vintage Trackpants', 'Unisex', 5800, 'images/u3.jpg', 1),
('Nike Trackpants', 'Unisex', 2400, 'images/u4.jpg', 1),
('Striped Polo T-shirt', 'Unisex', 340, 'images/u5.jpg', 1),
('Fullsleeve T-shirt', 'Unisex', 280, 'images/u6.jpg', 1),
('Parachute Pants', 'Unisex', 1650, 'images/u7.jpeg', 1),
('Champion Windcheater', 'Unisex', 1850, 'images/u8.jpeg', 1),
('Black Washed Jeans', 'Unisex', 2800, 'images/u9.jpg', 1),
('Adidas Compression T-shirt', 'Unisex', 3200, 'images/u10.jpeg', 1),
('Button Up shirt ', 'Unisex', 390, 'images/u11.jpg', 1),
('Mercedes X UA T-shirt', 'Unisex', 410, 'images/u12.jpeg', 1),
('Floral Print Shirt', 'Unisex', 440, 'images/u13.jpg', 1),
('Carhartt Retro ZipUp Hoodie', 'Unisex', 4500, 'images/u14.jpg', 1),
('Baggy Trousers', 'Unisex', 1250, 'images/u15.jpeg', 1),
('Baggy Sweatpants', 'Unisex', 1350, 'images/u16.jpeg', 1),
('Baseball Jersey', 'Unisex', 460, 'images/u17.jpeg', 1),
('Galatasaray Jersey', 'Unisex', 1500, 'images/u18.jpeg', 1),


-- WOMEN (w1-w21)
('Flared Trousers', 'Women', 550, 'images/w1.jpeg', 1),
('Ribbed Croptop', 'Women', 620, 'images/w2.jpeg', 1),
('Supreme Hoodie', 'Women', 1050, 'images/w3.jpeg', 1),
('HnM T-shirt', 'Women', 700, 'images/w4.jpeg', 1),
('Skirt', 'Women', 1200, 'images/w5.jpg', 1),
('Wideleg Trousers ', 'Women', 1400, 'images/w6.jpeg', 1),
('Button Up Shirt', 'Women', 1600, 'images/w7.jpeg', 1),
('CorsetTop', 'Women', 2100, 'images/w8.jpeg', 1),
('Slim T-shirt', 'Women', 2400, 'images/w9.jpeg', 1),
('Cherry T-shirt', 'Women', 2800, 'images/w10.jpg', 1),
('Knitted Top', 'Women', 5200, 'images/w11.jpeg', 1),
('Laced T-shirt', 'Women', 6100, 'images/w12.jpg', 1),
('Wide Leg Jeans ', 'Women', 820, 'images/w13.jpeg', 1),
('Raw Denim Jorts', 'Women', 950, 'images/w14.jpg', 1),
('Jeans Shorts', 'Women', 1750, 'images/w15.jpeg', 1),
('Cropped Sports T-shirt', 'Women', 3300, 'images/w16.jpg', 1),
('Crochet cropped T-shirt', 'Women', 3700, 'images/w17.jpg', 1),
('Embroidered Dress Skirt ', 'Women', 6800, 'images/w18.jpg', 1),
('Flared Jeans', 'Women', 4600, 'images/w19.jpeg', 1),
('Cropped Shirt', 'Women', 4200, 'images/w20.jpeg', 1),
('Vintage Jeans ', 'Women', 8200, 'images/w21.jpeg', 1);


UPDATE products 
SET name = 'Corset Top' 
WHERE name = 'CorsetTop';


SELECT name, image_path FROM products;




drop table products;

SELECT * FROM products 
WHERE category = ? AND price BETWEEN ? AND ?;


CREATE TABLE orders (
    id INT AUTO_INCREMENT PRIMARY KEY,
    product_id INT,
    user_id INT,
    order_date DATE,
    total_amount DECIMAL(10,2),
    status VARCHAR(50) DEFAULT 'Pending',
    full_name VARCHAR(150),
    address TEXT,
    city VARCHAR(100),
    phone_number VARCHAR(20),
    postal_code VARCHAR(20),
    payment_method VARCHAR(50),
    FOREIGN KEY (product_id) REFERENCES products(id),
    FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE user_activities (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    activity VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE cart (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE cart_items (
    id INT AUTO_INCREMENT PRIMARY KEY,
    cart_id INT NOT NULL,
    product_id INT NOT NULL,
    quantity INT NOT NULL DEFAULT 1,
    added_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (cart_id) REFERENCES cart(id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE
);



