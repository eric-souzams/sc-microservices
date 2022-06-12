package com.sc.bookservice.controller;

import com.sc.bookservice.dto.BookDto;
import com.sc.bookservice.model.Book;
import com.sc.bookservice.proxy.CambioProxy;
import com.sc.bookservice.repository.BookRepository;
import com.sc.bookservice.response.Cambio;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@Tag(name = "Book Endpoint")
@RestController
@RequestMapping(value = "/book-service")
public class BookController {

    @Autowired
    private Environment environment;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CambioProxy cambioProxy;

    private final Logger logger = LoggerFactory.getLogger(BookController.class);

    @Operation(summary = "Find specific book by your ID")
    @GetMapping(value = "/{id}/{currency}")
    @CircuitBreaker(name = "foo-bar", fallbackMethod = "fallbackMethod")
    public ResponseEntity<?> findBook(@PathVariable("id") Long id, @PathVariable("currency") String currency) {
        logger.info("Request to controller is received.");

        Optional<Book> result = bookRepository.findById(id);
        if (result.isEmpty()) throw new RuntimeException("Book Not Found");

        BookDto bookDto = new BookDto();
        BeanUtils.copyProperties(result.get(), bookDto);

        Cambio response = cambioProxy.getCambio(bookDto.getPrice(), "USD", currency);

        bookDto.setPrice(response.getConvertedValue());
        bookDto.setCurrency(currency);
        String port = environment.getProperty("local.server.port");
        bookDto.setEnvironment(port);

        return ResponseEntity.status(HttpStatus.OK).body(bookDto);
    }

    public ResponseEntity<?> fallbackMethod(Exception exception) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Endpoint down");
    }

}
