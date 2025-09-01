package com.bookshop.model;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import com.bookshop.validation.StrongPassword;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Column(unique = true, nullable = false)
    @NotBlank
    @Size(min = 3, max = 50)
    @Pattern(regexp = "^[A-Za-z0-9._-]+$", message = "Only letters, numbers, dot, underscore, hyphen")
    private String username;

    @NotBlank
    @Size(min = 12, max = 200)
    @StrongPassword
    private String password;

    @NotBlank
    private String role;
}
