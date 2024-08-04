package by.kryshtal.goalscore.entity;

import by.kryshtal.goalscore.dto.RegisterUserDto;
import by.kryshtal.goalscore.entity.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Base64;
import java.util.Date;

@Data
@NoArgsConstructor
public class User {
    private int id;

    public User(int id, String username,
                String password,
                int favorite_team_id,
                byte[] profile_image,
                String phone,
                Date joined,
                String role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.favorite_team_id = favorite_team_id;
        this.profile_image = profile_image;
        this.phone = phone;
        this.joined = joined;
        setRole(role);
    }

    private String username;
    private String password;
    private int favorite_team_id;
    private byte[] profile_image;
    private String phone;
    private Date joined;
    private UserRole role;

    public void setRole(String role) {
        if(role.equals("ROLE_ADMIN")){
            this.role = UserRole.OWNER;
        }else if(role.equals("ROLE_USER")){
            this.role = UserRole.UNAUTHORIZED;
        }else{
            this.role = UserRole.UNAUTHORIZED;
        }
    }



    public User(RegisterUserDto registerUserDto) {
        this.username = registerUserDto.getLogin();
        this.password = registerUserDto.getPassword();
        this.phone = registerUserDto.getPhone();
    }
    public User getUser() {
        return this;
    }
}

