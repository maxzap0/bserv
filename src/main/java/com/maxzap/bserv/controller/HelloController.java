package com.maxzap.bserv.controller;

import com.maxzap.bserv.dto.BookRequest;
import com.maxzap.bserv.mapper.BookToDtoMapperImpl;
import com.maxzap.bserv.model.Book;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@RestController
public class HelloController {

    @GetMapping("/hello")
    public String hello(@RequestParam(required = false) String name) {
        return "Hello, " + name;
    }

    @GetMapping("/hello/{id}")
    public Book test(@PathVariable String id) {

        Map<String, String> params = new HashMap<>();
        params.put("id", id);

        ResponseEntity<BookRequest> re = new RestTemplate().getForEntity(
                "http://localhost:8888/books/{id}", BookRequest.class, params
        );
        return new BookToDtoMapperImpl().bookRequestToBook(re.getBody());
    }

}
