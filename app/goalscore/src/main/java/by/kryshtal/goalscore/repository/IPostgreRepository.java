package by.kryshtal.goalscore.repository;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

public interface IPostgreRepository<T> {

    void insert(T element) throws SQLException, ParseException;

    void delete(T element) throws SQLException;

    void update(T element) throws SQLException;

    T get(T element) throws SQLException;

    T getById(int id) throws SQLException;

    List<T> getAll() throws SQLException;
}
