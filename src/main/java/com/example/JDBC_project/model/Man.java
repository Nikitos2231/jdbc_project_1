package com.example.JDBC_project.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import org.springframework.stereotype.Component;

@Component
public class Man {

    @Size(min = 2, max = 30, message = "Имя может состоять от 2 до 30 символов")
    private String name;

    @Min(value = 1900, message = "Го должен начинаться от 1900г")
    private int yearOfBirth;

    private int id;

    public Man() {
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

    public int getYearOfBirth() {
        return yearOfBirth;
    }

    public void setYearOfBirth(int yearOfBirth) {
        this.yearOfBirth = yearOfBirth;
    }
}
