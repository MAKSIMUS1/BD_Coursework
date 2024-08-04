package by.kryshtal.goalscore.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Area {
    private int id;
    private String name;
    public Area(int id, String name) {
        this.id = id;
        this.name = name.trim();
    }
}
