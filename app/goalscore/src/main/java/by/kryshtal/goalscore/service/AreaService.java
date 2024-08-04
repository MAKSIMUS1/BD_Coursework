package by.kryshtal.goalscore.service;

import by.kryshtal.goalscore.entity.Area;
import by.kryshtal.goalscore.repository.UnitOfWork;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

@Service
public class AreaService {
    private final UnitOfWork UOW;
    @Autowired
    public AreaService(UnitOfWork UOW) {
        this.UOW = UOW;
    }
    public Area getAreaById(int _id) throws SQLException {
        return UOW.getAreaRepository().getById(_id);
    }
}
