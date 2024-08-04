package by.kryshtal.goalscore.dto;

import by.kryshtal.goalscore.validator.CellPhone;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterUserDto {

    @NotBlank
    @Size(min=2, max=50)
    private String login;

    @NotBlank
    @Size(min=2, max=50)
    private String password;

    @CellPhone
    private String phone;
}
