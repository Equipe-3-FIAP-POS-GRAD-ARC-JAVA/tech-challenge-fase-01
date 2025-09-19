package br.com.fiap.challenge.tech_challenge_fase_01.domain.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.regex.Pattern;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private String id;
    private String name;
    private String email;
    private String login;
    private String password;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<RolesEnum> role;
    private boolean isActive;

//    private User(String id, String name, String email, String login, String password, LocalDateTime createdAt, LocalDateTime updatedAt, List<RolesEnum> role, boolean isActive) {
//        this.id = id;
//        this.name = name;
//        this.email = email;
//        this.login = login;
//        this.password = password;
//        this.createdAt = createdAt;
//        this.updatedAt = updatedAt;
//        this.role = role;
//        this.isActive = isActive;
//    }

    public static User createClient(String id, String name, String email, String login, String password) {
        LocalDateTime now = LocalDateTime.now();
        return new User(id, name, email, login, password, now, now, List.of(RolesEnum.CLIENT), true);
    }

    public static User with(String id, String name, String email, String login, String password, LocalDateTime createdAt, LocalDateTime updatedAt, List<RolesEnum> role, boolean isActive) {
        return new User(id, name, email, login, password, createdAt, updatedAt, role, isActive);
    }

    public User update(String name, String email, String login, boolean isActive) {
        if (isActive) {
            this.activate();
        } else {
            this.deactivate();
        }

        this.name = name;
        this.email = email;
        this.login = login;
        this.updatedAt = LocalDateTime.now();
        return this;
    }

    public User updatePassword(String password) {
        if (!this.isValidPassword(password)) {
            throw new IllegalArgumentException("Senha Inválida.");
        }

        this.password = password;
        return this;
    }

    private boolean isValidPassword(String password) {
        String regex = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,20}$";
        return Pattern.matches(regex, password);
    }

    public User activate() {
        this.isActive = true;
        return this;
    }

    public User deactivate() {
        this.isActive = false;
        return this;
    }

}
