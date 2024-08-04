package by.kryshtal.goalscore.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeamApplicationDto {
    private int team_id;
    private int goalkeeper_id;
    private int left_defender_id;
    private int central_1_defender_id;
    private int central_2_defender_id;
    private int right_defender_id;
    private int left_midfielder_id;
    private int central_midfielder_id;
    private int right_midfielder_id;
    private int left_winger_id;
    private int striker_id;
    private int right_winger_id;
    private String comment;
}
