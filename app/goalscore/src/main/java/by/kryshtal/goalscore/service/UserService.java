package by.kryshtal.goalscore.service;

import by.kryshtal.goalscore.dto.AuthUserDto;
import by.kryshtal.goalscore.dto.RegisterUserDto;
import by.kryshtal.goalscore.dto.UserProfileInfoDto;
import by.kryshtal.goalscore.entity.Token;
import by.kryshtal.goalscore.entity.User;
import by.kryshtal.goalscore.exceptions.AuthenticationException;
import by.kryshtal.goalscore.exceptions.NoSuchEntityException;
import by.kryshtal.goalscore.exceptions.RegistrationException;
import by.kryshtal.goalscore.repository.UnitOfWork;
import by.kryshtal.goalscore.security.Hasher;
import by.kryshtal.goalscore.security.TokenGenerator;
import by.kryshtal.goalscore.util.Cookier;
import by.kryshtal.goalscore.util.Mapper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;

@Service
public class UserService {
    private final UnitOfWork UOW;
    @Autowired
    public UserService(UnitOfWork UOW) {
        this.UOW = UOW;
    }

    public String authenticate(AuthUserDto authUserDto) throws NoSuchEntityException, AuthenticationException, SQLException {
        User user = UOW.getUserRepository().findByLogin(authUserDto.getLogin());
        if (user == null)
            throw new NoSuchEntityException("no user with such login");

        if (!Hasher.check(authUserDto.getPassword(), user.getPassword()))
            throw new AuthenticationException("invalid user сredentials");

        Token token = new Token(user.getId(), TokenGenerator.generate());
        UOW.getTokenRepository().save(token);
        return token.getToken();
    }
    public void register(RegisterUserDto registerUserDto) throws RegistrationException, SQLException {
        if (UOW.getUserRepository().findByLogin(registerUserDto.getLogin()) != null)
            throw new RegistrationException("user with such login already exist");

        User user = new User(registerUserDto);
        UOW.getUserRepository().insert(user);
    }
    public boolean isAdmin(HttpServletRequest request) throws SQLException {
        Optional<String> _token = Cookier.readServletCookie(request, "user-id");
        if (_token.isPresent()) {
            if(UOW.getUserRepository().getUserRole(_token.get()).equals("ROLE_ADMIN")){
                return true;
            }else{
                return false;
            }
        } else {
            return false;
        }
    }
    public int getUserId(HttpServletRequest request) throws SQLException {
        Optional<String> _token = Cookier.readServletCookie(request, "user-id");
        if (_token.isPresent()) {
            return  UOW.getTokenRepository().getUserIdByToken(_token.get());
        } else {
            return -1;
        }
    }
    public void setProfile_image(int _user_id, byte[] _bytes) throws SQLException {
        UOW.getUserRepository().setProfile_image(_user_id, _bytes);
    }
    public UserProfileInfoDto getUserProfile(int user_id) throws SQLException {
        return Mapper.map(UOW.getUserRepository().getById(user_id), UserProfileInfoDto.class);
    }

}
