drop table if exists books;
create table books
(
    ID     INT auto_increment,
    AUTHOR VARCHAR(255),
    TITLE  VARCHAR(255),
    PRICE  DOUBLE,
    constraint TABLE_NAME_PK
        primary key (ID)
);

create unique index TABLE_NAME_ID_UINDEX
    on books (ID);

INSERT INTO books (author, title, price)
VALUES
    ('Достоевский Ф.М', 'Идиот', 245),
    ('Булгаков М.А.', 'Собачье сердце', 245),
    ('Кинг С.', 'Мизери', 189)
;

