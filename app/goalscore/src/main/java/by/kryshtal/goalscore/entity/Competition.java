package by.kryshtal.goalscore.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Competition {
    private int id;
    private int area_id;
    private String name;
}
