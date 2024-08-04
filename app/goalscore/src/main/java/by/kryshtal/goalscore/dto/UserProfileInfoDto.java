package by.kryshtal.goalscore.dto;


import by.kryshtal.goalscore.entity.enums.UserRole;
import by.kryshtal.goalscore.util.ImageEncoder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Base64;
import java.util.Date;

@Data
@NoArgsConstructor
public class UserProfileInfoDto {
    private String username;
    private int favorite_team_id;
    private byte[] profile_image;
    private String phone;
    private Date joined;
    public String getProfile_image() {
        return ImageEncoder.encodeImage(this.profile_image);
    }
    public boolean isImage(){
        if(profile_image != null)
            return true;
        return false;
    }
}
