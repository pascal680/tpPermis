package me.pascal.tpPermis.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
public class Citoyen extends User implements Serializable {

    private String numAssMal;
    private char sexe;
    private int age;
    private String numTelephone;
    private String villeResidence;

    @OneToMany
    private Set<Citoyen> enfants = new HashSet<>();

    @OneToOne(cascade = CascadeType.ALL, targetEntity = Permis.class)
    @JoinColumn(name = "ID_PERMIS")
    private Permis permis;


    public Citoyen() {
        super();
    }

    public Citoyen(String nom, String prenom, String password, String email, String numAssMal, char sexe, int age, String numTelephone, String villeResidence) {
        super(nom, prenom, password, email);
        this.numAssMal = numAssMal;
        this.sexe = sexe;
        this.age = age;
        this.numTelephone = numTelephone;
        this.villeResidence = villeResidence;
    }



}
