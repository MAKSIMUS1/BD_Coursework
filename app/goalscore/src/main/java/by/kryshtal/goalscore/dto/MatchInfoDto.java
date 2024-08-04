package by.kryshtal.goalscore.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MatchInfoDto {
    private int matchId;
    private String competitionName;
    private String areaName;
    private Timestamp matchDatetime;
    private String homeTeamName;
    private int homeTeamId;
    private String awayTeamName;
    private int awayTeamId;
    private short homeGoal;
    private short awayGoal;
}
