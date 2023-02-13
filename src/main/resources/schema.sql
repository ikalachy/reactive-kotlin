CREATE TABLE breed (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(255),
  image VARCHAR(255)
);

CREATE TABLE sub_breed (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(255),
  breed_id INT,
  FOREIGN KEY (breed_id) REFERENCES breed(id)
);
--
--1  terrier1, 1
--2  terrier2, 1
--3  terrier2, 1
--
--"brred", "", "1"