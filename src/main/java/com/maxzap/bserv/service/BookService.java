package com.maxzap.bserv.service;

import com.maxzap.bserv.model.Book;

import java.util.List;

public interface BookService {
    Book getBookById(long id);
    List<Book> getAllBook();
    void addBook (Book book);
    List<Book> findByAuthor(String author);
}
