package ca.wec2020.application.backend.models;

import javax.persistence.*;

@Entity
@Table(name = "account_owns")
public class OwnsPermission {
    @EmbeddedId
    OwnsPermissionKey ao_id;

    @ManyToOne
    @MapsId("a_id")
    @JoinColumn(name = "a_id")
    Account account;

    @ManyToOne
    @MapsId("id")
    @JoinColumn(name = "id")
    User user;

    private String permission;

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }
}
