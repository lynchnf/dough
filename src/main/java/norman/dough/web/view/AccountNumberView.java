package norman.dough.web.view;

import norman.dough.domain.AccountNumber;
import norman.dough.domain.Account;

import java.math.BigDecimal;
import java.util.Date;

public class AccountNumberView {
    private Long id;
    private Integer version;
    private Long accountId;
    private String account;
    private String number;
    private Date effectiveDate;

    public AccountNumberView(AccountNumber entity) {
        id = entity.getId();
        version = entity.getVersion();
        if (entity.getAccount() != null) {
            accountId = entity.getAccount().getId();
            account = entity.getAccount().toString();
        }
        number = entity.getNumber();
        effectiveDate = entity.getEffectiveDate();
    }

    public Long getId() {
        return id;
    }

    public Integer getVersion() {
        return version;
    }

    public Long getAccountId() {
        return accountId;
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
