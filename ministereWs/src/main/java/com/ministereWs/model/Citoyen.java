package com.ministereWs.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
public class Citoyen {
    @Id
    private int id;
    private String name;
    private String numAssMal;
    private boolean valid;
    private String type;
}
