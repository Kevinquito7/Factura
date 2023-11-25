CREATE TABLE IF NOT EXISTS Client (
    id SERIAL PRIMARY KEY,
    nui VARCHAR (100) NOT NULL,
    fullname VARCHAR(100) NOT NULL,
    address VARCHAR(100) NOT NULL
    );

CREATE TABLE IF NOT EXISTS Product  (
    id SERIAL PRIMARY KEY,
    description VARCHAR(100) NOT NULL,
    brand VARCHAR (100) NOT NULL,
    price DECIMAL (10,2) NOT NULL,
    stock INT NOT NULL
);

CREATE TABLE IF NOT EXISTS Invoice (
    id SERIAL PRIMARY KEY,
    code VARCHAR (30) NOT NULL,
    create_at DATE NOT NULL,
    total DECIMAL (10,2) NOT NULL,
    Client_id INT,
    FOREIGN KEY (Client_id) REFERENCES Client(id)
);

CREATE TABLE IF NOT EXISTS Detail (
    id SERIAL PRIMARY KEY,
    quantity INT NOT NULL,
    price DECIMAL (10,2) NOT NULL,
    Invoice_id INT,
    Product_id INT,
    FOREIGN KEY (Invoice_id) REFERENCES Invoice(id),
    FOREIGN KEY (Product_id) REFERENCES Product(id)
    )



