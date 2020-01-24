package ca.wec2020.application.backend.controllers;

import ca.wec2020.application.backend.models.Account;
import ca.wec2020.application.backend.models.Transaction;

import javax.persistence.EntityManager;
import java.util.List;

public class AccountTransactionsController {
    private EntityManager entityManager;
    private Account acc;

    public AccountTransactionsController(Account a){
        acc = a;
    }

    public void connectAccounts(){
        //CONNECT TO THE DATABASE
        entityManager = EntityManagerFactory.entityManagerFactory().createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.merge(acc);
        entityManager.getTransaction().commit();
        entityManager.close();
    }
}
