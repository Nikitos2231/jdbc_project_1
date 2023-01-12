package com.example.JDBC_project.model;

import jakarta.validation.constraints.Size;
import org.springframework.stereotype.Component;

import java.sql.Date;

@Component
public class Book {

    @Size(min = 2, max = 100, message = "Размер названия должен быть от 2 до 100 символов")
    private String name;
    @Size(min = 2, max = 50, message = "Размер имени автора должен быть от 2 до 50 символов")
    private String author;
    private java.sql.Date date;
    private int id;

    private int manId;

    public Book() {
    }

    public int getManId() {
        return manId;
    }

    public void setManId(int manId) {
        this.manId = manId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
