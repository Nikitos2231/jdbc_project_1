package com.example.JDBC_project.service;

import com.example.JDBC_project.model.Book;
import com.example.JDBC_project.model.Man;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ManService {

    private static final Logger logger = LogManager.getLogger(ManService.class);
    private final Connection connection;

    @Autowired
    public ManService(Connection connection) {
        this.connection = connection;
    }

    public List<Man> getAllPeople() {
        List<Man> people = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Man");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Man man = new Man();
                man.setName(resultSet.getString("name"));
                man.setYearOfBirth(resultSet.getInt("year_of_birth"));
                man.setId(resultSet.getInt("id"));
                people.add(man);
            }
        } catch (SQLException e) {
            logger.warn("Произошла ошибка при попытке получить всех людей");
            throw new RuntimeException(e);
        }

        return  people;
    }

    public void saveMan(Man man) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO MAN(name, year_of_birth) VALUES(?,?)");
            preparedStatement.setString(1, man.getName());
            preparedStatement.setInt(2, man.getYearOfBirth());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            logger.warn("Произошла ошибка при попытке создать человека");
            throw new RuntimeException(e);
        }
    }

    public Man findById(int id) {
        Man man = new Man();
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM MAN WHERE id = ?")) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            man.setId(resultSet.getInt("id"));
            man.setName(resultSet.getString("name"));
            man.setYearOfBirth(resultSet.getInt("year_of_birth"));
        } catch (SQLException e) {
            logger.warn("Произошла ошибка при попытке найти человека с id: {}", id);
            throw new RuntimeException(e);
        }
        return man;
    }

    public boolean isExistManByName(String name) {
        try(PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM MAN WHERE name = ?")) {
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.next()) {
                return false;
            }
        } catch (SQLException e) {
            logger.warn("Произошла ошибка при попытке найти человека по имени: {}", name);
            throw new RuntimeException(e);
        }
        return true;
    }

    public void updateMan(Man man, int id) {
        try(PreparedStatement preparedStatement = connection.
                prepareStatement("UPDATE MAN SET name = ?, year_of_birth = ? where id = ?")) {
            preparedStatement.setString(1, man.getName());
            preparedStatement.setInt(2, man.getYearOfBirth());
            preparedStatement.setInt(3, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            logger.warn("Произошла ошибка при попытке обновить данные у человека с id: {}", id);
            throw new RuntimeException(e);
        }
    }

    public void deleteMan(int id) {
        try(PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM MAN WHERE id = ?")) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            logger.warn("Произошла ошибка при попытке удалить человека с id: {}", id);
            throw new RuntimeException(e);
        }
    }

    public List<Book> getBooksFromMan(int id) {
        List<Book> books = new ArrayList<>();
        try(PreparedStatement preparedStatement = connection.prepareStatement("select b.id, b.name, b.author, b.date from book b join Man M on b.man_id = M.id where man_id = ?")) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Book book = new Book();
                book.setId(resultSet.getInt("id"));
                book.setName(resultSet.getString("name"));
                book.setAuthor(resultSet.getString("author"));
                book.setDate(resultSet.getDate("date"));
                books.add(book);
            }
        } catch (SQLException e) {
            logger.warn("Произошла ошибка при попытке получить все книги для человека с id: {}", id);
            throw new RuntimeException(e);
        }
        return books;
    }
}
