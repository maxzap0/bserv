package com.maxzap.bserv.service;

import com.maxzap.bserv.model.Book;
import com.maxzap.bserv.dao.BookEntity;
import com.maxzap.bserv.exception.BookNotFoundException;
import com.maxzap.bserv.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import com.maxzap.bserv.mapper.BookToEntityMapper;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DefaultBookService implements BookService{

    private final BookRepository bookRepository;
    private final BookToEntityMapper bookToEntityMapper;

    @Override
    public Book getBookById(long id) {
        BookEntity bookEntity = bookRepository.findById(id).orElseThrow( ()
                -> new BookNotFoundException("Not found book with id "+id) );
        return bookToEntityMapper.bookEntityToBook(bookEntity);
    }

    @Override
    public List<Book> getAllBook() {
        List<BookEntity> bookEntityList = bookRepository.findAll();
        return bookEntityList.stream().
                map(bookToEntityMapper::bookEntityToBook)
                .toList();
    }

    @Override
    public void addBook(Book book) {
        BookEntity bookEntity = bookToEntityMapper.bookToBookEntity(book);
        bookRepository.save(bookEntity);
    }

    @Override
    public List<Book> findByAuthor(String author) {
        return bookRepository.findAllByAuthorContaining(author).stream()
                .map(bookToEntityMapper::bookEntityToBook)
                .toList();
    }
}
