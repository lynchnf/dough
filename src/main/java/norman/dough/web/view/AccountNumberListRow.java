package norman.dough.web.view;

import norman.dough.domain.AccountNumber;

import java.util.Date;

public class AccountNumberListRow {
    private Long id;
    private Integer version;
    private String account;
    private String number;
    private Date effectiveDate;

    public AccountNumberListRow(AccountNumber entity) {
        id = entity.getId();
        version = entity.getVersion();
        account = entity.getAccount() == null ? null : entity.getAccount().toString();
        number = entity.getNumber();
        effectiveDate = entity.getEffectiveDate();
    }

    public Long getId() {
        return id;
    }

    public Integer getVersion() {
        return version;
    }

    public String getAccount() {
        return account;
    }

    public String getNumber() {
        return number;
    }

    public Date getEffectiveDate() {
        return effectiveDate;
    }
}
