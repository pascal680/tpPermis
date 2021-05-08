package me.pascal.tpPermis;

import me.pascal.tpPermis.model.Citoyen;
import me.pascal.tpPermis.model.Permis;
import me.pascal.tpPermis.repositories.CitoyenRepository;
import me.pascal.tpPermis.service.AppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDate;

@SpringBootApplication
public class TppermisApplication implements CommandLineRunner {

    @Autowired
    AppService service;

    @Autowired
    CitoyenRepository repository;
    public static void main(String[] args) {
        SpringApplication.run(TppermisApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

//        Citoyen c1 = new Citoyen("Bourgoin", "Pascal", "test","pascalb2706@gmail.com","DAAC5E61", 'M', 20, "5149100627", "Lasalle");
//        service.addCitoyen(c1);

//        service.sendEmail("pascalb2706@gmail.com", "covid test", "testing","B:/SCHOOL/AL/Sem_8/prog_transactionnel/TP/tpPermis/genatedData/DAAC5E61.PNG", "B:/SCHOOL/AL/Sem_8/prog_transactionnel/TP/tpPermis/genatedData/DAAC5E61.pdf");
    }
}
