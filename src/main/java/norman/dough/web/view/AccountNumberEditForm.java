package norman.dough.web.view;

import norman.dough.domain.Account;
import norman.dough.domain.AccountNumber;
import norman.dough.exception.NotFoundException;
import norman.dough.service.AccountService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

public class AccountNumberEditForm {
    private static final Logger LOGGER = LoggerFactory.getLogger(AccountNumberEditForm.class);
    private AccountService accountService;
    private Long id;
    private Integer version = 0;
    @NotNull(message = "Account may not be blank.")
    private Long accountId;
    private String account;
    @NotBlank(message = "Number may not be blank.")
    @Size(max = 50, message = "Number may not be over {max} characters long.")
    private String number;
    @NotNull(message = "Effective Date may not be blank.")
    @DateTimeFormat(pattern = "M/d/yyyy")
    private Date effectiveDate;

    public AccountNumberEditForm() {
    }

    public AccountNumberEditForm(AccountNumber entity) {
        id = entity.getId();
        version = entity.getVersion();
        if (entity.getAccount() != null) {
            accountId = entity.getAccount().getId();
            account = entity.getAccount().toString();
        }
        number = entity.getNumber();
        effectiveDate = entity.getEffectiveDate();
    }

    public AccountNumberEditForm(Account parent) {
        accountId = parent.getId();
        account = parent.toString();
    }

    public AccountNumber toEntity() throws NotFoundException {
        AccountNumber entity = new AccountNumber();
        entity.setId(id);
        entity.setVersion(version);
        if (accountId != null) {
            Account account = accountService.findById(accountId);
            entity.setAccount(account);
        }
        entity.setNumber(StringUtils.trimToNull(number));
        entity.setEffectiveDate(effectiveDate);
        return entity;
    }

    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public String getAccount() {
        return account;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Date getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(Date effectiveDate) {
        this.effectiveDate = effectiveDate;
    }
}
