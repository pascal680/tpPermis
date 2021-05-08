package me.pascal.tpPermis.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@MappedSuperclass
@Data
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String nom;
    private String prenom;
    private String password;
    private String email;


    public User() {

    }

    public User(String nom, String prenom, String password, String email) {
        this.nom = nom;
        this.prenom = prenom;
        this.password = password;
        this.email = email;
    }


}
