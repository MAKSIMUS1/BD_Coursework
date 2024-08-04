package by.kryshtal.goalscore.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlayerMatchDto {
    private int id;
    private String name;
    private String position;
}
