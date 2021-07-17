CREATE TABLE stock
(
    sku             VARCHAR(9)  PRIMARY KEY,
    base_product_no VARCHAR(16) NOT NULL,
    amount          INTEGER     NOT NULL
);

CREATE INDEX stock_base_product_no ON stock (base_product_no);