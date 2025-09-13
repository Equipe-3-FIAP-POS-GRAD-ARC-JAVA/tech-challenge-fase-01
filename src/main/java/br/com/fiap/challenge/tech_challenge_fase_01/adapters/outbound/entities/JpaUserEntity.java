package br.com.fiap.challenge.tech_challenge_fase_01.adapters.outbound.entities;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import br.com.fiap.challenge.tech_challenge_fase_01.domain.user.RolesEnum;
import br.com.fiap.challenge.tech_challenge_fase_01.domain.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class JpaUserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false)
    private String id;
    @Column(nullable = false, length = 100)
    private String name;
    @Column(unique = true, nullable = false)
    private String email;
    @Column(nullable = false, length = 100)
    private String login;
    @Column(nullable = false, length = 50)
    private String password;
    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RolesEnum role;
    @Column(name = "is_active", nullable = false)
    private boolean isActive;

    public User toUserDomain() {
        return User.with(this.id, this.name, this.email, this.login, this.password, this.createdAt, this.updatedAt, this.role, this.isActive);
    }

}
