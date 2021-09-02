package norman.dough.web.view;

import norman.dough.domain.RecurringSchedule;

import java.math.BigDecimal;

public class RecurringScheduleView {
    private Long id;
    private Integer version;
    private String cronString;
    private BigDecimal estimatedAmount;
    private String comment;
    private Long tentativeAccountId;
    private String tentativeAccount;
    private Long tentativeCategoryId;
    private String tentativeCategory;

    public RecurringScheduleView(RecurringSchedule entity) {
        id = entity.getId();
        version = entity.getVersion();
        cronString = entity.getCronString();
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
    }

    public Long getId() {
        return id;
    }

    public Integer getVersion() {
        return version;
    }

    public String getCronString() {
        return cronString;
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
}
