package norman.dough.web.view;

import norman.dough.domain.Transaction;
import norman.dough.domain.Statement;
import norman.dough.domain.Category;
import norman.dough.domain.Transaction;

import java.math.BigDecimal;
import java.util.Date;

public class TransactionView {
    private Long id;
    private Integer version;
    private Long statementId;
    private String statement;
    private String name;
    private String memo;
    private Date postDate;
    private BigDecimal amount;
    private String checkNumber;
    private Long categoryId;
    private String category;
    private Long transferId;
    private String transfer;
    private Boolean voided;

    public TransactionView(Transaction entity) {
        id = entity.getId();
        version = entity.getVersion();
        if (entity.getStatement() != null) {
            statementId = entity.getStatement().getId();
            statement = entity.getStatement().toString();
        }
        name = entity.getName();
        memo = entity.getMemo();
        postDate = entity.getPostDate();
        amount = entity.getAmount();
        checkNumber = entity.getCheckNumber();
        if (entity.getCategory() != null) {
            categoryId = entity.getCategory().getId();
            category = entity.getCategory().toString();
        }
        if (entity.getTransfer() != null) {
            transferId = entity.getTransfer().getId();
            transfer = entity.getTransfer().toString();
        }
        voided = entity.getVoided();
    }

    public Long getId() {
        return id;
    }

    public Integer getVersion() {
        return version;
    }

    public Long getStatementId() {
        return statementId;
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

    public Long getCategoryId() {
        return categoryId;
    }

    public String getCategory() {
        return category;
    }

    public Long getTransferId() {
        return transferId;
    }

    public String getTransfer() {
        return transfer;
    }

    public Boolean getVoided() {
        return voided;
    }
}
