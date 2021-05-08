package me.pascal.tpPermis.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.qrcode.QRCodeWriter;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import me.pascal.tpPermis.model.Citoyen;
import me.pascal.tpPermis.model.Permis;
import me.pascal.tpPermis.model.User;
import me.pascal.tpPermis.repositories.PermisRepository;
import me.pascal.tpPermis.repositories.CitoyenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.mail.internet.MimeMessage;
import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.time.LocalDate;

@Service
public class AppService implements ServiceData {


    @Autowired
    private CitoyenRepository citoyenRepository;
    @Autowired
    private PermisRepository permisRepository;

    @Autowired(required = false)
    private JavaMailSender mailSender;

    @Autowired
    private Environment environment;


    public Citoyen login(String str1, String str2) {
        return citoyenRepository.findCitoyenByEmailIgnoreCaseAndPassword(str1, str2);
    }

    public boolean isLoginExist(String str) {
        return citoyenRepository.findCitoyenByEmailIgnoreCase(str) != null;
    }


    public void generateQR(String data, String filePath) throws Exception {
        Path path = FileSystems.getDefault().getPath(filePath + QR_EXTENSION);
        QRCodeWriter qr = new QRCodeWriter();
        MatrixToImageWriter.writeToPath(qr.encode(data, BarcodeFormat.QR_CODE, QR_CODE_WIDTH,
                QR_CODE_HEIGHT),
                "PNG", path);
    }

    public void generatePDF(String filePath, String filePathQR) throws Exception {
        PdfWriter writer = new PdfWriter(filePath + PDF_EXTENSION);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        Image image = new Image(ImageDataFactory.create(filePathQR + QR_EXTENSION));

        Paragraph p = new Paragraph("Bonjour\n")
                .add(" Voici votre code QR de permis Covid")
                .add(image);
        document.add(p);

        document.close();
    }

    public void sendEmail(String mailTo, String subject, String body, String filePathQR, String filePathPDF) throws Exception {

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(mailTo);
        helper.setSubject(subject);
        helper.setText(body);

        helper.addAttachment("QR_CODE.PNG", new File(filePathQR));
        helper.addAttachment("QR_PDF.pdf", new File(filePathPDF));

        mailSender.send(message);
    }

    public boolean requestPermis(Citoyen citoyen) {
        if (getCitoyenValidity(citoyen.getNumAssMal())) {
            if (citoyen.getPermis() != null) {
                renewPermis(citoyen);
                //Generate Qr, pdf et send email
                generateAll(citoyen);
                citoyenRepository.save(citoyen);
                return true;
            } else if (citoyen.getPermis() == null) {
                String typePermisCitoyen = getTypeByNumAssMaladie(citoyen.getNumAssMal()).toLowerCase();
                if (typePermisCitoyen == null) {
                    return false;
                }
                citoyen.setPermis(new Permis(typePermisCitoyen));
                generateAll(citoyen);
                citoyenRepository.save(citoyen);
                return true;
            }
        }
        return false;
    }

    public boolean generateAll(Citoyen citoyen) {
        String filePath = FILE_PATH_BASE + citoyen.getNumAssMal();
        if (citoyen.getPermis() != null) {
            try {
                generateQR(getDataQr(citoyen), filePath);
                generatePDF(filePath, filePath);
                sendEmail(citoyen.getEmail(), "Permis Covid",
                        "Voici votre permis de covid pour laquelle vous avez fait une demande",
                        filePath + QR_EXTENSION, filePath + PDF_EXTENSION);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("file path incorrect");
                return false;
            }
        } else {
            return false;
        }
    }

    public String getDataQr(Citoyen citoyen) {
        return "Numero d'assurance maladie: " + citoyen.getNumAssMal()
                + "\nPrenom,Nom: " + citoyen.getPrenom() + ", " + citoyen.getNom()
                + "\nType permis: " + citoyen.getPermis().getTypePermis()
                + "\nDate permis issue: " + citoyen.getPermis().getDatePermis()
                + "\nDate expiration permis: " + citoyen.getPermis().getDateExpirationPermis();
    }

    public Citoyen addCitoyen(Citoyen citoyen) {
        return citoyenRepository.save(citoyen);
    }

    public boolean addEnfant(String numAssMalParent, Citoyen enfant) {
        Citoyen parent = citoyenRepository.findByNumAssMal(numAssMalParent);
        if (parent.getAge() > 16) {
            parent.getEnfants().add(enfant);
            citoyenRepository.save(enfant);
            citoyenRepository.save(parent);

            return true;
        }
        return false;
    }

    public boolean renewPermis(Citoyen citoyen) {
        if (citoyen.getPermis().getTypePermis().equalsIgnoreCase("test")) {
            Permis permis = new Permis("test");
            citoyen.setPermis(permis);
            return true;
        } else if (citoyen.getPermis().getTypePermis().equalsIgnoreCase("vaccin")) {
            Permis permis = new Permis("vaccin");
            citoyen.setPermis(permis);
            return true;
        }
        return false;
    }


    public boolean requestPermisTousEnfantsFromParent(Citoyen citoyen) {
        boolean flag = false;
        if (citoyen.getEnfants().size() > 0) {
            for (Citoyen enfant : citoyen.getEnfants()) {
                if (enfant.getAge() <= 16) {
                    //Verifier que les enfants sont encore du bon age
                    flag = requestPermis(enfant);
                }
            }
            return flag;
        }
        return flag;
    }

    public boolean requestPermisSeulEnfantsFromParent(Citoyen citoyen, String numAssMalEnfant) {
        if (citoyen.getEnfants().size() > 0) {
            for (Citoyen enfant : citoyen.getEnfants()) {
                if (enfant.getNumAssMal().equals(numAssMalEnfant)) {
                    if (enfant.getAge() <= 16) {
                        //Verifier que les enfants sont encore du bon age
                        return requestPermis(enfant);
                    }
                }
            }
        }
        return false;
    }

    public boolean getCitoyenValidity(String numAssMal) {
        final String url = "http://localhost:9393/ministere/";
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Boolean> responseEntity = restTemplate.getForEntity(url + numAssMal, Boolean.class);
        return responseEntity.getBody();
    }

    public String getTypeByNumAssMaladie(String numAssMal) {
        final String url = "http://localhost:9393/ministere/typeVaccin/";
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(url + numAssMal, String.class);
        return responseEntity.getBody();
    }


}
