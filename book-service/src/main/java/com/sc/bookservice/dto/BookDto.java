package com.sc.bookservice.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class BookDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String author;

    private Date launchDate;

    private Double price;

    private String title;

    private String currency;

    private String environment;

    public BookDto() {}

    public BookDto(Long id, String author, Date launchDate, Double price, String title, String currency, String environment) {
        this.id = id;
        this.author = author;
        this.launchDate = launchDate;
        this.price = price;
        this.title = title;
        this.currency = currency;
        this.environment = environment;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Date getLaunchDate() {
        return launchDate;
    }

    public void setLaunchDate(Date launchDate) {
        this.launchDate = launchDate;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookDto bookDto = (BookDto) o;
        return Objects.equals(id, bookDto.id) && Objects.equals(author, bookDto.author) && Objects.equals(launchDate, bookDto.launchDate) && Objects.equals(price, bookDto.price) && Objects.equals(title, bookDto.title) && Objects.equals(currency, bookDto.currency) && Objects.equals(environment, bookDto.environment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, author, launchDate, price, title, currency, environment);
    }
}
