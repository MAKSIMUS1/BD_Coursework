package by.kryshtal.goalscore.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class PlayerInfoDto {
    private int id;

    public PlayerInfoDto(int id, String name, String position, Date date_of_birth, String nationality, String team_short_name, int team_id) {
        this.id = id;
        this.name = name;
        this.position = position;
        this.date_of_birth = date_of_birth;
        this.nationality = nationality.trim();
        this.team_short_name = team_short_name;
        this.team_id = team_id;
    }

    private String name;
    private String position;
    private Date date_of_birth;
    private String nationality;
    private String team_short_name;
    private int team_id;
}
