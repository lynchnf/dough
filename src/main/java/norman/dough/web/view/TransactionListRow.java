package norman.dough.web.view;

import norman.dough.domain.Transaction;
import norman.dough.domain.Statement;
import norman.dough.domain.Category;
import norman.dough.domain.Transaction;

import java.math.BigDecimal;
import java.util.Date;

public class TransactionListRow {
    private Long id;
    private Integer version;
    private String statement;
    private String name;
    private String memo;
    private Date postDate;
    private BigDecimal amount;
    private String checkNumber;
    private String category;
    private String transfer;
    private Boolean voided;

    public TransactionListRow(Transaction entity) {
        id = entity.getId();
        version = entity.getVersion();
        statement = entity.getStatement() == null ? null : entity.getStatement().toString();
        name = entity.getName();
        memo = entity.getMemo();
        postDate = entity.getPostDate();
        amount = entity.getAmount();
        checkNumber = entity.getCheckNumber();
        category = entity.getCategory() == null ? null : entity.getCategory().toString();
        transfer = entity.getTransfer() == null ? null : entity.getTransfer().toString();
        voided = entity.getVoided();
    }

    public Long getId() {
        return id;
    }

    public Integer getVersion() {
        return version;
    }

    public String getStatement() {
        return statement;
    }

    public String getName() {
        return name;
    }

    public String getMemo() {
        return memo;
    }

    public Date getPostDate() {
        return postDate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getCheckNumber() {
        return checkNumber;
    }

    public String getCategory() {
        return category;
    }

    public String getTransfer() {
        return transfer;
    }

    public Boolean getVoided() {
        return voided;
    }
}
