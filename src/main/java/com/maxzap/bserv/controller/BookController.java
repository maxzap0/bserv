package com.maxzap.bserv.controller;

import com.maxzap.bserv.dto.BookRequest;
import com.maxzap.bserv.mapper.BookToDtoMapper;
import com.maxzap.bserv.model.Book;
import com.maxzap.bserv.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;
    private final BookToDtoMapper bookToDtoMapper;

    @GetMapping
    public List<Book> getAllBooks(@RequestParam(required = false) String author){
        if (author==null) return bookService.getAllBook();
        return bookService.findByAuthor(author);
    }

    @GetMapping("/{id}")
    public Book getBookById(@PathVariable long id) {
        return bookService.getBookById(id);
    }

    @PostMapping
    public void addBook(@RequestBody BookRequest bookRequest) {
        bookService.addBook(bookToDtoMapper.bookRequestToBook(bookRequest));
    }
}
