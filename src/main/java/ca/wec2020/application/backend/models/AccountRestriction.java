package ca.wec2020.application.backend.models;

import javax.persistence.*;

@Entity
@Table(name = "account_restrictions")
public class AccountRestriction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer ar_id;

    private String ar_key;
    private String value;

    public Integer getAr_id() {
        return ar_id;
    }

    public String getAr_key() {
        return ar_key;
    }

    public void setAr_key(String ar_key) {
        this.ar_key = ar_key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
