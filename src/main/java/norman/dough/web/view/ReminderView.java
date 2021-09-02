package norman.dough.web.view;

import norman.dough.domain.Reminder;

import java.math.BigDecimal;
import java.util.Date;

public class ReminderView {
    private Long id;
    private Integer version;
    private Long recurringScheduleId;
    private String recurringSchedule;
    private Date estimatedDate;
    private BigDecimal estimatedAmount;
    private String comment;
    private Long tentativeAccountId;
    private String tentativeAccount;
    private Long tentativeCategoryId;
    private String tentativeCategory;
    private Long actualTransactionId;
    private String actualTransaction;
    private Boolean skipped;

    public ReminderView(Reminder entity) {
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

    public Long getId() {
        return id;
    }

    public Integer getVersion() {
        return version;
    }

    public Long getRecurringScheduleId() {
        return recurringScheduleId;
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

    public Long getTentativeAccountId() {
        return tentativeAccountId;
    }

    public String getTentativeAccount() {
        return tentativeAccount;
    }

    public Long getTentativeCategoryId() {
        return tentativeCategoryId;
    }

    public String getTentativeCategory() {
        return tentativeCategory;
    }

    public Long getActualTransactionId() {
        return actualTransactionId;
    }

    public String getActualTransaction() {
        return actualTransaction;
    }

    public Boolean getSkipped() {
        return skipped;
    }
}
