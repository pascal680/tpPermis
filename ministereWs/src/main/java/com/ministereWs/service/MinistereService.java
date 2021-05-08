package com.ministereWs.service;

import com.ministereWs.Repository.CitoyenRepository;
import com.ministereWs.model.Citoyen;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MinistereService {

    @Autowired
    CitoyenRepository citoyenRepository;

    public boolean validerNumeroAssuranceMaladie(String numAssMal){
        Citoyen c = citoyenRepository.findByNumAssMal(numAssMal);
        if(c!= null){
            return c.isValid();
        }
        else {
            return false;
        }
    }

    public String getTypeCitoyen(String numAssMal){
        Citoyen c = citoyenRepository.findByNumAssMal(numAssMal);
        if(c!= null){
            return c.getType();
        }
        else {
            return null;
        }
    }

}
