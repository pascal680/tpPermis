package me.pascal.tpPermis.model;

import lombok.Data;
import org.apache.tomcat.jni.Local;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Data
public class Permis implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idPermis;

    private String typePermis;

    private LocalDate datePermis;
    private LocalDate dateExpirationPermis;



    public Permis() {

    }

    public Permis(String typePermis) {
        this.typePermis = typePermis;
        if (typePermis.equalsIgnoreCase("test")){
            datePermis = LocalDate.now();
            dateExpirationPermis = LocalDate.now().plusDays(14);
        } else if(typePermis.equalsIgnoreCase("vaccin")){
            datePermis = LocalDate.now();
            dateExpirationPermis = LocalDate.now().plusMonths(6);
        }
    }


}
