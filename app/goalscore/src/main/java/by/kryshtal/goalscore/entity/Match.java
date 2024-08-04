package by.kryshtal.goalscore.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Match {
    private int id;
    private int competition_id;
    private int attendance;
    private int home_match_team_id;
    private int away_match_team_id;
    private short home_goal_score;
    private short away_goal_score;
    private String match_date;
}
