package com.neu.cloudassign1.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="Id", unique = true, nullable = false)
    private long id;

    @Column(name="email")
    @NotNull(message="is required")
    @Pattern(regexp="[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+", message="Invalid Email id")
    private String email;

    @Column(name="password")
    @NotNull(message="is required")
    private String password;

    public User(){}

    public User(long id, String email, String password) {

        this.id = id;
        this.email = email;
        this.password = password;
    }
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

	@Override
	public String toString() {
		return "User [email=" + email + ", password=" + password + "]";
	}
    
}
