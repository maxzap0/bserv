package com.maxzap.bserv.model;

import lombok.Value;

@Value
public class Book {
    long id;
    String title;
    String author;
    double price;
}
