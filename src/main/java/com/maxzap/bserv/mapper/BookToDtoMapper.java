package com.maxzap.bserv.mapper;

import com.maxzap.bserv.dto.BookRequest;
import com.maxzap.bserv.model.Book;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BookToDtoMapper {
    Book bookRequestToBook(BookRequest bookRequest);
    BookRequest bookToBookRequest(Book book);
}
