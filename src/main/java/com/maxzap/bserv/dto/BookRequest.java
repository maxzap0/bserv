package com.maxzap.bserv.dto;

import lombok.Data;

@Data
public class BookRequest {
    private String title;
    private String author;
    private double price;
}
