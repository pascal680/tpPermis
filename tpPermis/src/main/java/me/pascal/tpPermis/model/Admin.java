package me.pascal.tpPermis.model;

import lombok.Data;

import javax.persistence.Entity;
import java.io.Serializable;

@Entity
@Data
public class Admin extends User implements Serializable {

    private String role;

    public Admin() {
    }

    public Admin(String nom, String prenom, String password, String email, String role) {
        super(nom, prenom, password, email);
        this.role = role;
    }
}
