package ca.wec2020.application.backend.models;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
class OwnsPermissionKey implements Serializable {

    @Column(name = "a_id")
    int accountId;

    @Column(name = "id")
    int userId;

    public OwnsPermissionKey() {
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
