package ca.wec2020.application.backend.controllers.csv;

import ca.wec2020.application.MainView;
import ca.wec2020.application.backend.controllers.EntityManagerFactory;
import ca.wec2020.application.backend.models.Transaction;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;

import javax.persistence.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.Scanner;

@Route(value = "uploadCSV", layout = MainView.class)
@PageTitle("Welcome")
@CssImport(value = "styles/views/style.css", include = "lumo-badge")
@Secured("ROLE_User")

public class CSVUploadProcessor {
    private EntityManager entityManager;
    private MemoryBuffer buffer;
    public CSVUploadProcessor(MemoryBuffer buffer) {
        this.buffer = buffer;
    }

    private Date date;
    private String type;
    private float amount;
    private String currency;
    private String title;

    public void insertSavingsDB(){
        System.out.println("SAVINGS");
        //READ IN CSV
        BufferedReader input = new BufferedReader(new InputStreamReader(buffer.getInputStream()));
        String line = "";
        int colCount = 0;
        while(true){
            try {
                if (!((line = input.readLine()) != null)) break;
            } catch (IOException e) {
                e.printStackTrace();
            }

            //PARSE THE DATA
            colCount++;
            Scanner scan = new Scanner(line).useDelimiter(",");
            String str_date = scan.next();
            type = scan.next();
            String str_amount = scan.next();
            title = scan.next();

            if(colCount != 1){
                //CONVERT STRINGS TO VALUES
                stringToValues(str_date, str_amount);
            }
            System.out.println(date + " " + type + " " + amount + " " + title);
        }
        try {
            input.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void insertChequingDB(){
        //READ IN CSV
        BufferedReader input = new BufferedReader(new InputStreamReader(buffer.getInputStream()));
        String line = "";
        int colCount = 0;
        while(true){
            try {
                if (!((line = input.readLine()) != null)) break;
            } catch (IOException e) {
                e.printStackTrace();
            }

            //PARSE THE DATA
            colCount++;
            Scanner scan = new Scanner(line).useDelimiter(",");
            String str_date = scan.next();
            type = scan.next();
            String str_amount = scan.next();
            title = scan.next();
            currency = "CAD";

            if(colCount != 1){
                //CONVERT STRINGS TO VALUES
                stringToValues(str_date, str_amount);

                Transaction transaction = new Transaction();
                transaction.setDate(date);
                transaction.setType(type);
                transaction.setAmount(amount);
                transaction.setCurrency(currency);
                transaction.setTitle(title);

                //CONNECT TO THE DATABASE
                entityManager = EntityManagerFactory.entityManagerFactory().createEntityManager();
                entityManager.getTransaction().begin();
                entityManager.merge(transaction);
                entityManager.getTransaction().commit();
                entityManager.close();
            }
        }
        try {
            input.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void stringToValues(String sDate, String sAmount){
        date = Date.valueOf(sDate);
        amount = Float.parseFloat(sAmount);
    }
}
