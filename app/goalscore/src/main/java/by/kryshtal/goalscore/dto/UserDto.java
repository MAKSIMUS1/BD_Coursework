package by.kryshtal.goalscore.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public class UserDto {

    public Long id;

    @JsonFormat(pattern = "yyyy-MM-dd")
    public LocalDate joined;

    public String login;

    public UserDto() {
    }

    public UserDto(Long id, LocalDate joined, String login) {
        this.id = id;
        this.login = login;
        this.joined = joined;
    }

}

