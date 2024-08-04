package by.kryshtal.goalscore.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Player {
    private int id;
    private String name;
    private String position;
    private Date date_of_birth;
    private String nationality;
    private int team_id;
}