package com.maxzap.bserv.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.maxzap.bserv.dao.BookEntity;
import com.maxzap.bserv.dto.BookRequest;
import com.maxzap.bserv.mapper.BookToEntityMapperImpl;
import com.maxzap.bserv.service.BookService;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
@Sql(scripts = "classpath:init_db_h2_test.sql", config = @SqlConfig(encoding = "UTF-8"))
@SpringBootTest
class DefaultBookControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    BookService bookService;

    @Test
    void getBookById() throws Exception {
        mockMvc.perform(get("/books/1"))
                .andExpect(status().isOk());
    }

    @Test
    @Order(value = 0)
    void getBookById2() throws Exception {
        String title = bookService.getBookById(1).getTitle();
        mockMvc.perform(get("/books/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(title));
    }

    @Test
    void getBookByInvalidID() throws Exception {
        mockMvc.perform(get("/books/4"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAllBook() throws Exception {
        String title = bookService.getBookById(1).getTitle();
        mockMvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value(title));
    }


    /*
     * Используется BookEntity вместо Book, т.к. Book не имеет конструктора по умолчанию
     * и не может быть десерилирозован
     * */
    @Test
    void getAllBook2() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();
        ObjectMapper mapper = new ObjectMapper();
        List<BookEntity> booksResult = mapper.readValue(
                mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8),
                new TypeReference<>() {
                });
        List<BookEntity> expectedList = bookService.getAllBook()
                .stream()
                .map(book -> new BookToEntityMapperImpl().bookToBookEntity(book))
                .collect(Collectors.toList());
        assertThat(booksResult).hasSize(expectedList.size());
        assertEquals(booksResult.get(1).getTitle(), expectedList.get(1).getTitle());
    }

    @Test
    void findByAuthor() throws Exception {
        String author = bookService.getBookById(1).getAuthor();
        String mvcResult = mockMvc.perform(get("/books").param("author", author)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
        List<BookEntity> bookEntityList = new ObjectMapper().readValue(mvcResult, new TypeReference<>() {
        });
        bookEntityList.forEach(bookEntity -> assertEquals(author, bookEntity.getAuthor()));
    }

    @Test
    void addBook1() throws Exception {
        String jsonString = """
                {
                    "author": "test",
                    "title" : "test",
                    "price" : 120
                }
                """;

        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(jsonString))
                .andExpect(status().isOk());
    }

    @Test
    void addBook2() throws Exception {
        BookRequest bookRequest = BookRequest.builder()
                .author("test")
                .title("test")
                .price(34)
                .build();
        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(bookRequest)))
                .andExpect(status().isOk());
    }

    @Test
    @Order(value = 10)
    void deleteById() throws Exception {
        mockMvc.perform(delete("/books/3"))
                .andExpect(status().isOk());
    }

    @Test
    void deleteByIdZero() throws Exception {
        mockMvc.perform(delete("/books/0"))
                .andExpect(status().isNotFound());
    }
}