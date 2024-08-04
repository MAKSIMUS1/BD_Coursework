package by.kryshtal.goalscore.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeamInfoDto {
    private int id;;
    public int area_id;
    private String name;
    private String short_name;
    private String tla;
    private String address;
    private String website;
    private int founded;
    private String club_colors;
    private String area_name;
}
