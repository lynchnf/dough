package norman.dough.web.view;

import norman.dough.domain.Category;
import norman.dough.domain.Statement;
import norman.dough.domain.Transaction;
import norman.dough.exception.NotFoundException;
import norman.dough.service.CategoryService;
import norman.dough.service.StatementService;
import norman.dough.service.TransactionService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Date;

public class TransactionEditForm {
    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionEditForm.class);
    private StatementService statementService;
    private CategoryService categoryService;
    private TransactionService transferService;
    private Long id;
    private Integer version = 0;
    @NotNull(message = "Statement may not be blank.")
    private Long statementId;
    private String statement;
    @NotBlank(message = "Name may not be blank.")
    @Size(max = 100, message = "Name may not be over {max} characters long.")
    private String name;
    @Size(max = 100, message = "Memo may not be over {max} characters long.")
    private String memo;
    @NotNull(message = "Post Date may not be blank.")
    @DateTimeFormat(pattern = "M/d/yyyy")
    private Date postDate;
    @NotNull(message = "Amount may not be blank.")
    @Digits(integer = 7, fraction = 2, message = "Amount value out of bounds. (<{integer} digits>.<{fraction} digits> expected)")
    @NumberFormat(style = NumberFormat.Style.CURRENCY)
    private BigDecimal amount;
    @Size(max = 10, message = "Check Number may not be over {max} characters long.")
    private String checkNumber;
    private Long categoryId;
    private String category;
    private Long transferId;
    private String transfer;
    @NotNull(message = "Void may not be blank.")
    private Boolean voided;

    public TransactionEditForm() {
        voided = false;
    }

    public TransactionEditForm(Transaction entity) {
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

    public TransactionEditForm(Statement parent) {
        statementId = parent.getId();
        statement = parent.toString();
        voided = false;
    }

    public Transaction toEntity() throws NotFoundException {
        Transaction entity = new Transaction();
        entity.setId(id);
        entity.setVersion(version);
        if (statementId != null) {
            Statement statement = statementService.findById(statementId);
            entity.setStatement(statement);
        }
        entity.setName(StringUtils.trimToNull(name));
        entity.setMemo(StringUtils.trimToNull(memo));
        entity.setPostDate(postDate);
        entity.setAmount(amount);
        entity.setCheckNumber(StringUtils.trimToNull(checkNumber));
        if (categoryId != null) {
            Category category = categoryService.findById(categoryId);
            entity.setCategory(category);
        }
        if (transferId != null) {
            Transaction transfer = transferService.findById(transferId);
            entity.setTransfer(transfer);
        }
        entity.setVoided(voided);
        return entity;
    }

    public void setStatementService(StatementService statementService) {
        this.statementService = statementService;
    }

    public void setCategoryService(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    public void setTransferService(TransactionService transferService) {
        this.transferService = transferService;
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

    public Long getStatementId() {
        return statementId;
    }

    public void setStatementId(Long statementId) {
        this.statementId = statementId;
    }

    public String getStatement() {
        return statement;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Date getPostDate() {
        return postDate;
    }

    public void setPostDate(Date postDate) {
        this.postDate = postDate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCheckNumber() {
        return checkNumber;
    }

    public void setCheckNumber(String checkNumber) {
        this.checkNumber = checkNumber;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategory() {
        return category;
    }

    public Long getTransferId() {
        return transferId;
    }

    public void setTransferId(Long transferId) {
        this.transferId = transferId;
    }

    public String getTransfer() {
        return transfer;
    }

    public Boolean getVoided() {
        return voided;
    }

    public void setVoided(Boolean voided) {
        this.voided = voided;
    }
}
