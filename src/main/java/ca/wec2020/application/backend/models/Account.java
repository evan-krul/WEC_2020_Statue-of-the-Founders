package ca.wec2020.application.backend.models;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "account")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer a_id;
    private boolean is_locked;
    private Date lock_release;
    private boolean is_investment;
    private String account_name;
    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Transaction> transactions;
    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<AccountRestriction> accountRestrictions;
    @OneToMany(mappedBy = "account")
    Set<OwnsPermission> ownsPermissions;

    public Integer getA_id() {
        return a_id;
    }

    public boolean isIs_locked() {
        return is_locked;
    }

    public void setIs_locked(boolean is_locked) {
        this.is_locked = is_locked;
    }

    public Date getLock_release() {
        return lock_release;
    }

    public void setLock_release(Date lock_release) {
        this.lock_release = lock_release;
    }

    public boolean isIs_investment() {
        return is_investment;
    }

    public void setIs_investment(boolean is_investment) {
        this.is_investment = is_investment;
    }

    public String getAccount_name() {
        return account_name;
    }

    public void setAccount_name(String account_name) {
        this.account_name = account_name;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public List<AccountRestriction> getAccountRestrictions() {
        return accountRestrictions;
    }

    public void setAccountRestrictions(List<AccountRestriction> accountRestrictions) {
        this.accountRestrictions = accountRestrictions;
    }

    public Set<OwnsPermission> getOwnsPermissions() {
        return ownsPermissions;
    }

    public void setOwnsPermissions(Set<OwnsPermission> ownsPermissions) {
        this.ownsPermissions = ownsPermissions;
    }
}
