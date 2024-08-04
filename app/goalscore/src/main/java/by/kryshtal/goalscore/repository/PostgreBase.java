package by.kryshtal.goalscore.repository;

import lombok.Getter;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

@Getter
public abstract class PostgreBase<T> implements IPostgreRepository<T> {

    protected Connection connection;

    public PostgreBase(Connection connection){
        this.connection = connection;
    }

    @Override
    public abstract void insert(T element) throws SQLException, ParseException;

    @Override
    public abstract void delete(T element) throws SQLException;

    @Override
    public abstract void update(T element) throws SQLException;

    @Override
    public abstract T get(T element) throws SQLException;

    @Override
    public abstract List<T> getAll() throws SQLException;

}
