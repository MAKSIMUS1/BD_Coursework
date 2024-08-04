package by.kryshtal.goalscore.entity.enums;

/**
 * Number in constructor parameter represents level of access
 * 10 - ROLE_ADMIN
 * 3 - ROLE_USER
 * 0 - null
 */
public enum UserRole {
    OWNER(10),
    AUTHORIZED(3),
    UNAUTHORIZED(0);

    public Integer level;

    UserRole(Integer level) {
        this.level = level;
    }
}