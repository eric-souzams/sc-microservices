package com.sc.bookservice.controller;

import com.sc.bookservice.dto.BookDto;
import com.sc.bookservice.model.Book;
import com.sc.bookservice.proxy.CambioProxy;
import com.sc.bookservice.repository.BookRepository;
import com.sc.bookservice.response.Cambio;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Optional;

@RestController
@RequestMapping(value = "/book-service")
public class BookController {

    @Autowired
    private Environment environment;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CambioProxy cambioProxy;

    @GetMapping(value = "/{id}/{currency}")
    public BookDto findBook(@PathVariable("id") Long id, @PathVariable("currency") String currency) {
        Optional<Book> result = bookRepository.findById(id);
        if (result.isEmpty()) throw new RuntimeException("Book Not Found");

        BookDto bookDto = new BookDto();
        BeanUtils.copyProperties(result.get(), bookDto);

        Cambio response = cambioProxy.getCambio(bookDto.getPrice(), "USD", currency);

        bookDto.setPrice(response.getConvertedValue());
        bookDto.setCurrency(currency);
        String port = environment.getProperty("local.server.port");
        bookDto.setEnvironment(port);

        return bookDto;
    }

}
