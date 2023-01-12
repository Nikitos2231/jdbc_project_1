package com.example.JDBC_project.service;

import com.example.JDBC_project.model.Book;
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
public class BookService {

    private static final Logger logger = LogManager.getLogger(BookService.class);
    private final Connection connection;

    @Autowired
    public BookService(Connection connection) {
        this.connection = connection;
    }

    public List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();
        try(PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM BOOK");
                ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                Book book = new Book();
                book.setName(resultSet.getString("name"));
                book.setAuthor(resultSet.getString("author"));
                book.setDate(resultSet.getDate("date"));
                book.setId(resultSet.getInt("id"));
                book.setManId(resultSet.getInt("man_id"));
                books.add(book);
            }
        } catch (SQLException e) {
            logger.warn("Произошла ошибка при попытке получить все книги");
            throw new RuntimeException(e);
        }
        return books;
    }

    public Book findBookById(int id) {
        Book book = new Book();
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Book where id = ?")) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            book.setName(resultSet.getString("name"));
            book.setAuthor(resultSet.getString("author"));
            book.setDate(resultSet.getDate("date"));
            book.setId(resultSet.getInt("id"));
            book.setManId(resultSet.getInt("man_id"));
        } catch (SQLException e) {
            logger.warn("Произошла ошибка при попытке получить данные по книге с id: {}", id);
            throw new RuntimeException(e);
        }
        return book;
    }

    public void assignBook(int id, int manId) {
        try(PreparedStatement preparedStatement = connection.prepareStatement("UPDATE BOOK SET man_id = ? where id = ?")) {
            preparedStatement.setInt(1, manId);
            preparedStatement.setInt(2, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            logger.warn("Произошла ошибка при попытке назначить книгу с id: {} человеку с id {}", id, manId);
            throw new RuntimeException(e);
        }
    }

    public void doFreeBook(int id) {
        try(PreparedStatement preparedStatement = connection.prepareStatement("UPDATE BOOK SET man_id = ? where id = ?")) {
            preparedStatement.setObject(1, null);
            preparedStatement.setInt(2, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            logger.warn("Проищошла ошибка при попытке сделать книгу с id: {} свободной", id);
            throw new RuntimeException(e);
        }
    }

    public void saveBook(Book book) {
        try(PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO BOOK(name, author, date) VALUES(?,?,?)")) {
            preparedStatement.setString(1, book.getName());
            preparedStatement.setString(2, book.getAuthor());
            preparedStatement.setDate(3, book.getDate());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            logger.warn("Произошла ошибка при попытке создать новую книгу");
            throw new RuntimeException(e);
        }
    }

    public void editBook(Book book, int id) {
        Book book1 = findBookById(id);
        try (PreparedStatement preparedStatement = connection
                .prepareStatement("UPDATE BOOK SET name = ?, author = ?, date = ?, man_id = ? where id = ?")) {
            preparedStatement.setString(1, book.getName());
            preparedStatement.setString(2, book.getAuthor());
            preparedStatement.setDate(3, book.getDate());
            preparedStatement.setInt(4, book1.getManId());
            preparedStatement.setInt(5, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            logger.warn("Произошла ошибка при попытке редактировать книгу с id: {}", id);
            throw new RuntimeException(e);
        }

    }

    public void deleteBook(int id) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM BOOK WHERE id = ?")) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            logger.warn("Произошла ошибка при попытке удалить книгу с id: {}", id);
            throw new RuntimeException(e);
        }
    }
}
