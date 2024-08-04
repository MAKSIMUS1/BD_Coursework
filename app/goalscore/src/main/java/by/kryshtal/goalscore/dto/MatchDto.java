package by.kryshtal.goalscore.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MatchDto {
    private int competition_id;
    private int attendance;
    private int home_match_team_id;
    private int away_match_team_id;
    private int home_goal_score;
    private int away_goal_score;
    private String match_date;
}

