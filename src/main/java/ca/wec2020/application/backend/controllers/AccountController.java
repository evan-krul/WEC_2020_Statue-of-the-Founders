package ca.wec2020.application.backend.controllers;

import ca.wec2020.application.backend.controllers.security.UserDetailsImpl;
import ca.wec2020.application.backend.models.*;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.persistence.*;
import java.sql.Date;
import java.util.*;

public class AccountController {
    private static EntityManager entityManager;

    public static List<Account> getAccountsForUser() {
        UserDetailsImpl user_impl = (UserDetailsImpl)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        System.out.println(user_impl.getUsername());
//        User user = user_impl.getUser();

        EntityManager entityManager = EntityManagerFactory.entityManagerFactory().createEntityManager();
//        todo make this show real user
        TypedQuery<Account> query = entityManager.createQuery("SELECT a FROM Account a", Account.class);
        return query.getResultList();
    }


    public static void makeTransfer(Account origin, Account destination, double amount) {
        Transaction withdraw = new Transaction();
        withdraw.setType("Withdraw");
        withdraw.setTitle("Transfer to: " + destination.getAccount_name());
        withdraw.setDate(java.sql.Date.valueOf(TimeController.getInstance().getDate()));
        withdraw.setCurrency("CAD");
        withdraw.setAmount(amount);

        Transaction deposit = new Transaction();
        withdraw.setType("Deposit");
        withdraw.setTitle("Transfer from: " + origin.getAccount_name());
        withdraw.setDate(java.sql.Date.valueOf(TimeController.getInstance().getDate()));
        withdraw.setCurrency("CAD");
        withdraw.setAmount(amount);

        List<Transaction> transactions_o = origin.getTransactions();
        transactions_o.add(deposit);
        origin.setTransactions(transactions_o);

        List<Transaction> transactions_w = destination.getTransactions();
        transactions_w.add(withdraw);
        destination.setTransactions(transactions_w);


        //CONNECT TO THE DATABASE
        entityManager = EntityManagerFactory.entityManagerFactory().createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.merge(origin);
        entityManager.getTransaction().commit();
        entityManager.close();

//        CONNECT TO THE DATABASE
        entityManager = EntityManagerFactory.entityManagerFactory().createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.merge(destination);
        entityManager.merge(transactions_w);
        entityManager.getTransaction().commit();
        entityManager.close();

    }

    public static void withdraw(Account origin, String title, double amount) {
        Transaction withdraw = new Transaction();
        withdraw.setType("Withdrawl");
        withdraw.setTitle(title);
        withdraw.setDate(java.sql.Date.valueOf(TimeController.getInstance().getDate()));
        withdraw.setCurrency("CAD");
        withdraw.setAmount(amount);
        origin.getTransactions().add(withdraw);

        //CONNECT TO THE DATABASE
        entityManager = EntityManagerFactory.entityManagerFactory().createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.merge(origin);
        entityManager.merge(withdraw);
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    public static void updateLimit(Account account, Double value) {
        List<AccountRestriction> accountRestrictions = account.getAccountRestrictions();
        AccountRestriction accountRestriction  = accountRestrictions.stream().filter(r->r.getAr_key().equals("MAX")).findFirst().orElse(null);
        assert accountRestriction != null;
        System.out.println(accountRestrictions);
        accountRestriction.setValue(value.toString());
        account.setAccountRestrictions(Collections.singletonList(accountRestriction));

        entityManager = EntityManagerFactory.entityManagerFactory().createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.merge(account);
        entityManager.getTransaction().commit();
        entityManager.close();
    }
}
