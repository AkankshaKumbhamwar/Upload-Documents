create database StoreFolder;
use StoreFolder;

CREATE TABLE user_registration (
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    email VARCHAR(255) primary key,
    phone varchar(20) not null unique,
    country VARCHAR(255),
    state VARCHAR(255),
    city VARCHAR(255),
    password VARCHAR(255)
);

select * from user_registration;

CREATE TABLE IF NOT EXISTS user_folders (
    folder_id INT AUTO_INCREMENT PRIMARY KEY,
    folder_name VARCHAR(255),
    email VARCHAR(255),
    FOREIGN KEY (email) REFERENCES user_registration(email)
);
select * from user_folders;

CREATE TABLE user_documents (
    document_id INT AUTO_INCREMENT PRIMARY KEY,
    document_name VARCHAR(255),
    document_type ENUM('PDF', 'Excel', 'Image', 'Word') NOT NULL,
    document_data LONGBLOB,
    email VARCHAR(255),
    folder_id INT,
    FOREIGN KEY (email) REFERENCES user_registration(email),
    FOREIGN KEY (folder_id) REFERENCES user_folders(folder_id)
);
select * from user_documents;





