package by.kryshtal.goalscore.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthUserDto {

    @NotBlank
    private String login;
    @NotBlank
    private String password;

}
