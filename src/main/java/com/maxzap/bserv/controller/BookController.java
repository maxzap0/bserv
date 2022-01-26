package com.maxzap.bserv.controller;

import com.maxzap.bserv.dto.BookRequest;
import com.maxzap.bserv.mapper.BookToDtoMapper;
import com.maxzap.bserv.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;
    private final BookToDtoMapper bookToDtoMapper;

    @GetMapping
    public ResponseEntity<Object> getAllBooks(@RequestParam(required = false) String author){
        if (author==null) return  new ResponseEntity<>(bookService.getAllBook(), HttpStatus.OK);
        return new ResponseEntity<>(bookService.findByAuthor(author), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getBookById(@PathVariable long id) {
        if (validId(id)) {
            return new ResponseEntity<>(bookService.getBookById(id), HttpStatus.OK);
        }
        return new ResponseEntity<>("Id "+id+" not found", HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<Object> addBook(@RequestBody BookRequest bookRequest) {
        try {
            bookService.addBook(bookToDtoMapper.bookRequestToBook(bookRequest));
            return new ResponseEntity<>("Book was added", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred adding", HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable long id) {
        if (validId(id)) {
            bookService.deleteById(id);
            return new ResponseEntity<>("Book was deleted", HttpStatus.OK);
        }
        return new ResponseEntity<>("Id "+id+" not found", HttpStatus.NOT_FOUND);
    }

    private boolean validId(long id) {
        return (id>0 && id<=bookService.countBooks());
    }
}
