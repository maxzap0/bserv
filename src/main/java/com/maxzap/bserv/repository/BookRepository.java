package com.maxzap.bserv.repository;

import com.maxzap.bserv.dao.BookEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepository extends JpaRepository<BookEntity, Long> {
    List<BookEntity> findAllByAuthorContaining(String author);
}
