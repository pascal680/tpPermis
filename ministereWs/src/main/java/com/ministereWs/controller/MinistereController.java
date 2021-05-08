package com.ministereWs.controller;

import com.ministereWs.Repository.CitoyenRepository;
import com.ministereWs.service.MinistereService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin (origins = "http://localhost:4220")
public class MinistereController {

    @Autowired
    CitoyenRepository citoyenRepository;

    @Autowired
    MinistereService ministereService;

    //@RequestMapping(value = "/ministere/{numAssMal}", method = RequestMethod.GET)

    @GetMapping("/ministere/{numAssMal}")
    public boolean checkCitizenValidity(@PathVariable String numAssMal){
        return ministereService.validerNumeroAssuranceMaladie(numAssMal);
    }

    @GetMapping("/ministere/typeVaccin/{numAssMal}")
    public String getCitizenType(@PathVariable String numAssMal){
        return ministereService.getTypeCitoyen(numAssMal);
    }

//    @RequestMapping (value = "/ministere/{email && pwd}", method = RequestMethod.GET)
//    public boolean login(@PathVariable String email, @PathVariable String password){
//        if(citoyenRepository.findBy)
//    }


}
