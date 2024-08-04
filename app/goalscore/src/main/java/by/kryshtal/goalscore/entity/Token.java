package by.kryshtal.goalscore.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Token {

    private int id;
    private int user_id;
    private String token;

    public Token(int user_id, String token) {
        this.user_id = user_id;
        this.token = token;
    }

}
