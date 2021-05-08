package me.pascal.tpPermis.controller;

import me.pascal.tpPermis.model.Citoyen;
import me.pascal.tpPermis.service.AppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin (origins = "http://localhost:4220")
public class PermisSanteController {
    @Autowired
    AppService service;

    @RequestMapping(value = "/permisSante/login/{email}/{password}", method = RequestMethod.GET)
    public Citoyen login(@PathVariable String email, @PathVariable String password){
        return service.login(email, password);
    }

    @RequestMapping(value = "/permisSante/{email}", method = RequestMethod.GET)
    public boolean loginExist(@PathVariable String email){
        return service.isLoginExist(email);
    }

//    @PutMapping(value = "/permisSante/update")
//    public Citoyen update(@RequestBody Citoyen citoyen){
//        return service.addCitoyen(citoyen);
//    }

    @RequestMapping(value = "/permisSante/addCitoyen", method = RequestMethod.POST)
    public Citoyen subscribe(@RequestBody Citoyen citoyen){
        return service.addCitoyen(citoyen);
    }

    @PostMapping(path = "/permisSante/requestPermis")
    public boolean requestPermis(@RequestBody Citoyen citoyen){
        return service.requestPermis(citoyen);
    }

    @PostMapping(path = "/permisSante/sendEmail")
    public boolean sendEmail(@RequestBody Citoyen citoyen){
        return service.generateAll(citoyen);
    }

    @PostMapping(path = "/permisSante/addEnfant/{numAssMalParent}")
    public boolean addEnfant(@PathVariable String numAssMalParent, @RequestBody Citoyen enfant){
        return service.addEnfant(numAssMalParent, enfant);
    }

}
