package me.pascal.tpPermis;

import me.pascal.tpPermis.model.Permis;
import me.pascal.tpPermis.model.Citoyen;
import me.pascal.tpPermis.repositories.PermisRepository;
import me.pascal.tpPermis.repositories.CitoyenRepository;
import static org.junit.jupiter.api.Assertions.*;

import me.pascal.tpPermis.service.AppService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;

import java.io.File;
import java.util.Arrays;

@DataJpaTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ComponentScan(basePackages = "me.pascal.tpPermis.service")
public class AppPermisTests {
    @Autowired
    private AppService service;


    @Autowired
    private CitoyenRepository citoyenRepo;
    @Autowired
    private PermisRepository permisRepo;

    @BeforeAll
    public void insertData(){

        Citoyen c1 = new Citoyen("Bourgoin", "Pascal", "test","pascalb2706@gmail.com","12345", 'M', 20, "5149100627", "Lasalle");
        Citoyen c2 = new Citoyen("Tata", "Toto", "toto123","toto123@test.com","56789", 'M', 20, "5149100628", "Lasalle");

        Citoyen enfant1 = new Citoyen("Eponge", "Bob", "bob123","bob123@gmail.com","12345", 'M', 14, "5149101245", "Lasalle");
        Citoyen enfant2 = new Citoyen("Star", "Patrick", "pat123","pat123@gmail.com","34567", 'M', 12, "5141201245", "Lasalle");
        c2.getEnfants().add(enfant1);
        c2.getEnfants().add(enfant2);
        Permis p3 = new Permis(); p3.setTypePermis("TEST");
        Permis p4 = new Permis(); p4.setTypePermis("TEST");
        Permis p5 = new Permis(); p5.setTypePermis("TEST");


        permisRepo.saveAll(Arrays.asList(p3,p4,p5));
        citoyenRepo.saveAll(Arrays.asList(c1, c2));
    }

    @Test
    public void testFindAllCitoyens(){
        assertEquals(citoyenRepo.findAll().size(), 1);
        assertNotNull(citoyenRepo.findAll());
    }

    @Test
    public void testFindAllPermis(){
        assertEquals(permisRepo.findAll().size(), 3);
    }

    @Test
    public void testDeletePermis(){
        permisRepo.deleteById(1);
        assertEquals(permisRepo.findAll().size(), 2);
    }

    @Test
    public void testDeleteCitoyen(){
        citoyenRepo.deleteByEmail("pascalb2706@gmail.com");
        assertEquals(citoyenRepo.findAll().size(), 0);

    }

    @Test
    public void testLogin(){
        assertNotNull(service.login("toto123@test.com", "toto123"));
    }

    @Test
    public void testLoginExist(){
        assertTrue(service.isLoginExist("pascalb2706@gmail.com"));
    }

    @Test
    public void testRequestPermisTousEnfantsFromParent(){
        int count = 0;
        service.requestPermisTousEnfantsFromParent(citoyenRepo.findCitoyenByEmailIgnoreCase("toto123@test.com"));
        for(Citoyen enfant :citoyenRepo.findCitoyenByEmailIgnoreCase("toto123@test.com").getEnfants()){
            if(enfant.getPermis() != null){
                count++;
            }

        }
        assertEquals(count, 2);
    }

    @Test
    public void testRequestPermisSeulEnfantFromParent(){
        int count = 0;
        service.requestPermisSeulEnfantsFromParent(citoyenRepo.findCitoyenByEmailIgnoreCase("toto123@test.com"), "12345");
        for(Citoyen enfant :citoyenRepo.findCitoyenByEmailIgnoreCase("toto123@test.com").getEnfants()){
            if(enfant.getPermis() != null){
                count++;
            }

        }
        assertEquals(count, 1);
    }


    @Test
    public void testGenerateQr() throws Exception{
        service.generateQR("test", "C:/Test/test.PNG");
        File qrCode = new File("C:/Test/test.PNG");

        assertTrue(qrCode.exists());
        qrCode.delete();
    }

    @Test
    public void testGeneratePdf() throws Exception{
        service.generateQR("test", "C:/Test/test.PNG");
        service.generatePDF("C:/Test/test.pdf","C:/Test/test.PNG");
        File qrCode = new File("C:/Test/test.PNG");
        File pdf = new File("C:/Test/test.pdf");

        assertTrue(pdf.exists());

        qrCode.delete();
        pdf.delete();
    }

    @Test
    public void testAddEnfant(){
        Citoyen parent = citoyenRepo.findCitoyenByEmailIgnoreCase("pascalb2706@gmail.com");
        Citoyen enfant =  new Citoyen( "Star1", "Patrick1", "pat1234","pat1234@gmail.com","34568", 'M', 12, "5141201245", "Lasalle");
        service.addEnfant("12345", enfant);
        assertEquals(parent.getEnfants().size(), 1);
    }



}
