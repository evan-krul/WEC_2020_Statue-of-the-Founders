package ca.wec2020.application.backend.controllers;

import ca.wec2020.application.backend.controllers.security.UserDetailsImpl;
import ca.wec2020.application.backend.models.Account;
import ca.wec2020.application.backend.models.OwnsPermission;
import ca.wec2020.application.backend.models.Transaction;
import ca.wec2020.application.backend.models.User;
import org.springframework.security.core.context.SecurityContextHolder;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AccountController {
    public static List<Account> getAccountsForUser() {
        UserDetailsImpl user_impl = (UserDetailsImpl)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = user_impl.getUser();

//        TODO JPA this
        List<Account> accountsList = new ArrayList<>();
        Account account1 = new Account();
        account1.setAccount_name("Chequings");
        account1.setIs_investment(false);
        account1.setIs_locked(false);
        Set<OwnsPermission> ownsPermissionSet = new HashSet<>();
        ownsPermissionSet.add(new OwnsPermission(account1, user, "Owner"));
        account1.setOwnsPermissions(ownsPermissionSet);
        List<Transaction> transactions = new ArrayList<>();
        Transaction transaction = new Transaction();
        transaction.setAmount(12.43);
        transaction.setCurrency("CAD");
        transaction.setDate(new Date(132324324));
        transaction.setTitle("Sample 1");
        transaction.setType("Deposit");
        transactions.add(transaction);

        transaction = new Transaction();
        transaction.setAmount(312.43);
        transaction.setCurrency("CAD");
        transaction.setDate(new Date(13232434));
        transaction.setTitle("Sample 2");
        transaction.setType("Deposit");
        transactions.add(transaction);

        transaction = new Transaction();
        transaction.setAmount(3122.43);
        transaction.setCurrency("CAD");
        transaction.setDate(new Date(13232432));
        transaction.setTitle("Sample 21");
        transaction.setType("Withdrawl");
        transactions.add(transaction);

        account1.setTransactions(transactions);
        accountsList.add(account1);

        return accountsList;
    }

    public static void makeTransfer(Account origin, Account destination, double amount) {

    }
}
