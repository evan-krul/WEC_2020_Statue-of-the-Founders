package ca.wec2020.application.backend.controllers.csv;

import ca.wec2020.application.MainView;
import ca.wec2020.application.backend.controllers.AccountTransactionsController;
import ca.wec2020.application.backend.controllers.EntityManagerFactory;
import ca.wec2020.application.backend.controllers.security.UserDetailsImpl;
import ca.wec2020.application.backend.models.Account;
import ca.wec2020.application.backend.models.OwnsPermission;
import ca.wec2020.application.backend.models.Transaction;
import ca.wec2020.application.backend.models.User;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.persistence.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;
import java.sql.Date;
import java.util.*;

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

    private Account account;
    private AccountTransactionsController accTran;
    private List<Transaction> listTrans;
    private String accountType;

    public void insertSavingsDB(String accType){
        accountType = accType;
        listTrans = new ArrayList<Transaction>();

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

                listTrans.add(transaction);
            }
        }
        account = new Account();
        account.setAccount_name(accountType);
        account.setIs_investment(false);
        account.setIs_locked(false);

//        UserDetailsImpl user_impl = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        User user = user_impl.getUser();
//        Set<OwnsPermission> ownsPermissionSet = new HashSet<>();
//        ownsPermissionSet.add(new OwnsPermission(account, user, "Owner"));
//        account.setOwnsPermissions(ownsPermissionSet);

        account.setTransactions(listTrans);
        accTran = new AccountTransactionsController(account);
        accTran.connectAccounts();

        try {
            input.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void insertChequingDB(String accType){
        accountType = accType;
        listTrans = new ArrayList<Transaction>();
//        UserDetailsImpl user_impl = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        User user = user_impl.getUser();

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

                listTrans.add(transaction);
            }
        }

        account = new Account();
        account.setAccount_name(accountType);
        account.setIs_investment(false);
        account.setIs_locked(false);

//        UserDetailsImpl user_impl = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        User user = user_impl.getUser();
//        Set<OwnsPermission> ownsPermissionSet = new HashSet<>();
//        ownsPermissionSet.add(new OwnsPermission(account, user, "Owner"));
//        account.setOwnsPermissions(ownsPermissionSet);

        account.setTransactions(listTrans);
        accTran = new AccountTransactionsController(account);
        accTran.connectAccounts();

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
