package norman.dough.web.view;

import norman.dough.domain.Account;
import norman.dough.domain.Statement;
import norman.dough.exception.NotFoundException;
import norman.dough.service.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

public class StatementEditForm {
    private static final Logger LOGGER = LoggerFactory.getLogger(StatementEditForm.class);
    private AccountService accountService;
    private Long id;
    private Integer version = 0;
    @NotNull(message = "Account may not be blank.")
    private Long accountId;
    private String account;
    @NotNull(message = "Closing Date may not be blank.")
    @DateTimeFormat(pattern = "M/d/yyyy")
    private Date closeDate;
    @Digits(integer = 7, fraction = 2, message = "New Balance value out of bounds. (<{integer} digits>.<{fraction} digits> expected)")
    @NumberFormat(style = NumberFormat.Style.CURRENCY)
    private BigDecimal closeBalance;
    @Digits(integer = 7, fraction = 2, message = "Minimum Payment Due value out of bounds. (<{integer} digits>.<{fraction} digits> expected)")
    @NumberFormat(style = NumberFormat.Style.CURRENCY)
    private BigDecimal minPayment;
    @DateTimeFormat(pattern = "M/d/yyyy")
    private Date dueDate;
    @Digits(integer = 7, fraction = 2, message = "Previous Balance value out of bounds. (<{integer} digits>.<{fraction} digits> expected)")
    @NumberFormat(style = NumberFormat.Style.CURRENCY)
    private BigDecimal openBalance;
    @Digits(integer = 7, fraction = 2, message = "Payments, Credits value out of bounds. (<{integer} digits>.<{fraction} digits> expected)")
    @NumberFormat(style = NumberFormat.Style.CURRENCY)
    private BigDecimal credits;
    @Digits(integer = 7, fraction = 2, message = "Purchases value out of bounds. (<{integer} digits>.<{fraction} digits> expected)")
    @NumberFormat(style = NumberFormat.Style.CURRENCY)
    private BigDecimal debits;
    @Digits(integer = 7, fraction = 2, message = "Fees Charged value out of bounds. (<{integer} digits>.<{fraction} digits> expected)")
    @NumberFormat(style = NumberFormat.Style.CURRENCY)
    private BigDecimal fees;
    @Digits(integer = 7, fraction = 2, message = "Interest Charged value out of bounds. (<{integer} digits>.<{fraction} digits> expected)")
    @NumberFormat(style = NumberFormat.Style.CURRENCY)
    private BigDecimal interest;

    public StatementEditForm() {
    }

    public StatementEditForm(Statement entity) {
        id = entity.getId();
        version = entity.getVersion();
        if (entity.getAccount() != null) {
            accountId = entity.getAccount().getId();
            account = entity.getAccount().toString();
        }
        closeDate = entity.getCloseDate();
        closeBalance = entity.getCloseBalance();
        minPayment = entity.getMinPayment();
        dueDate = entity.getDueDate();
        openBalance = entity.getOpenBalance();
        credits = entity.getCredits();
        debits = entity.getDebits();
        fees = entity.getFees();
        interest = entity.getInterest();
    }

    public StatementEditForm(Account parent) {
        accountId = parent.getId();
        account = parent.toString();
    }

    public Statement toEntity() throws NotFoundException {
        Statement entity = new Statement();
        entity.setId(id);
        entity.setVersion(version);
        if (accountId != null) {
            Account account = accountService.findById(accountId);
            entity.setAccount(account);
        }
        entity.setCloseDate(closeDate);
        entity.setCloseBalance(closeBalance);
        entity.setMinPayment(minPayment);
        entity.setDueDate(dueDate);
        entity.setOpenBalance(openBalance);
        entity.setCredits(credits);
        entity.setDebits(debits);
        entity.setFees(fees);
        entity.setInterest(interest);
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

    public Date getCloseDate() {
        return closeDate;
    }

    public void setCloseDate(Date closeDate) {
        this.closeDate = closeDate;
    }

    public BigDecimal getCloseBalance() {
        return closeBalance;
    }

    public void setCloseBalance(BigDecimal closeBalance) {
        this.closeBalance = closeBalance;
    }

    public BigDecimal getMinPayment() {
        return minPayment;
    }

    public void setMinPayment(BigDecimal minPayment) {
        this.minPayment = minPayment;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public BigDecimal getOpenBalance() {
        return openBalance;
    }

    public void setOpenBalance(BigDecimal openBalance) {
        this.openBalance = openBalance;
    }

    public BigDecimal getCredits() {
        return credits;
    }

    public void setCredits(BigDecimal credits) {
        this.credits = credits;
    }

    public BigDecimal getDebits() {
        return debits;
    }

    public void setDebits(BigDecimal debits) {
        this.debits = debits;
    }

    public BigDecimal getFees() {
        return fees;
    }

    public void setFees(BigDecimal fees) {
        this.fees = fees;
    }

    public BigDecimal getInterest() {
        return interest;
    }

    public void setInterest(BigDecimal interest) {
        this.interest = interest;
    }
}
