package norman.dough.web.view;

import norman.dough.domain.Statement;

import java.math.BigDecimal;
import java.util.Date;

public class StatementView {
    private Long id;
    private Integer version;
    private Long accountId;
    private String account;
    private Date closeDate;
    private BigDecimal closeBalance;
    private BigDecimal minPayment;
    private Date dueDate;
    private BigDecimal openBalance;
    private BigDecimal credits;
    private BigDecimal debits;
    private BigDecimal fees;
    private BigDecimal interest;

    public StatementView(Statement entity) {
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

    public Date getCloseDate() {
        return closeDate;
    }

    public BigDecimal getCloseBalance() {
        return closeBalance;
    }

    public BigDecimal getMinPayment() {
        return minPayment;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public BigDecimal getOpenBalance() {
        return openBalance;
    }

    public BigDecimal getCredits() {
        return credits;
    }

    public BigDecimal getDebits() {
        return debits;
    }

    public BigDecimal getFees() {
        return fees;
    }

    public BigDecimal getInterest() {
        return interest;
    }
}
