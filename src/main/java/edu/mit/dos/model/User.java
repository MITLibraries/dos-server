package edu.mit.dos.model;

import java.util.List;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Table(name = "user1")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    //@Size(min = 4, max = 255, message = "Minimum username length: 4 characters")
    //@Column(unique = true, nullable = false)
    @Column
    private String username;

    //@Column(unique = true, nullable = false)
    @Column
    private String email;

    @Size(min = 8, message = "Minimum password length: 8 characters")
    private String password;

    @Column
    Role role;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
