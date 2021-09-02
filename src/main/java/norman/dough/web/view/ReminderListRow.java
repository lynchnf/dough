package norman.dough.web.view;

import norman.dough.domain.Reminder;
import norman.dough.domain.RecurringSchedule;
import norman.dough.domain.Account;
import norman.dough.domain.Category;
import norman.dough.domain.Transaction;

import java.math.BigDecimal;
import java.util.Date;

public class ReminderListRow {
    private Long id;
    private Integer version;
    private String recurringSchedule;
    private Date estimatedDate;
    private BigDecimal estimatedAmount;
    private String comment;
    private String tentativeAccount;
    private String tentativeCategory;
    private String actualTransaction;
    private Boolean skipped;

    public ReminderListRow(Reminder entity) {
        id = entity.getId();
        version = entity.getVersion();
        recurringSchedule = entity.getRecurringSchedule() == null ? null : entity.getRecurringSchedule().toString();
        estimatedDate = entity.getEstimatedDate();
        estimatedAmount = entity.getEstimatedAmount();
        comment = entity.getComment();
        tentativeAccount = entity.getTentativeAccount() == null ? null : entity.getTentativeAccount().toString();
        tentativeCategory = entity.getTentativeCategory() == null ? null : entity.getTentativeCategory().toString();
        actualTransaction = entity.getActualTransaction() == null ? null : entity.getActualTransaction().toString();
        skipped = entity.getSkipped();
    }

    public Long getId() {
        return id;
    }

    public Integer getVersion() {
        return version;
    }

    public String getRecurringSchedule() {
        return recurringSchedule;
    }

    public Date getEstimatedDate() {
        return estimatedDate;
    }

    public BigDecimal getEstimatedAmount() {
        return estimatedAmount;
    }

    public String getComment() {
        return comment;
    }

    public String getTentativeAccount() {
        return tentativeAccount;
    }

    public String getTentativeCategory() {
        return tentativeCategory;
    }

    public String getActualTransaction() {
        return actualTransaction;
    }

    public Boolean getSkipped() {
        return skipped;
    }
}
