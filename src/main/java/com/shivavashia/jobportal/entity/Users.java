package com.shivavashia.jobportal.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Entity
@Table(name = "users")
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int user_id;

    @Column(unique = true,name = "email")
    private String email;

//    @NotEmpty
    @Column(name = "is_active")
    private String is_active;

    @NotEmpty
    @Column(name = "password")
    private String password;

    @DateTimeFormat(pattern="dd-MM-yyyy")
    @Column(name = "registration_date")
    private Date registration_date;

    @ManyToOne
    @JoinColumn(name = "user_type_id", referencedColumnName = "user_type_id")
    private UsersType user_type_id;


    public Users() {
    }

    public Users(int user_id, String email, String is_active, String password, Date registration_date, UsersType user_type_id) {
        this.user_id = user_id;
        this.email = email;
        this.is_active = is_active;
        this.password = password;
        this.registration_date = registration_date;
        this.user_type_id = user_type_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIs_active() {
        return is_active;
    }

    public void setIs_active(String is_active) {
        this.is_active = is_active;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getRegistration_date() {
        return registration_date;
    }

    public void setRegistration_date(Date registration_date) {
        this.registration_date = registration_date;
    }

    public UsersType getUser_type_id() {
        return user_type_id;
    }

    public void setUser_type_id(UsersType user_type_id) {
        this.user_type_id = user_type_id;
    }

    @Override
    public String toString() {
        return "Users{" +
                "user_id=" + user_id +
                ", email='" + email + '\'' +
                ", is_active='" + is_active + '\'' +
                ", password='" + password + '\'' +
                ", registration_date=" + registration_date +
                ", userTypeId=" + user_type_id +
                '}';
    }
}
