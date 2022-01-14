package com.maxzap.bserv.mapper;

import com.maxzap.bserv.dao.BookEntity;
import com.maxzap.bserv.model.Book;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BookToEntityMapper {
    BookEntity bookToBookEntity(Book book);
    Book bookEntityToBook(BookEntity bookEntity);
}
