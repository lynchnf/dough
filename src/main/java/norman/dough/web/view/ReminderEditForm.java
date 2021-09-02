package norman.dough.web.view;

import norman.dough.domain.Reminder;
import norman.dough.domain.RecurringSchedule;
import norman.dough.domain.Account;
import norman.dough.domain.Category;
import norman.dough.domain.Transaction;
import norman.dough.exception.NotFoundException;
import norman.dough.service.RecurringScheduleService;
import norman.dough.service.AccountService;
import norman.dough.service.CategoryService;
import norman.dough.service.TransactionService;
import norman.dough.util.MiscUtils;
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

public class ReminderEditForm {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReminderEditForm.class);
    private RecurringScheduleService recurringScheduleService;
    private AccountService tentativeAccountService;
    private CategoryService tentativeCategoryService;
    private TransactionService actualTransactionService;
    private Long id;
    private Integer version = 0;
    private Long recurringScheduleId;
    private String recurringSchedule;
    @NotNull(message = "Estimated Date may not be blank.")
    @DateTimeFormat(pattern = "M/d/yyyy")
    private Date estimatedDate;
    @NotNull(message = "Estimated Amount may not be blank.")
    @Digits(integer = 7, fraction = 2, message = "Estimated Amount value out of bounds. (<{integer} digits>.<{fraction} digits> expected)")
    @NumberFormat(style = NumberFormat.Style.CURRENCY)
    private BigDecimal estimatedAmount;
    @Size(max = 255, message = "Comment may not be over {max} characters long.")
    private String comment;
    private Long tentativeAccountId;
    private String tentativeAccount;
    private Long tentativeCategoryId;
    private String tentativeCategory;
    private Long actualTransactionId;
    private String actualTransaction;
    @NotNull(message = "Skip may not be blank.")
    private Boolean skipped;

    public ReminderEditForm() {
        skipped = false;
    }

    public ReminderEditForm(Reminder entity) {
        id = entity.getId();
        version = entity.getVersion();
        if (entity.getRecurringSchedule() != null) {
            recurringScheduleId = entity.getRecurringSchedule().getId();
            recurringSchedule = entity.getRecurringSchedule().toString();
        }
        estimatedDate = entity.getEstimatedDate();
        estimatedAmount = entity.getEstimatedAmount();
        comment = entity.getComment();
        if (entity.getTentativeAccount() != null) {
            tentativeAccountId = entity.getTentativeAccount().getId();
            tentativeAccount = entity.getTentativeAccount().toString();
        }
        if (entity.getTentativeCategory() != null) {
            tentativeCategoryId = entity.getTentativeCategory().getId();
            tentativeCategory = entity.getTentativeCategory().toString();
        }
        if (entity.getActualTransaction() != null) {
            actualTransactionId = entity.getActualTransaction().getId();
            actualTransaction = entity.getActualTransaction().toString();
        }
        skipped = entity.getSkipped();
    }

    public Reminder toEntity() throws NotFoundException {
        Reminder entity = new Reminder();
        entity.setId(id);
        entity.setVersion(version);
        if (recurringScheduleId != null) {
            RecurringSchedule recurringSchedule = recurringScheduleService.findById(recurringScheduleId);
            entity.setRecurringSchedule(recurringSchedule);
        }
        entity.setEstimatedDate(estimatedDate);
        entity.setEstimatedAmount(estimatedAmount);
        entity.setComment(StringUtils.trimToNull(comment));
        if (tentativeAccountId != null) {
            Account tentativeAccount = tentativeAccountService.findById(tentativeAccountId);
            entity.setTentativeAccount(tentativeAccount);
        }
        if (tentativeCategoryId != null) {
            Category tentativeCategory = tentativeCategoryService.findById(tentativeCategoryId);
            entity.setTentativeCategory(tentativeCategory);
        }
        if (actualTransactionId != null) {
            Transaction actualTransaction = actualTransactionService.findById(actualTransactionId);
            entity.setActualTransaction(actualTransaction);
        }
        entity.setSkipped(skipped);
        return entity;
    }

    public void setRecurringScheduleService(RecurringScheduleService recurringScheduleService) {
        this.recurringScheduleService = recurringScheduleService;
    }

    public void setTentativeAccountService(AccountService tentativeAccountService) {
        this.tentativeAccountService = tentativeAccountService;
    }

    public void setTentativeCategoryService(CategoryService tentativeCategoryService) {
        this.tentativeCategoryService = tentativeCategoryService;
    }

    public void setActualTransactionService(TransactionService actualTransactionService) {
        this.actualTransactionService = actualTransactionService;
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

    public Long getRecurringScheduleId() {
        return recurringScheduleId;
    }

    public void setRecurringScheduleId(Long recurringScheduleId) {
        this.recurringScheduleId = recurringScheduleId;
    }

    public String getRecurringSchedule() {
        return recurringSchedule;
    }

    public Date getEstimatedDate() {
        return estimatedDate;
    }

    public void setEstimatedDate(Date estimatedDate) {
        this.estimatedDate = estimatedDate;
    }

    public BigDecimal getEstimatedAmount() {
        return estimatedAmount;
    }

    public void setEstimatedAmount(BigDecimal estimatedAmount) {
        this.estimatedAmount = estimatedAmount;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Long getTentativeAccountId() {
        return tentativeAccountId;
    }

    public void setTentativeAccountId(Long tentativeAccountId) {
        this.tentativeAccountId = tentativeAccountId;
    }

    public String getTentativeAccount() {
        return tentativeAccount;
    }

    public Long getTentativeCategoryId() {
        return tentativeCategoryId;
    }

    public void setTentativeCategoryId(Long tentativeCategoryId) {
        this.tentativeCategoryId = tentativeCategoryId;
    }

    public String getTentativeCategory() {
        return tentativeCategory;
    }

    public Long getActualTransactionId() {
        return actualTransactionId;
    }

    public void setActualTransactionId(Long actualTransactionId) {
        this.actualTransactionId = actualTransactionId;
    }

    public String getActualTransaction() {
        return actualTransaction;
    }

    public Boolean getSkipped() {
        return skipped;
    }

    public void setSkipped(Boolean skipped) {
        this.skipped = skipped;
    }
}
