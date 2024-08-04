package by.kryshtal.goalscore.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlayerDto {
    private int id;
    private String name;
    private String position;
    private Date date_of_birth;
    public void setDate_of_birth(String date_of_birth) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            this.date_of_birth = dateFormat.parse(date_of_birth);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
    private String nationality;
    private int team_id;
}
